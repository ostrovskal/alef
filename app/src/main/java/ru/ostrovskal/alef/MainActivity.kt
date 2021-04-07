package ru.ostrovskal.alef

import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var json: String
        var urls: MutableList<String>? = null
        startLog(this, "ALEF", BuildConfig.VERSION_NAME, BuildConfig.DEBUG)
        super.onCreate(savedInstanceState)
        try {
            json = loadListPicture("http://dev-tasks.alef.im/task-m-001/list.php")
            if(json.isEmpty()) {

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val errorLoad = urls.isNullOrEmpty()
        setContentView(if(errorLoad) R.layout.error else R.layout.activity_main)
        if(errorLoad) return
        val fimg = findViewById<ImageView>(R.id.fullImage)
        val grid = findViewById<GridView>(R.id.gridView)
        grid?.let {
            it.columnWidth = if(Config.isPortrait) 120.dp else 150.dp
            it.adapter = PicAdapter(this, urls ?: listOf()) { iv: ImageView ->
                it.visibility = View.GONE
                fimg.visibility = View.VISIBLE
                val url = iv.tag as String
                Picasso.with(this)
                    .load(url)
                    .placeholder(android.R.drawable.stat_sys_upload)
                    .error(android.R.drawable.stat_notify_error)
                    .into(fimg)
            }
        }
        fimg?.apply {
            setOnClickListener {
                visibility = View.GONE
                grid.visibility = View.VISIBLE
            }
        }
    }

    private fun loadListPicture(url: String) : String {
        var result = ""
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                "failure".debug()
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    result = response.body()?.string() ?: ""
                } catch( e: IOException) {
                    "response exception".debug()
                    e.printStackTrace()
                }
            }
        })
        return result
    }
}
