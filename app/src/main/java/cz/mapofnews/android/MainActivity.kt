package cz.mapofnews.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.backendless.Backendless
import cz.mapofnews.R
import cz.mapofnews.android.widgets.MySlidingPaneLayout
import cz.mapofnews.api.AppCallback
import cz.mapofnews.service.Event
import cz.mapofnews.service.News
import cz.mapofnews.service.RetrieveManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_right_panel.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainActivity :
        AppCompatActivity(),
        MapViewFragment.OnFragmentInteractionListener,
        MySlidingPaneLayout.PanelSlideListener,
        HasSupportFragmentInjector {

    // injector for fragments
    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val SERVER_URL: String by lazy { resources.getString(R.string.SERVER_URL) }
    private val APPLICATION_ID: String by lazy { resources.getString(R.string.APPLICATION_ID) }
    private val API_KEY: String by lazy { resources.getString(R.string.API_KEY) }


    // tag for logging
    private val TAG = MainActivity::class.java.name

    // map view fragment
    private lateinit var mapViewFragment: MapViewFragment

    // right pane
    private lateinit var rightPanel: MySlidingPaneLayout

    // data manager
    @Inject lateinit var retrieveManager: RetrieveManager

    // active event
    private var activeEvent: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // initialize backendless app and conect to the server
        Backendless.setUrl(SERVER_URL)
        Backendless.initApp(applicationContext, APPLICATION_ID, API_KEY)

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
        mapViewFragment.mapUntouched = false
        rightPanel.openPane()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onUpdateClick(v: View) {
        mapViewFragment.loadAndShowMarkers()
        mapViewFragment.mapUntouched = false
        rightPanel.openPane()
    }

    /**
     * When the marker is clicked, this method is called to show the right panel.
     */
    override fun onEventClick(eventId: String) {
        // load data from data layer
        val event: Event = retrieveManager.events[eventId] ?: throw IllegalArgumentException("Event with objectId $eventId does not exists")

        // assign data to the layout
        titleView.text = event.title
        abstractView.text = event.abstract
        sourceView.text = event.source
        dateView.text = extractDate(event.eventDate)
        // initially, set read button visibility to gone, it stays like that only if both newsId and link are null
        readBtn.visibility = View.GONE
        if (event.newsId == null) {
            if (event.link != null) {
                // event has no news assigned, instead read btn, we show open original article
                readBtn.text = resources.getString(R.string.read_original)
                readBtn.setOnClickListener({ view -> openOriginalArticle(view) })
                readBtn.visibility = View.VISIBLE
            }
        } else {
            // event has an news text attached, so we display read button
            readBtn.text = resources.getString(R.string.read_it)
            readBtn.setOnClickListener({ view -> startNewsActivity(view) })
            readBtn.visibility = View.VISIBLE
        }

        // store event
        activeEvent = event

        // open the panel
        rightPanel.closePane()
    }

    @Suppress("UNUSED_PARAMETER")
    fun startNewsActivity(v: View) {
        // create intent (to start the NewsActivity)
        val newsIntent = Intent(this, NewsActivity::class.java)
        // test if there is active event
        val event = activeEvent ?: throw IllegalStateException("Trying to start news activity though no event is active.")
        // test if the event has news attached
        val newsId = event.newsId ?: throw IllegalStateException("Event $event has no news attached.")
        // retrieve news
        retrieveManager.fetchNews(newsId,
                object : AppCallback<News?> {
                    override fun handleResponse(response: News?) {
                        if (response == null) throw IllegalStateException("Trying to start news activity though event does have news attached")
                        // add news details to the intent
                        newsIntent.putExtra(NewsActivity.EXTRA_TEXT, response.text)
                        newsIntent.putExtra(NewsActivity.EXTRA_TITLE, event.title)
                        newsIntent.putExtra(NewsActivity.EXTRA_LINK, event.link)
                        // start the avtivity
                        startActivity(newsIntent)
                    }

                })
    }

    @Suppress("UNUSED_PARAMETER")
    fun openOriginalArticle(v: View) {
        // craete intent to open web browser
        val webBrowserIntent = Intent(Intent.ACTION_VIEW)
        // test if there is active event
        val event = activeEvent ?: throw IllegalStateException("Trying to start news activity though no event is active.")
        webBrowserIntent.data = Uri.parse(event.link) ?: throw IllegalStateException("Link for the original article is null.")
        // start intent
        startActivity(webBrowserIntent)
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

    /**
     * Extracts string label of the date from the [Date].
     * Returns labels 'today' or 'yesterday' or date in the format as it is specified in the resources.
     */
    private fun extractDate(date: Date): String {
        val eventDate = Calendar.getInstance()
        eventDate.time = date
        val today = Calendar.getInstance()
        // set all timing variables to 0 so that we compare only dates
        eventDate.isLenient = false
        eventDate.set(Calendar.HOUR_OF_DAY, 0)
        eventDate.set(Calendar.MINUTE, 0)
        eventDate.set(Calendar.SECOND, 0)
        eventDate.set(Calendar.MILLISECOND, 0)
        today.isLenient = false
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // today
        if (eventDate == today) return getString(R.string.today)
        // yesterday
        today.add(Calendar.DATE, -1)
        if (eventDate == today) return getString(R.string.yesterday)
        // other date
        return SimpleDateFormat(getString(R.string.date_format)).format(date)
    }

    /**
     * Dagger routine to provide clever injection of a fragment into activity.
     */
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    /**
     * When map view fragment is attached to the activity, we store the reference to this fragment.
     */
    fun registerMapViewFragment(fragment: MapViewFragment) {
        mapViewFragment = fragment
    }

}
