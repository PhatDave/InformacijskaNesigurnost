package com.example.jebeniis

import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.lang3.StringUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: Adapter? = null
    private lateinit var searchBox: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val questions = questions
        val keyList = ArrayList(
            questions!!.keys
        )
        searchBox = findViewById(R.id.searchBox)
        searchBox.setOnEditorActionListener{ _: TextView?, _: Int, _: KeyEvent? ->
            val search = searchBox.text.toString()
            val filteredKeys: MutableList<FilterItem> = ArrayList()
            for (key in keyList) {
                key.lowercase(Locale.getDefault())
                var minLev = 10000
                val words = key.split(" ").toTypedArray()
                for (word in words) {
                    @Suppress("DEPRECATION") val lev = StringUtils.getLevenshteinDistance(word, search)
                    if (lev < minLev) {
                        minLev = lev
                    }
                }
                if (minLev < 8) {
                    filteredKeys.add(FilterItem(key, minLev))
                }
            }
            filteredKeys.sortWith{ o1: FilterItem, o2: FilterItem -> o1.lev - o2.lev }
            keyList.removeIf { true }
            for (item in filteredKeys) {
                keyList.add(item.key)
            }
            true
        }
        adapter = Adapter(this, keyList, questions)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.also { recyclerView.adapter = it }
    }

    private val questions: HashMap<String, String>?
        get() {
            val content = "is.json".loadAsset()
            val questions = HashMap<String, String>()
            try {
                val jsonObject = content?.let { JSONObject(it) }
                jsonObject?.keys()?.forEachRemaining { key: String ->
                    try {
                        val value = jsonObject.getString(key)
                        key.lowercase(Locale.getDefault())
                        questions[key] = value
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                return questions
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }

    private fun String.loadAsset(): String? {
        val json: String = try {
            val `is` = this@MainActivity.assets.open(this)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}