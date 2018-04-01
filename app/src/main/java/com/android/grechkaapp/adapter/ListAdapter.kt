package com.android.grechkaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.grechkaapp.R
import com.android.grechkaapp.item.Model
import com.squareup.picasso.Picasso

/**
 * Created by Lenovo on 30.03.2018.
 */
class ListAdapter(private var mCtx: Context, private var list: List<Model.Music>, var pos: String?) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(R.layout.item_list_view, parent, false)

        val tvSongName: TextView = view.findViewById(R.id.tvSongName)
        tvSongName.text = list.get(position).name

        val imgPlay: ImageView = view.findViewById(R.id.imgPlay)

        when {
            pos == null -> display(imgPlay, R.mipmap.ic_arrow_play)
            position == pos!!.toInt() -> display(imgPlay, R.mipmap.ic_pouse)
            else -> display(imgPlay, R.mipmap.ic_arrow_play)
        }

        return view
    }

    private fun display(imgView: ImageView, imgRes: Int){
        Picasso.with(mCtx)
                .load(imgRes)
                .into(imgView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        // Toast.makeText(context, "Load Success", Toast.LENGTH_SHORT).show();
                    }

                    override fun onError() {
                        // Toast.makeText(context, "Load Error", Toast.LENGTH_SHORT).show();
                    }
                })
    }

}