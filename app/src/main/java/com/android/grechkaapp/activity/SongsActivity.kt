package com.android.grechkaapp.activity

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.android.grechkaapp.R
import com.android.grechkaapp.adapter.ListAdapter
import com.android.grechkaapp.api.ServerApi
import com.android.grechkaapp.item.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_songs.*
import java.util.ArrayList

class SongsActivity : AppCompatActivity() {

    lateinit var musicList: List<Model.Music>  // Список с музыкой

    lateinit var listAdapter: ListAdapter

    var handler: Handler? = null
    lateinit var runnable: Runnable

    var mediaPlayer: MediaPlayer? = null

    internal var pos: String? = null  // Держит позицию для кнопки Play
    internal var b = false  // Состояние кнопки Play (вкл./выкл.)

    internal var state: Parcelable? = null  // Держит состояние списка (при обновлении адаптера список не скролится вверх)

    internal var p: Int = 0 // Держит позицию для кнопок Next и Previous
    internal var listSize = 0 // Размерность списка песен



    private var disposable: Disposable? = null

    private val serverApi by lazy {
        ServerApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)
        setSupportActionBar(toolbarSongs)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbarSongs.setNavigationOnClickListener { finish() }

        handler = Handler()

        mediaPlayer = MediaPlayer()

        musicList = ArrayList<Model.Music>()

        disposable = serverApi.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                            musicList = result.music

                            listSize = musicList.size

                            state = lvMusic.onSaveInstanceState() //  Запоминает позицию до обновления адаптера

                            listAdapter = ListAdapter(this@SongsActivity, musicList, null)

                            lvMusic.adapter = listAdapter

                            lvMusic.onRestoreInstanceState(state)  //  Восстанавливает позицию позицию после обновления адаптера

                            lvMusic.setOnItemClickListener { adapterView, view, position, id ->

                                p = position

                                playButtonClick(position)

                            }

//                             ----------------------------- Panel -----------------------------------------------------

                            btnPanelPlay.setOnClickListener {
                                playButtonClick(p)
                            }

                            btnPanelNext.setOnClickListener {
                                playButtonNext(p)
                            }

                            btnPanelPrevious.setOnClickListener {
                                playButtonPrevious(p)
                            }

//                            ----------------------------------------------------------------------------------------

                        },
                        { error -> Toast.makeText(this@SongsActivity, "Проверьте интернет соединение !!!", Toast.LENGTH_SHORT).show() }
                )



    }


    fun playCycle() {
        try {
//            Log.d("myLogs", "--- mediaPlayer.getCurrentPosition() ---  =  " + mediaPlayer.getCurrentPosition() + " mediaPlayer.getDuration() = " + mediaPlayer.getDuration());
            seekBarMusic.progress = mediaPlayer!!.currentPosition
//            Если песня идет до конца, включается следующаяя
            if (mediaPlayer!!.isPlaying && mediaPlayer!!.currentPosition >= mediaPlayer!!.duration - 500) {
                playButtonNext(p)
            }
        } catch (e: java.lang.IllegalStateException) {
            e.printStackTrace()
        }

        if (mediaPlayer!!.isPlaying) {
            runnable = Runnable { playCycle() }
            handler!!.postDelayed(runnable, 200)  // Частота проверки состояния времени проигранной песни
        }
    }

    private fun playButtonClick(position: Int){

//        (pos = nul при первом вхождении приложения)
        b = if (pos != null && pos!!.toInt() == position) {
            !b
        } else {
            false
        }

        linMusic.visibility = View.VISIBLE
        tvNameOfSong.text = musicList.get(position).name

        if (b){ // Ставим на паузу
            Log.d("myLogs", "P1")
            btnPanelPlay.setImageResource(R.mipmap.ic_panel_play)

            state = lvMusic.onSaveInstanceState() //  Восстанавливает позицию позицию после обновления адаптера

            listAdapter = ListAdapter(this@SongsActivity, musicList, null)
            lvMusic.adapter = listAdapter

            lvMusic.onRestoreInstanceState(state) //  Запоминает позицию до обновления адаптера

            mediaPlayer?.pause()

        } else {

            if (pos != null && pos!!.toInt() == position){ // Возобновление
                Log.d("myLogs", "P2")
                btnPanelPlay.setImageResource(R.mipmap.ic_panel_stop)

                state = lvMusic.onSaveInstanceState() //  Восстанавливает позицию позицию после обновления адаптера

                listAdapter = ListAdapter(this@SongsActivity, musicList, position.toString())
                lvMusic.adapter = listAdapter

                lvMusic.onRestoreInstanceState(state) //  Запоминает позицию до обновления адаптера

                mediaPlayer?.start()
                playCycle()

            } else { // Переключение на новое аудио
                Log.d("myLogs", "P3")
                btnPanelPlay.setImageResource(R.mipmap.ic_panel_stop)

                setTitleAndChangeSong(position)
            }
        }

        pos = position.toString()

    }

    private fun playButtonNext(position: Int){
        btnPanelPlay.setImageResource(R.mipmap.ic_panel_stop)

        p = position + 1
        pos = (position + 1).toString()

        if (p == listSize) {
            p = 0
            playButtonClick(p)
            return
        }

        setTitleAndChangeSong(p)
    }

    private fun playButtonPrevious(position: Int){
        btnPanelPlay.setImageResource(R.mipmap.ic_panel_stop)

        p = position - 1
        pos = (position - 1).toString()

        if (p == -1) {
            p = listSize - 1
            playButtonClick(p)
            return
        }

        setTitleAndChangeSong(p)
    }

    private fun setTitleAndChangeSong(p: Int){
        tvNameOfSong.text = musicList.get(p).name

        state = lvMusic.onSaveInstanceState() //  Восстанавливает позицию позицию после обновления адаптера

        listAdapter = ListAdapter(this@SongsActivity, musicList, p.toString())
        lvMusic.adapter = listAdapter

        lvMusic.onRestoreInstanceState(state) //  Запоминает позицию до обновления адаптера

//          Перед включением новой песни страрую выключаем
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }

        //        Создаем объект плеера
        mediaPlayer = MediaPlayer.create(this@SongsActivity, Uri.parse(musicList[p].song))
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)


        seekBarMusic.max = mediaPlayer!!.getDuration()
        mediaPlayer!!.start()
        playCycle()

//        Прокрутить
        seekBarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, input: Boolean) {
                if (input) {
                    mediaPlayer!!.seekTo(progress)
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        mediaPlayer!!.start()
        playCycle()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer!!.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release() // Освободить память
        try {
            handler!!.removeCallbacks(runnable)
        } catch (e: java.lang.RuntimeException){
            e.printStackTrace()
        }
//        handler!!.removeCallbacks(runnable)
    }

}
