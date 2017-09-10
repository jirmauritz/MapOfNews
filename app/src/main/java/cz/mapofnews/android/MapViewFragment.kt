package cz.mapofnews.android

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import cz.mapofnews.R
import cz.mapofnews.api.AppCallback
import cz.mapofnews.service.Event
import cz.mapofnews.service.RetrieveManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Google Maps view fragment.
 * Activities that contain this fragment must implement the
 * [MapViewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class MapViewFragment :
        Fragment(),
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveStartedListener {

    private var mListener: OnFragmentInteractionListener? = null

    // google map fragment reference
    private lateinit var gMap: GoogleMap
    // center of the map
    private lateinit var mapCenter: LatLng
    // boolean is true only if the last interaction with the map was marker, becomes false when touching the map
    var mapUntouched = false

    // tag that is used in the logs
    private val TAG = MapViewFragment::class.java.name

    // inject retrieve manager
    @Inject lateinit var retrieveManager: RetrieveManager

    private val defaultZoom: Float by lazy { resources.getString(R.string.defaultZoom).toFloat() }
    private val southBorder: Double by lazy { resources.getString(R.string.southBorder).toDouble() }
    private val westBorder: Double by lazy { resources.getString(R.string.westBorder).toDouble() }
    private val northBorder: Double by lazy { resources.getString(R.string.northBorder).toDouble() }
    private val eastBorder: Double by lazy { resources.getString(R.string.eastBorder).toDouble() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater == null) {
            throw NullPointerException("Inflater is null in onCreateView.")
        }
        val rootView = inflater.inflate(R.layout.fragment_map_view, container, false)

        val mapView = rootView.findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        mapView.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync(this)

        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // save google map instance
        gMap = googleMap

        // register for the marker click
        gMap.setOnMarkerClickListener(this)

        // register for the map click
        gMap.setOnCameraMoveStartedListener(this)

        // set map style from map_style.json, possible to edit on https://mapstyle.withgoogle.com/
        try {
            val success = gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Map style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }

        // disable rotation of the map by two fingers
        gMap.uiSettings.isRotateGesturesEnabled = false

        // set minimal zoom level, so that users dont zoom out to whole europe
        gMap.setMinZoomPreference(defaultZoom)

        // restrict the view on the country only
        val viewBorder = LatLngBounds(
                LatLng(southBorder, westBorder),
                LatLng(northBorder, eastBorder))
        // constrain the camera target to the country bounds
        gMap.setLatLngBoundsForCameraTarget(viewBorder)

        // move the camera to the center of the target country and set minimal zoom
        mapCenter = viewBorder.center
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewBorder.center, defaultZoom))

        // load and show all news markers
        loadAndShowMarkers()
    }

    fun loadAndShowMarkers() {
        // first remove all markers in case some are in the map
        gMap.clear()
        // retrieve markers from the backend
        retrieveManager.fetchAllEvents(object : AppCallback<List<Event>> {
            override fun handleResponse(response: List<Event>) {
                for (event in response) {
                    val marker = gMap.addMarker(MarkerOptions()
                            .position(LatLng(event.lat, event.lng))
                            .alpha(0.9f)
                            .icon(BitmapDescriptorFactory.fromResource(event.type.resNum)))
                    marker.tag = event.objectId
                }
            }
        })
    }

    fun restore() {
        //     gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, defaultZoom.toFloat()))
    }

    fun centralizeMarker(panelOpened: Boolean, markerPositionParam: LatLng?) {
        // if marker is null, force him to be center of the map
        val markerPosition = markerPositionParam ?: gMap.cameraPosition.target
        // set untouched
        mapUntouched = true
        // move map so that the marker is centralized in the left part
        var dX = resources.getDimensionPixelSize(R.dimen.map_xoffset_when_marker_focus)
        var dY = resources.getDimensionPixelSize(R.dimen.map_yoffset_when_marker_focus)
        if (!panelOpened) {
            // revert direction
            dX *= -1
            dY *= -1
        }
        val projection = gMap.projection
        val markerPoint = projection.toScreenLocation(markerPosition)
        markerPoint.offset(dX, dY)
        gMap.animateCamera(CameraUpdateFactory.newLatLng(projection.fromScreenLocation(markerPoint)))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker != null && marker.tag != null) {
            try {
                // centralize map on marker
                centralizeMarker(true, marker.position)
                // let the main activity know the marker was clicked
                mListener?.onEventClick(marker.tag as String)

                return true
            } catch (e: NumberFormatException) {
                Log.e(TAG, "Marker contains tag which is not Long, but ${marker.tag!!::class.java.name}")
            }
        }
        return false
    }

    override fun onCameraMoveStarted(reason: Int) {
        // set untouched only if user moved with the map
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mapUntouched = false
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        // get parent activity
        if (context !is MainActivity) throw RuntimeException(context!!.toString() + " must be MainActivity.")
        val mainActivity: MainActivity = context
        // register fragment in activity
        mainActivity.registerMapViewFragment(this)
        // register activity in fragment
        mListener = context
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     */
    interface OnFragmentInteractionListener {
        fun onEventClick(eventId: String)
    }
}
