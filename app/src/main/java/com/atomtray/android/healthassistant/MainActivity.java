package com.atomtray.android.healthassistant;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.adroitandroid.chipcloud.ChipCloud;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.TensorFlowLite;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected Interpreter tflite;
    public static final List<String> OUTPUT_LABELS = Collections.unmodifiableList(
            Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        //Cloud Chip initialization and data adding
        ChipCloud cp = findViewById(R.id.chip_cloud);
        cp.addChip("Do you have headache? ");
        cp.addChip("Is your eyes paining? ");
        cp.addChip("Do you have any stomachache?");
        cp.addChip("Do you feel uncomfort?");
        cp.setMode(ChipCloud.Mode.MULTI);

        //TF lite initialization.


        try {
            tflite = new Interpreter((MappedByteBuffer) loadModelFile(getAssets(),"model.tflite"));

//            Object input = Collections.unmodifiableList(
//                    Arrays.asList("1", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));
            float[][] output = new float[][]{{0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
            float[][] input = {{0, 1, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 0,1}};
            tflite.run(input,output);
            Toast.makeText(this,"dd",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }



}
