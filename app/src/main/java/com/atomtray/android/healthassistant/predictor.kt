package com.atomtray.android.healthassistant

import org.tensorflow.lite.Interpreter
import java.util.*

class predictor {

    private var interpreter: Interpreter? = null
    private val labels = Vector<String>()

    fun getPrediction(data:Array<String>){
        //TODO get the data and predict the output and return the value.
    }

}