package com.android.grechkaapp.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.android.grechkaapp.Method
import com.android.grechkaapp.R
import com.android.grechkaapp.SlideshowDialogFragment
import com.android.grechkaapp.adapter.GalleryAdapter
import com.android.grechkaapp.api.ServerApi
import com.android.grechkaapp.item.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.content_photo.*
import java.io.Serializable

class PhotoActivity : AppCompatActivity() {

    private var images: List<Model.Photo>? = null
    private var mAdapter: GalleryAdapter? = null

    lateinit var progressDialog: ProgressDialog

    private var disposable: Disposable? = null

    private val serverApi by lazy {
        ServerApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        setSupportActionBar(toolbarPhoto)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbarPhoto.setNavigationOnClickListener { finish() }

        fabPhoto.setOnClickListener { view ->
            Method(this).fabMethod()
        }

        // Set up progress before call
        progressDialog = ProgressDialog(this@PhotoActivity)
        progressDialog.max = 100
        progressDialog.setMessage("Загрузка ....")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCanceledOnTouchOutside(false)
        // show it
        progressDialog.show()

        disposable = serverApi.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            progressDialog.dismiss()

                            images = result.photo

                            mAdapter = GalleryAdapter(this, images!!)

                            val mLayoutManager = GridLayoutManager(applicationContext, 2)
                            recyclerView.layoutManager = mLayoutManager
                            recyclerView.itemAnimator = DefaultItemAnimator()
                            recyclerView.adapter = mAdapter

//                            recyclerView.addOnItemTouchListener(GalleryAdapter.RecyclerTouchListener(applicationContext, recyclerView, GalleryAdapter.ClickListener{
//
//                            }))

                            recyclerView.addOnItemTouchListener(GalleryAdapter.RecyclerTouchListener(applicationContext, recyclerView, object : GalleryAdapter.ClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val bundle = Bundle()
                                    bundle.putSerializable("images", images as Serializable)
                                    bundle.putInt("position", position)

                                    val ft = supportFragmentManager.beginTransaction()
                                    val newFragment = SlideshowDialogFragment.newInstance()
                                    newFragment.arguments = bundle
                                    newFragment.show(ft, "slideshow")
                                }

                                override fun onLongClick(view: View, position: Int) {

                                }
                            }))

                        },
                        { error ->
                            progressDialog.dismiss()
                            Toast.makeText(this@PhotoActivity, "Проверьте интернет соединение !!!", Toast.LENGTH_SHORT).show()
                        }
                )




    }

}
