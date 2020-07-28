package com.kedia.nogozo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private val adapter = RecyclerAdapter(mutableListOf())
    private var list: MutableList<String>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val jsonObject = JSONObject(readFromAsset())
        val names = jsonObject.getJSONArray("names")
        for (i in 0 until names.length())
            list?.add(names[i] as String)
        listenTextChange()
        setupRecycler()
    }

    private fun setupRecycler() {
       recyclerView.apply {
           adapter = this@MainActivity.adapter
           layoutManager = LinearLayoutManager(this@MainActivity)
       }
    }

    private fun listenTextChange() {
        editText.addTextChangedListener {
            filter(it.toString())
        }
    }

    private fun filter(text: String) {
        val list = mutableListOf<String>()
        for (element in this@MainActivity.list!!) {
            if (element.contains(text.toString(), ignoreCase = true) && (text == "").not())
                list.add(element)
        }
        adapter.setData(list)
    }

    private fun readFromAsset(): String? {
        var json: String? = null
        try {

            val inputStream = this.assets.open("names.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
}