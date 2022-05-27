package org.ieselcaminas.proyectofinal.ui.appFragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ieselcaminas.proyectofinal.databinding.FragmentHomeBinding
import org.ieselcaminas.proyectofinal.model.recyclerViewNews.New
import org.ieselcaminas.proyectofinal.model.recyclerViewNews.RecyclerViewAdapterNews
import org.ieselcaminas.proyectofinal.ui.LoadingDialog
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import kotlin.math.min


class Home: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val feedRSS = "https://www.lifecoach-directory.org.uk/blog/feed"
    private lateinit var recView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        recView = binding.recViewHome
        val news = ArrayList<New>(0)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val titles = ArrayList<String>(0)
                val links = ArrayList<String>(0)
                val dates = ArrayList<String>(0)
                val descriptions = ArrayList<String>(0)
                val url = URL(feedRSS)
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = false
                val xpp = factory.newPullParser()
                xpp.setInput(getInputStream(url), "UTF_8")
                var eventType = xpp.eventType
                var insideItem = false

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.name.lowercase() == "item") {
                            insideItem = true
                        } else if (xpp.name.lowercase() == "title") {
                            if (insideItem) {
                                titles.add(xpp.nextText())
                            }
                        } else if (xpp.name.lowercase() == "link") {
                            if (insideItem) {
                                links.add(xpp.nextText())
                            }
                        } else if (xpp.name.lowercase() == "pubdate") {
                            if (insideItem) {
                                dates.add(xpp.nextText())
                            }
                        } else if (xpp.name.lowercase() == "description") {
                            if (insideItem) {
                                descriptions.add(xpp.nextText())
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.name.lowercase() == "item") {
                        insideItem = false
                    }
                    eventType = xpp.next()
                }

                val min = min(min(titles.size,dates.size), min(links.size,descriptions.size))
                for (i in 0 until min) {
                    news.add(New(titles[i], links[i], dates[i], descriptions[i]))
                }
            }catch (e: Exception) {
                loadingDialog.dismissDialog()
            }
            handler.post {
                recView.setHasFixedSize(true)
                val adapter = RecyclerViewAdapterNews(news)
                recView.adapter = adapter
                recView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                loadingDialog.dismissDialog()
            }
        }
    }

    private fun getInputStream(url: URL): InputStream? {
        return try {
            url.openConnection().getInputStream()
        }catch (e: IOException) {
            null
        }
    }
}