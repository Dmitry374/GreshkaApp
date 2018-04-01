package com.android.grechkaapp.api

import com.android.grechkaapp.item.Model
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Lenovo on 28.03.2018.
 */
interface ServerApi {

//    @GET("json.json")
    @GET("HCd7xd")
//    fun loadData(): Observable<ItemServerData>
    fun loadData(): Observable<Model.ItemServerData>
    fun sentMsg(@Url url: String): Observable<Model.ItemServerMsg>

    companion object {
        fun create(): ServerApi {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl("http://dimon.ru/")
                    .baseUrl("http://keit126.ru/")
                    .build()

            return retrofit.create(ServerApi::class.java)
        }
    }

}