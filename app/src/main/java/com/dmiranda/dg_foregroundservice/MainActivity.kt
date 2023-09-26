package com.dmiranda.dg_foregroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.dmiranda.dg_foregroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Handler.Callback {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btStartStop.setOnClickListener  {
            if (ForegroundService.running) {
                ForegroundService.stopService(this)
                binding.btStartStop.text = getString(R.string.message_start)
            } else {
                ForegroundService.starService(this.getString(R.string.message_2_user), Handler(this))
                binding.btStartStop.text = getString(R.string.message_stop)
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        val count = msg.data.getInt("Contador")
        binding.tvCounter.text = count.toString()
        return true
    }

}