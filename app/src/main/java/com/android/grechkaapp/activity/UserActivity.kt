package com.android.grechkaapp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.grechkaapp.Method
import com.android.grechkaapp.R
import com.android.grechkaapp.adapter.GridAdapter
import com.android.grechkaapp.api.ServerApi
import com.android.grechkaapp.item.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.content_user.*

class UserActivity : AppCompatActivity() {

    internal var progressDialog: ProgressDialog? = null

    private val ITEM_ABOUT = 0
    private val ITEM_SONGS = 1
    private val ITEM_PHOTO = 2
    private val ITEM_LINKS = 3

    private var disposable: Disposable? = null

    private val serverApi by lazy {
        ServerApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Method(this).fabMethod()
        }

//        ---------------------

        var listButmenu: MutableList<Model.Butmenu>

        // Set up progress before call
        progressDialog = ProgressDialog(this@UserActivity)
        progressDialog!!.setMax(100)
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

                            listButmenu = result.butmenu as MutableList<Model.Butmenu>

                            val adapter = GridAdapter(this@UserActivity, 1, listButmenu)

                            gridView.adapter = adapter

                            gridView.setOnItemClickListener { adapterView, view, position, id ->

                                when(position){
                                    ITEM_ABOUT -> {
                                        val intent = Intent(this@UserActivity, AboutActivity::class.java)
                                        intent.putExtra("imgAbout", result.picabout)
                                        intent.putExtra("textAbout", result.textabout)
                                        startActivity(intent)
                                    }
                                    ITEM_SONGS -> {
                                        val intent = Intent(this@UserActivity, SongsActivity::class.java)
                                        startActivity(intent)
                                    }
                                    ITEM_PHOTO -> {
                                        val intent = Intent(this@UserActivity, PhotoActivity::class.java)
                                        startActivity(intent)
                                    }
                                    ITEM_LINKS -> {
                                        val intent = Intent(this@UserActivity, LinkActivity::class.java)
                                        startActivity(intent)
                                    }
                                }



                            }

                        },
                        { error ->
                            progressDialog!!.dismiss()
                            Toast.makeText(this@UserActivity, "Проверьте интернет соединение !!!", Toast.LENGTH_SHORT).show()
                        }
                )
























    }

    //    Функция проверки почты
    fun isValidEmailAddress(email: String): Boolean {
        val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = java.util.regex.Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

}
