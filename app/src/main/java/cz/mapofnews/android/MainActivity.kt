package cz.mapofnews.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.backendless.Backendless
import cz.mapofnews.R
import cz.mapofnews.android.widgets.MySlidingPaneLayout
import cz.mapofnews.service.Event
import cz.mapofnews.service.RetrieveManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_right_panel.*
import java.util.*


class MainActivity : AppCompatActivity(), MapViewFragment.OnFragmentInteractionListener, MySlidingPaneLayout.PanelSlideListener {

    // property file name (must be located in assets directory)
    private val PROPERTY_FILE_NAME = "app.properties"
    // localization properties file name (must be located in assets directory)
    private val LOCALIZATION_FILE_NAME = "czechia.properties"

    // tag for logging
    private val TAG = MainActivity::class.java.name

    // map view fragment
    private lateinit var mapViewFragment: MapViewFragment
    // right pane
    private lateinit var rightPanel: MySlidingPaneLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load properties file
        val prop = Properties()
        baseContext.assets.open(PROPERTY_FILE_NAME).use {
            prop.load(it)
        }

        // load localization properties
        val localProp = Properties()
        baseContext.assets.open(LOCALIZATION_FILE_NAME).use {
            localProp.load(it)
        }

        // initialize backendless app and conect to the server
        Backendless.setUrl(prop.getProperty("SERVER_URL"))
        Backendless.initApp(applicationContext, prop.getProperty("APPLICATION_ID"), prop.getProperty("API_KEY"))

        // create map view fragment
        mapViewFragment = MapViewFragment.newInstance(
                localProp.getProperty("DEFAULT_ZOOM").toFloat(),
                localProp.getProperty("SOUTH_BORDER").toDouble(),
                localProp.getProperty("WEST_BORDER").toDouble(),
                localProp.getProperty("NORTH_BORDER").toDouble(),
                localProp.getProperty("EAST_BORDER").toDouble()
        )
        // inject map view fragment into our layout
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_frame_for_map, mapViewFragment, mapViewFragment::javaClass.name)
                .addToBackStack(null)
                .commit()

        // set the main view to this clas
        setContentView(R.layout.activity_main)

        // since the pane here is the map, this actually means close the right pane and open map
        slideLayout.openPane()
        // save the slide layout to close it later
        rightPanel = slideLayout
        // register this class to listen on panel
        rightPanel.setPanelSlideListener(this)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onRestoreClick(v: View) {
        mapViewFragment.restore()
    }

    /**
     * When the marker is clicked, this method is called to show the right panel.
     */
    override fun onEventClick(eventId: String) {
        // load data from data layer
        val event: Event = RetrieveManager.events.get(eventId) ?:
                throw IllegalArgumentException("Event with objectId $eventId does not exists")

        // assign data to the layout
        titleView.text = event.title
        abstractView.text = event.abstract
        sourceView.text = event.source

        // open the panel
        rightPanel.closePane()
    }

    // Slide Panel Event Listener methods
    override fun onPanelClosed(panel: View?) {}
    override fun onPanelSlide(panel: View?, slideOffset: Float) {}

    /**
     * In this case, this actually means right panel is closed.
     * We move the map back at the position before opening panel.
     */
    override fun onPanelOpened(panel: View?) {
        if (mapViewFragment.mapUntouched) {
            mapViewFragment.centralizeMarker(false, null)
            // we mark the map as 'touched' as we dont want repeatedly moving a map when user manually opens the pane
            mapViewFragment.mapUntouched = false
        }
    }

}
