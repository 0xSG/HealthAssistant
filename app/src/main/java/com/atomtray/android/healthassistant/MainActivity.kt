package com.atomtray.android.healthassistant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.adroitandroid.chipcloud.ChipCloud
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chip_cloud.addChip("Do you have headache? ")
        chip_cloud.addChip("Is your eyes paining? ")
        chip_cloud.addChip("Do you have any stomachache?")
        chip_cloud.addChip("Do you feel uncomfort?")
        chip_cloud.setMode(ChipCloud.Mode.MULTI)



    }
}
