package com.android.grechkaapp.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.grechkaapp.Method
import com.android.grechkaapp.R
import com.android.grechkaapp.api.ServerApi
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : AppCompatActivity() {

    private lateinit var urlImg: String
    private lateinit var textAbout: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        val intent = intent
        urlImg = intent.getStringExtra("imgAbout")
        textAbout = intent.getStringExtra("textAbout")


        fab.setOnClickListener { view ->
            Method(this).fabMethod()
        }

//        ------------------------------

        tvScrollingAbout.text = Html.fromHtml(textAbout)

        Glide.with(this).load(urlImg).into(image_scrolling_top)



    }

}
