package com.rubenrganga.lectornoticias

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            main()
        }
    }

    suspend fun main(){
        val url = "https://www.vidaextra.com/feedburner.xml" //El "feed" a leer
        val items = leerRss(url)
        val lvDatos = findViewById<ListView>(R.id.lvDatos)
        val adapter = ItemAdapter(this, items)

        lvDatos.adapter = adapter

        lvDatos.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.link))
            startActivity(intent)
        }
    }
}

suspend fun leerRss(url: String): List<Item> {

    val items = mutableListOf<Item>()

    withContext(Dispatchers.IO) {
        val inputStream = URL(url).openStream()
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(inputStream, null)

        var eventType = parser.eventType
        var currentElement = ""
        var currentItem: Item? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentElement = parser.name
                    if (currentElement == "item") {
                        currentItem = Item("", "","")
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "item") {
                        items.add(currentItem!!)
                    }
                    currentElement = ""
                }
                XmlPullParser.TEXT -> {
                    when (currentElement) {
                        "title" -> currentItem?.title = parser.text
                        "pubDate" -> currentItem?.date = parser.text
                        "link" -> currentItem?.link = parser.text
                    }
                }
            }
            eventType = parser.next()
        }

    }

    return items
}

