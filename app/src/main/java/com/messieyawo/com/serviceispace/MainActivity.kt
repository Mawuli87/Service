package com.messieyawo.com.serviceispace

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.ScrollView
import com.messieyawo.com.serviceispace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    private lateinit var myService: MyService

    //connect our service to the activity

    private val connection = object: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(LOG_TAG, "Connecting service")
            val binder = service as MyService.MyServiceBinder
            myService = binder.getService()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize button click handlers
        with(binding) {
            runButton.setOnClickListener { runCode() }
            clearButton.setOnClickListener { clearOutput() }
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also { service ->
            bindService(service, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    /**
     * Run some code
     */
    private fun runCode() {
        myService.doSomething()
    }

    /**
     * Clear log display
     */
    private fun clearOutput() {
        binding.logDisplay.text = ""
        scrollTextToEnd()
    }

    /**
     * Log output to logcat and the screen
     */
    @Suppress("SameParameterValue")
    private fun log(message: String) {
        Log.i(LOG_TAG, message)
        binding.logDisplay.append(message + "\n")
        scrollTextToEnd()
    }

    /**
     * Scroll to end. Wrapped in post() function so it's the last thing to happen
     */
    private fun scrollTextToEnd() {
        Handler().post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }


}