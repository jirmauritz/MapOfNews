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
import cz.mapofnews.data.EventManager

/**
 * Google Maps view fragment.
 * Activities that contain this fragment must implement the
 * [MapViewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapViewFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // default zoom (will be reset to the number from properties)
    private var defaultZoom: Float = 7f
    // borders of the map (will be overriden by arguments, default numbers are set to Czechia)
    private var southBorder: Double = 48.55
    private var westBorder: Double = 12.131196
    private var northBorder: Double = 51.05
    private var eastBorder: Double = 18.881621

    private var mListener: OnFragmentInteractionListener? = null

    // google map fragment reference
    private lateinit var gMap: GoogleMap
    // center of the map
    private lateinit var mapCenter: LatLng

    // tag that is used in the logs
    private val TAG = MapViewFragment::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            defaultZoom = arguments.getFloat(DEFAULT_ZOOM)
            southBorder = arguments.getDouble(SOUTH_BORDER)
            westBorder = arguments.getDouble(WEST_BORDER)
            northBorder = arguments.getDouble(NORTH_BORDER)
            eastBorder = arguments.getDouble(EAST_BORDER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater == null) {
            throw NullPointerException("Inflater is null in onCreateView.")
        }
        val rootView = inflater.inflate(R.layout.fragment_map_view, container, false)

        val mapView = rootView.findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState);

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
        val viewBorder = LatLngBounds(LatLng(southBorder, westBorder), LatLng(northBorder, eastBorder))
        // constrain the camera target to the country bounds
        gMap.setLatLngBoundsForCameraTarget(viewBorder)

        // move the camera to the center of the target country and set minimal zoom
        mapCenter = viewBorder.center
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewBorder.center, defaultZoom))

        // load and show all news markers
        loadAndShowMarkers()
    }

    private fun loadAndShowMarkers() {
        for (event in EventManager.getAllEvents()) {
            val marker = gMap.addMarker(MarkerOptions()
                    .position(LatLng(event.lat, event.lng))
                    .alpha(0.9f)
                    .icon(BitmapDescriptorFactory.fromResource(event.type.resNum)))
            marker.tag = event.id
        }
    }

    fun restore() {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, defaultZoom))
    }

    fun centralizeMarker(panelOpened: Boolean, markerPosition: LatLng?) {
        // if marker is null, force him to be center of the map
        val markerPosition = markerPosition ?: gMap.cameraPosition.target
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
                mListener?.onEventClick(marker.tag as Long)

                return true
            } catch (e: NumberFormatException) {
                Log.e(TAG, "Marker contains tag which is not Long, but ${marker.tag!!::class.java.name}")
            }
        }
        return false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
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
        fun onEventClick(eventId: Long)
    }

    companion object {
        private val DEFAULT_ZOOM = "DEFAULT_ZOOM"
        private val SOUTH_BORDER = "SOUTH_BORDER"
        private val WEST_BORDER = "WEST_BORDER"
        private val NORTH_BORDER = "NORTH_BORDER"
        private val EAST_BORDER = "EAST_BORDER"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters. Center
         * of the map will be computed from the borders.
         *
         * @param defaultZoom Zoom level for the Google Maps.
         * @param southBorder South border of the map.
         * @param westBorder West border of the map.
         * @param northBorder North border of the map.
         * @param eastBorder East border of the map.
         * @return A new instance of fragment MapViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(defaultZoom: Float, southBorder: Double, westBorder: Double, northBorder: Double, eastBorder: Double): MapViewFragment {
            val fragment = MapViewFragment()
            val args = Bundle()
            args.putFloat(DEFAULT_ZOOM, defaultZoom)
            args.putDouble(SOUTH_BORDER, southBorder)
            args.putDouble(WEST_BORDER, westBorder)
            args.putDouble(NORTH_BORDER, northBorder)
            args.putDouble(EAST_BORDER, eastBorder)
            fragment.arguments = args
            return fragment
        }
    }
}
