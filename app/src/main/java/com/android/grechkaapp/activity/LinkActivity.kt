package com.android.grechkaapp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.android.grechkaapp.Method
import com.android.grechkaapp.R
import com.android.grechkaapp.adapter.RecyclerAdapter
import com.android.grechkaapp.api.ServerApi
import com.android.grechkaapp.item.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_link.*
import kotlinx.android.synthetic.main.content_link.*


class LinkActivity : AppCompatActivity() {

//    lateinit var linkList: List<Model.Link>

    private lateinit var linearLayoutManager: LinearLayoutManager

    private var progressDialog: ProgressDialog? = null

    private var disposable: Disposable? = null

    private val serverApi by lazy {
        ServerApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link)
        setSupportActionBar(toolbarLink)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewLinks.layoutManager = linearLayoutManager

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbarLink.setNavigationOnClickListener { finish() }

        fabLink.setOnClickListener { view ->
            Method(this).fabMethod()
        }

//        -----------------------------

//        linkList = mutableListOf<Model.Link>()

        var linkList: List<Model.Link>

        // Set up progress before call
        progressDialog = ProgressDialog(this@LinkActivity)
        progressDialog!!.max = 100
        progressDialog!!.setMessage("Загрузка ....")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCanceledOnTouchOutside(false)
        // show it
        progressDialog!!.show()

        disposable = serverApi.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            progressDialog!!.dismiss()

                            linkList = result.links

                            recyclerViewLinks.adapter = RecyclerAdapter(this, linkList, object : RecyclerAdapter.OnItemClickListener{
                                override fun onItemClick(item: Model.Link) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.linkurl))
                                    startActivity(intent)
                                }

                            })




                        },
                        { error ->
                            progressDialog!!.dismiss()
                            Toast.makeText(this@LinkActivity, "Проверьте интернет соединение !!!", Toast.LENGTH_SHORT).show()
                        }
                )


    }


}
