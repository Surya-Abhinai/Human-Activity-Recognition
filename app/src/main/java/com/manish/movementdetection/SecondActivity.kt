package com.manish.movementdetection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.math.pow
import kotlin.math.sqrt

class SecondActivity : AppCompatActivity() {

    private lateinit var predictButton: Button
    private lateinit var predictedTV : TextView
    private lateinit var interpreter: Interpreter

    private var list = arrayListOf<Float>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        predictButton = findViewById(R.id.predictButton)
        predictedTV = findViewById(R.id.predictedTV)


        val sensorDataList = intent.getSerializableExtra("sensorDataList") as? List<Value>

        if (!sensorDataList.isNullOrEmpty()) {
            val (meanList, stdDevList) = calculateMeanAndStdDev(sensorDataList)

            for (i in 0 until 3) {
                list.add(meanList[i])
            }
            for (i in 0 until 3) {
                list.add(stdDevList[i])
            }
            for (i in 3 until 6) {
                list.add(meanList[i])
            }
            for (i in 3 until 6) {
                list.add(stdDevList[i])
            }


            // Display the mean and standard deviation for each column
            val resultTextView: TextView = findViewById(R.id.text)
//            resultTextView.text = buildString {
//                for (i in 0 until 6) {
//                    append("Column $i:\n")
//                    append("Mean: ${meanList[i]}\n")
//                    append("Standard Deviation: ${stdDevList[i]}\n\n")
//                }
//            }
            resultTextView.text =
                "accX:\nMean: ${meanList[0]}\nStandard Deviation: ${stdDevList[0]}" +
                    "\n\naccY:\n" +
                    "Mean: ${meanList[1]}\n" +
                    "Standard Deviation: ${stdDevList[1]}"+"\n\naccZ:\n" +
                        "Mean: ${meanList[2]}\n" +
                        "Standard Deviation: ${stdDevList[2]}"+"\n\ngyroX:\n" +
                        "Mean: ${meanList[3]}\n" +
                        "Standard Deviation: ${stdDevList[3]}"+"\n\ngyroY:\n" +
                    "Mean: ${meanList[4]}\n" +
                    "Standard Deviation: ${stdDevList[4]}"+"\n\ngyroZ:\n" +
                        "Mean: ${meanList[5]}\n" +
                        "Standard Deviation: ${stdDevList[5]}"
        }

//        predictButton.setOnClickListener {
//            val model = CompressedModel.newInstance(this@SecondActivity)
//
//            // Prepare input data
//            val inputData = list.toFloatArray()
//
//            // Create a ByteBuffer for input data
//            val inputBuffer = ByteBuffer.allocateDirect(inputData.size * java.lang.Float.SIZE / java.lang.Byte.SIZE)
//                .order(ByteOrder.nativeOrder())
//            for (input in inputData) {
//                inputBuffer.putFloat(input)
//            }
//
//
//            // Create a TensorBuffer for input
//            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1, 12), DataType.FLOAT32)
//            inputFeature0.loadBuffer(inputBuffer)
//
//            // Run inference and get result
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//            // Process the outputFeature0 tensor buffer to get the prediction
//            val prediction = outputFeature0.floatArray
//
//            // Example: Find the index of the maximum value in the prediction array
//            var maxIndex = 0
//            for (i in 1 until prediction.size) {
//                if (prediction[i] > prediction[maxIndex]) {
//                    maxIndex = i
//                }
//            }
//
//            // Display the predicted output on the screen
//            predictedTV.text = "Predicted Output: $maxIndex"
//
//            // Close the model to release resources
//            model.close()
//        }

        predictButton.setOnClickListener {
            interpreter = Interpreter(loadModelFile())

            // Prepare input data
            val inputBuffer = getInputByteBuffer(list)

            // Define the output buffer
            val outputBuffer = ByteBuffer.allocateDirect(24) // Assuming single float output

            // Run inference
            interpreter.run(inputBuffer, outputBuffer)

            // Process the output
            outputBuffer.rewind() // Rewind buffer to read from the beginning
            val outputArray = FloatArray(6) // Assuming 6 output values
            outputBuffer.asFloatBuffer().get(outputArray) // Read the output values into an array

            // Find the index with the maximum value
            var maxIndex = 0
            for (i in 1 until outputArray.size) {
                if (outputArray[i] > outputArray[maxIndex]) {
                    maxIndex = i
                }
            }

            // Map the maxIndex to the corresponding activity
            val activity = when (maxIndex) {
                0 -> "STANDING"
                1 -> "SITTING"
                2 -> "LAYING"
                3 -> "WALKING"
                4 -> "WALKING_DOWNSTAIRS"
                else -> "WALKING_UPSTAIRS"
            }

            // Display the predicted activity
            predictedTV.text = "Predicted Activity: $activity"
        }

    }
    private fun loadModelFile(): ByteBuffer {
        val fileDescriptor = assets.openFd("compressed_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    private fun getInputByteBuffer(inputList: ArrayList<Float>): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(inputList.size * 4) // Assuming float size is 4 bytes
        byteBuffer.order(ByteOrder.nativeOrder())
        inputList.forEach { byteBuffer.putFloat(it) }
        byteBuffer.rewind() // Rewind the buffer to its initial position
        return byteBuffer
    }
    private fun calculateMeanAndStdDev(sensorDataList: List<Value>): Pair<List<Float>, List<Float>> {
        val columnSums = MutableList(6) { 0f }
        val columnSquares = MutableList(6) { 0f }

        // Calculate column sums
        for (value in sensorDataList) {
            columnSums[0] += value.accX.toFloat()
            columnSums[1] += value.accY.toFloat()
            columnSums[2] += value.accZ.toFloat()
            columnSums[3] += value.gyroX.toFloat()
            columnSums[4] += value.gyroY.toFloat()
            columnSums[5] += value.gyroZ.toFloat()
        }

        // Calculate mean
        val meanList = columnSums.map { it / sensorDataList.size }

        // Calculate column squares
        for (value in sensorDataList) {
            columnSquares[0] += (value.accX.toFloat() - meanList[0]).pow(2)
            columnSquares[1] += (value.accY.toFloat() - meanList[1]).pow(2)
            columnSquares[2] += (value.accZ.toFloat() - meanList[2]).pow(2)
            columnSquares[3] += (value.gyroX.toFloat() - meanList[3]).pow(2)
            columnSquares[4] += (value.gyroY.toFloat() - meanList[4]).pow(2)
            columnSquares[5] += (value.gyroZ.toFloat() - meanList[5]).pow(2)
        }

        // Calculate standard deviation
        val stdDevList = columnSquares.map { sqrt(it / sensorDataList.size) }

        return meanList to stdDevList
    }

}