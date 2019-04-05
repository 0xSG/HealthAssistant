package com.atomtray.android.healthassistant;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
            Arrays.asList("anemia", "asthma", "bone fracture", "bone infection", "common cold", "gastritis", "high blood pressure", "high cholesterol", "sprained ankle", "sprained knee", "tension headache", "type 1 diabetes", "type 2 diabetes", "vitamin d deficiency"));
    public static final List<String> INPUT_LABELS = Collections.unmodifiableList(
            Arrays.asList("abdominal pain", "anxiety", "arm pain", "back pain", "bleeding",
                    "blurred vision", "bone loss", "burning sensation", "cavity",
                    "coughing", "depression", "discomfort", "dry cough", "ear pain",
                    "fast breathing", "feeling cold", "fever", "foot pain",
                    "hand pain", "headache", "insomnia", "knee pain", "leg pain",
                    "pain", "red rashes", "redness", "rib pain", "sadness"));

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        //Cloud Chip initialization and data adding
        ChipCloud cp = findViewById(R.id.chip_cloud);
        for(String sym:INPUT_LABELS)
            cp.addChip(sym);
        

        cp.setMode(ChipCloud.Mode.MULTI);

        //TF lite initialization.


        try {
            tflite = new Interpreter((MappedByteBuffer) loadModelFile(getAssets(),"model.tflite"));

//            Object input = Collections.unmodifiableList(
//                    Arrays.asList("1", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));
            float[][] output = new float[][]{{0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
            float[][] input = {{0, 1, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 0,1}};

            /*Log.d("sgk", "Before------");
            for(float i:output[0])
                Log.d("sgk", String.valueOf(i));*/
            tflite.run(input,output);

            /*Log.d("sgk", "After------");
            for(float i:output[0])
            Log.d("sgk", String.valueOf(i));*/

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
