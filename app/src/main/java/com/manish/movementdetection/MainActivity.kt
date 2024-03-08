package com.manish.movementdetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import java.io.Serializable
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var myAcc : Sensor
    private lateinit var myGyro : Sensor
    private lateinit var startButton: Button
    private lateinit var goToNewActivity: Button
    private lateinit var canProceed : TextView

    private val sensorDataList = mutableListOf<Value>()

    private lateinit var accelerometerTextView: TextView
    private lateinit var gyroscopeTextView: TextView

    private val handler = Handler()
    private var isCollectingData = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accelerometerTextView = findViewById(R.id.accelerometerTextView)
        gyroscopeTextView = findViewById(R.id.gyroscopeTextView)
        startButton = findViewById(R.id.collectButton)
        goToNewActivity = findViewById(R.id.Go)
        canProceed = findViewById(R.id.can_proceed)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        myAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        myGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        startButton.setOnClickListener {
            startDataCollection()
        }

        goToNewActivity.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("sensorDataList", sensorDataList as Serializable)
            startActivity(intent)
        }

    }

    private fun startDataCollection() {
        startButton.text = "Wait for 10 seconds"
        sensorDataList.clear()
        isCollectingData = true
        handler.postDelayed({
            isCollectingData = false
            showData()
        }, 10000)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, myAcc, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, myGyro, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val df = DecimalFormat("#.####")

            val timestamp = System.currentTimeMillis()
            val accX = event.values[0]
            val accY = event.values[1]
            val accZ = event.values[2]

            val gyroX: Float
            val gyroY: Float
            val gyroZ: Float

            // Extract gyro values if it is a gyroscope sensor event
            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                gyroX = event.values[0]
                gyroY = event.values[1]
                gyroZ = event.values[2]
            } else {
                gyroX = 0f
                gyroY = 0f
                gyroZ = 0f
            }

            // Update TextViews based on sensor type
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accelerometerTextView.text = "ax: ${df.format(accX)};\nay: ${df.format(accY)}; \naz: ${df.format(accZ)};\n\n"
            } else if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                gyroscopeTextView.text = "omegaX: ${df.format(gyroX)};\nomegaY: ${df.format(gyroY)}; \nomegaZ: ${df.format(accZ)};\n\n"
            }

            if (isCollectingData) {
                val value = Value(System.currentTimeMillis(),
                    df.format(accX), df.format(accY), df.format(accZ), df.format(gyroX), df.format(gyroY), df.format(gyroZ))
                sensorDataList.add(value)
            }

        }
    }

    private fun showData() {
        startButton.text = "Start Data Collection"
        canProceed.text = "Now proceed to Calculate values"
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        canProceed.text = ""
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        handler.removeCallbacksAndMessages(null) // Remove any pending delayed tasks
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}