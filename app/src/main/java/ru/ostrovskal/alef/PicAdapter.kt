package ru.ostrovskal.alef

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso

class PicAdapter(private val context: Context, private val urls: List<String>, private val notify: (iv: ImageView) -> Unit): BaseAdapter() {
    // фон миниатюры(кэшируемо)
    private var bkg: ColorDrawable? = null

    init {
        bkg = ColorDrawable(0xff00ff00.toInt())
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return ((convertView ?: ImageView(context)) as? ImageView)?.apply {
            val url = getItem(position)
            tag = url
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener{ notify(this) }
            background = bkg
            val cw = ((parent as? GridView)?.columnWidth ?: 120.dp) - 20.dp
            layoutParams = AbsListView.LayoutParams(cw, cw)
            Picasso.with(context)
                .load(url)
                .placeholder(android.R.drawable.stat_sys_upload)
                .error(android.R.drawable.stat_notify_error)
                .into(this)
        } ?: error("View is null!")
    }

    override fun getCount() = urls.size

    override fun getItem(position: Int): String = urls[position]

    override fun getItemId(position: Int) = position.toLong()
}