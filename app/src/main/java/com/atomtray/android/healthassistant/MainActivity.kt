package com.atomtray.android.healthassistant

import android.content.res.AssetManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.adroitandroid.chipcloud.ChipCloud
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var interpreter: Interpreter? = null
    var assetManager: AssetManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chip_cloud.addChip("Do you have headache? ")
        chip_cloud.addChip("Is your eyes paining? ")
        chip_cloud.addChip("Do you have any stomachache?")
        chip_cloud.addChip("Do you feel uncomfort?")
        chip_cloud.setMode(ChipCloud.Mode.MULTI)

        assetManager = applicationContext.getAssets()

        thread {
            interpreter = Interpreter(loadModelFile(assetManager!!, "model.tflite"))
            var input:Any = [1,0,0,0,0,0,1].toAny()
            var out:Any =null
            interpreter?.run(input,out)
        }
    }
    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
