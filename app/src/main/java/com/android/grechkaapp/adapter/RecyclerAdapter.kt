package com.android.grechkaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.grechkaapp.R
import com.android.grechkaapp.item.Model

import com.squareup.picasso.Picasso

/**
 * Created by Lenovo on 30.03.2018.
 */
class RecyclerAdapter(private var mCtx: Context, private var list: List<Model.Link>, private var listener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Model.Link)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tvTextLink!!.text = list[position].linkurl

        Picasso.with(mCtx)
                .load(list[position].linkicon)
                .into(holder.imgImageLink, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //                        Toast.makeText(context, "Load Success", Toast.LENGTH_SHORT).show();
                    }

                    override fun onError() {
                        //                        Toast.makeText(context, "Load Error", Toast.LENGTH_SHORT).show();
                    }
                })

        holder.bind(list[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_recycler_view_link, parent, false)
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgImageLink = itemView.findViewById<ImageView>(R.id.imgImageLink)!!
        val tvTextLink = itemView.findViewById<TextView>(R.id.tvTextLink)!!

        fun bind(link: Model.Link, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(link) }
        }
    }




}