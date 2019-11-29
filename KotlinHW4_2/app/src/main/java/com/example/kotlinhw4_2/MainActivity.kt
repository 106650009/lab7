package com.example.kotlinhw4_2

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var rabprogress = 0
    private var turprogress = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener {
            btn_start.isEnabled = false
            rabprogress = 0
            turprogress = 0
            seekBar.progress = 0
            seekBar2.progress = 0

            runThread()
            runAsyncTask()
        }
    }

    private fun runThread() {
        Thread(
            Runnable {
                while (rabprogress <= 100 && turprogress <= 100) {
                    try {
                        Thread.sleep(100)
                        rabprogress += (Math.random() * 3).toInt()

                        val msg = Message()
                        msg.what = 1
                        mHandler.sendMessage(msg)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }

        ).start()
    }

    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            1 -> seekBar.progress = rabprogress
        }
        if (rabprogress >= 100 && turprogress <= 100) {
            Toast.makeText(this, "兔子勝利", Toast.LENGTH_SHORT).show()
            btn_start.isEnabled = true
        }
        false
    })

    private fun runAsyncTask(){
        object : AsyncTask<Void, Int, Boolean>() {
            override fun doInBackground(vararg voids: Void): Boolean {
                while (turprogress <= 100 && rabprogress < 100) {
                    try {
                        Thread.sleep(100)

                        turprogress += (Math.random() * 3).toInt()
                        publishProgress(turprogress)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                return true
            }

            protected override fun onProgressUpdate( values: Array<Int>) {
                super.onProgressUpdate(*values)
                seekBar2.progress = values[0]
            }

            override fun onPostExecute(aBoolean: Boolean?) {
                super.onPostExecute(aBoolean)
                if (turprogress >= 100 && rabprogress < 100) {
                    Toast.makeText(
                        this@MainActivity,
                        "烏龜勝利", Toast.LENGTH_SHORT
                    ).show()
                    btn_start.isEnabled = true
                }
            }
        }.execute()
    }
}