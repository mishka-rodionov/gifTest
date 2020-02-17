package com.rodionov.gifapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivItemGifList.setOnClickListener {
            getRandom()
        }
//        getRandom()
    }

    override fun onResume() {
        super.onResume()
        getRandom()
        Log.d("testGif", "url = $url")
    }

    private fun getRandom() {
        buildNewRetrofit().create(ApiService::class.java).getRandom(Settings.API_KEY).subscribeOn(
            Schedulers.newThread()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Log.d("testGif", "url = $url")
                url = it.gifObjectModel.toGifItemModel().stillUrl ?: ""
                val layoutParams = ivItemGifList.layoutParams
                layoutParams.width =
                    UIUtils.getScreenWidthInPx(this) - UIUtils.convertDpToPixel(8F).toInt()
                val scaleFactor: Float =
                    (UIUtils.getScreenWidthInPx(this) - UIUtils.convertDpToPixel(8F).toInt()).toFloat() / Settings.FIXED_WIDTH
                layoutParams.height =
                    (it.gifObjectModel.toGifItemModel().height.toFloat() * scaleFactor).toInt()
                ivItemGifList.layoutParams = layoutParams
            }.subscribeBy(
                onNext = {
                    Log.d("testGif", "url = $url")
                    GlideApp.with(this)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivItemGifList)
                }, onError = {
                    Log.d("testGif", "onError url = $url")
                }
            )
    }

    fun buildNewRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.giphy.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit

    }
}
