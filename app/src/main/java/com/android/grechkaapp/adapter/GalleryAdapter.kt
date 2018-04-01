package com.android.grechkaapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import com.android.grechkaapp.R
import com.android.grechkaapp.item.Model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Created by Lenovo on 30.03.2018.
 */
class GalleryAdapter(private var mCtx: Context, private var images: List<Model.Photo>)
    : RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.gallery_thumbnail, parent, false)

        return MyViewHolder(itemView)
    }

    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val photo: Model.Photo = images[position]

        Glide.with(mCtx).load(photo.url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder!!.thumbnail)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    interface ClickListener{
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)
    }

    class RecyclerTouchListener() : RecyclerView.OnItemTouchListener{

        constructor(ctx: Context, recyclerView: RecyclerView, clickListener: GalleryAdapter.ClickListener?) : this() {
            this.clickListener = clickListener
            gestureDetector = GestureDetector(ctx, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        private var gestureDetector: GestureDetector? = null
        private var clickListener: GalleryAdapter.ClickListener? = null



//        fun RecyclerTouchListener(ctx: Context, recyclerView: RecyclerView, clickListener: GalleryAdapter.ClickListener?) {
//            this.clickListener = clickListener
//            gestureDetector = GestureDetector(ctx, object : GestureDetector.SimpleOnGestureListener() {
//                override fun onSingleTapUp(e: MotionEvent): Boolean {
//                    return true
//                }
//
//                override fun onLongPress(e: MotionEvent) {
//                    val child = recyclerView.findChildViewUnder(e.x, e.y)
//                    if (child != null && clickListener != null) {
//                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
//                    }
//                }
//            })
//        }

        override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
            val child = rv!!.findChildViewUnder(e!!.x, e.y)
            if (child != null && clickListener != null && gestureDetector!!.onTouchEvent(e)) {
                clickListener!!.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }


    }
}