package com.android.grechkaapp

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.grechkaapp.api.ServerApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Lenovo on 30.03.2018.
 */
class Method(var mCtx: Context) {

    private var urlMsg = ""

    private var disposable: Disposable? = null

    private val serverApi by lazy {
        ServerApi.create()
    }

    fun fabMethod(){

        val mBuilder = AlertDialog.Builder(mCtx)
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val mView = layoutInflater.inflate(R.layout.dialog_feedback, null)
        mBuilder.setView(mView)

        val mName = mView.findViewById<View>(R.id.edDialogName) as EditText
        val mEmail = mView.findViewById<View>(R.id.edDialogEmail) as EditText
        val mMessage = mView.findViewById<View>(R.id.edDialogMessage) as EditText
        val mBtnSent = mView.findViewById<View>(R.id.btnDialogSent) as Button

        val dialog = mBuilder.create()
        dialog.show()

//        App Name
        val context : Context? = mCtx
        val res = context?.resources
        val appName = res?.getString(R.string.app_name)

        mBtnSent.setOnClickListener {

            val name: String
            val email: String
            var msg: String

            if (mName.text.toString().isEmpty() || mEmail.text.toString().isEmpty() || mMessage.text.toString().isEmpty()) run {
                Toast.makeText(mCtx, "Заполните все поля !!!", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmailAddress(mEmail.text.toString())) run {
                Toast.makeText(mCtx, "Введите пожалуйста корректный адрес электронной почты !!!", Toast.LENGTH_SHORT).show()
            } else {

                name = mName.text.toString().replace("&", "%20")

                msg = mMessage.text.toString()
                msg = msg.replace("&", "%20")
                msg = msg.replace(" ", "%20")

                email = mEmail.text.toString()

                urlMsg = "sendgridmail.php?app=$appName&from=$email&msg=$msg"

                disposable = serverApi.sentMsg(urlMsg)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result ->
                                    Toast.makeText(mCtx, "Ваше сообщение было отправлено!!! Спасибо !!!", Toast.LENGTH_LONG).show()
                                    dialog.dismiss()
                                },
                                { error -> Toast.makeText(mCtx, "Проверьте интернет соединение !!!", Toast.LENGTH_SHORT).show() }
                        )
            }
        }

    }



    //    Функция проверки почты
    fun isValidEmailAddress(email: String): Boolean {
        val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = java.util.regex.Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

}