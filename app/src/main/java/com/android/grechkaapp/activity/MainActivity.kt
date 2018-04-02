package com.android.grechkaapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.android.grechkaapp.R
import com.android.grechkaapp.api.ServerApi
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val serverApi by lazy {
        ServerApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposable = serverApi.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                            Log.d("myLogs", "splashUrl =  ${result.splash}")

                            Picasso
                                    .with(applicationContext)
                                    .load(result.splash)
                                    .into(imgHello)

                            val anim: Animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.myalpha)
                            imgHello.startAnimation(anim)

                            Handler().postDelayed({
                                val intent = Intent(this, UserActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()

                            }, 4000)


                        },
                        { error -> Toast.makeText(this@MainActivity, "Проверьте интернет соединение !!!", Toast.LENGTH_SHORT).show() }
                )


    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}
