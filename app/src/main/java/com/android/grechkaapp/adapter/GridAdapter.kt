package com.android.grechkaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.grechkaapp.R
import com.android.grechkaapp.item.Model
import com.squareup.picasso.Picasso

/**
 * Created by Lenovo on 29.03.2018.
 */
class GridAdapter(var mCtx: Context, var resource: Int, var list: List<Model.Butmenu>)
    : ArrayAdapter<Model.Butmenu>(mCtx, resource, list) {

    override fun getCount(): Int {
        return list.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(R.layout.item_grid, parent, false)

        val imgItemGrid: ImageView = view.findViewById(R.id.imgItemGrid)
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)

        tvMenuName.text = list.get(position).name

        Picasso
                .with(context)
                .load(list.get(position).icon)
                .into(imgItemGrid)

        return view
    }
}