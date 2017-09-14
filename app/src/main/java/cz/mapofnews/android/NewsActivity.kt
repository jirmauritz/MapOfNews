package cz.mapofnews.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cz.mapofnews.R
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TEXT = "cz.mapofnews.NewsActivity.Text"
        val EXTRA_TITLE = "cz.mapofnews.NewsActivity.Title"
        val EXTRA_LINK = "cz.mapofnews.NewsActivity.Link"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // get details of the news from the parent intent and display it
        textView.text = intent.getStringExtra(EXTRA_TEXT)
        titleView.text = intent.getStringExtra(EXTRA_TITLE)

    }

    @Suppress("UNUSED_PARAMETER")
    fun returnBack(v: View) {
        finish()
    }

    /**
     * Opens original article in a new intent.
     */
    @Suppress("UNUSED_PARAMETER")
    fun openOriginalArticle(v: View) {
        // craete intent to open web browser
        val webBrowserIntent = Intent(Intent.ACTION_VIEW)
        webBrowserIntent.data = Uri.parse(intent.getStringExtra(EXTRA_LINK))
        // start intent
        startActivity(webBrowserIntent)
    }

}
