package com.android.grechkaapp

import android.support.v4.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.grechkaapp.item.Model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.ArrayList

/**
 * Created by Lenovo on 30.03.2018.
 */
class SlideshowDialogFragment : DialogFragment() {

    private val TAG = SlideshowDialogFragment::class.java.simpleName
    private var images: ArrayList<Model.Photo>? = null
    private var viewPager: ViewPager? = null
    private var selectedPosition = 0

    companion object {
        fun newInstance(): SlideshowDialogFragment = SlideshowDialogFragment()
    }

    fun newInstance(): SlideshowDialogFragment {
        return SlideshowDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view: View = inflater!!.inflate(R.layout.fragment_image_slider, container, false)
        viewPager = view.findViewById(R.id.viewpager)

        images = arguments.getSerializable("images") as ArrayList<Model.Photo>
        selectedPosition = arguments.getInt("position")

        val myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.setAdapter(myViewPagerAdapter)
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)

        setCurrentItem(selectedPosition)

        return view
    }

    private fun setCurrentItem(position: Int){
        viewPager!!.setCurrentItem(position, false)
    }

    //	page change listener
    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }


    //	adapter
    inner class MyViewPagerAdapter : PagerAdapter() {

        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(R.layout.image_fullscreen_preview, container, false)

            val imageViewPreview = view.findViewById<ImageView>(R.id.image_preview)

            val photo = images!!.get(position)

            Glide.with(activity).load(photo.url)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview)

            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return images!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj as View
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


}