package ru.ostrovskal.alef

import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var adapt: PicAdapter?  = null
    var grid: GridView?             = null
    var terr: TextView?             = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startLog(this, "ALEF", BuildConfig.VERSION_NAME, BuildConfig.DEBUG)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fimg = findViewById<ImageView>(R.id.fullImage)
        grid = findViewById(R.id.gridView)
        terr = findViewById(R.id.errMsg)
        grid?.let {
            it.columnWidth = if(Config.isPortrait) 120.dp else 150.dp
            adapt = PicAdapter(this) { iv: ImageView ->
                it.visibility = View.GONE
                fimg.visibility = View.VISIBLE
                val url = iv.tag as String
                Picasso.with(this)
                    .load(url)
                    .placeholder(android.R.drawable.stat_sys_upload)
                    .error(android.R.drawable.stat_notify_error)
                    .into(fimg)
            }
            it.adapter = adapt
        }
        fimg?.apply {
            setOnClickListener {
                visibility = View.GONE
                grid?.visibility = View.VISIBLE
            }
        }
        try {
            loadListPicture("http://dev-tasks.alef.im/task-m-001/list.php")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadListPicture(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                runAction(true)
            }

            override fun onResponse(call: Call, response: Response) {
                var result = ""
                try {
                    result = response.body()?.string() ?: ""
                    if(result.isNotEmpty()) {
                        val resp = "{\"urls\":$result}"
                        val urls = MutableList<String>(0) { "" }
                        JSONObject(resp).optJSONArray("urls")?.apply {
                            repeat(length()) { urls.add(this.getString(it)) }
                        }
                        adapt?.urls = urls
                    }
                } catch (e: IOException) {
                    "response exception".debug()
                    e.printStackTrace()
                } catch(j: JSONException) {
                    j.info()
                }
                runAction(result.isEmpty())
            }

            fun runAction(err: Boolean) {
                this@MainActivity.runOnUiThread {
                    if(err) {
                        terr?.visibility = View.VISIBLE
                        "failure".debug()
                    } else {
                        adapt?.notifyDataSetChanged()
                        grid?.visibility = View.VISIBLE
                        "success".debug()
                    }
                }

            }
        })
    }
}
