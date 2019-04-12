package com.atomtray.android.healthassistant;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.TensorFlowLite;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected Interpreter tflite;

    public static final List<String> INPUT_LABELS = Collections.unmodifiableList(
            Arrays.asList("abdominal pain", "anxiety", "arm pain", "back pain", "bleeding",
                    "blurred vision", "bone loss", "burning sensation", "cavity",
                    "coughing", "depression", "discomfort", "dry cough", "ear pain",
                    "fast breathing", "feeling cold", "fever", "foot pain",
                    "hand pain", "headache", "insomnia", "knee pain", "leg pain",
                    "pain", "red rashes", "redness", "rib pain", "sadness"));
    private ChipCloud cp;
    public static List<Float> selectedList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);


// set an exit transition
        getWindow().setExitTransition(new Explode());
        //initialising selected List
        selectedList = new ArrayList<>();
        for(int k=0;k<INPUT_LABELS.size();k++)
            selectedList.add(0f);

        //Cloud Chip initialization and data adding
        cp = findViewById(R.id.chip_cloud);
        cp.setElevation(10f);
        for(String sym:INPUT_LABELS)
            cp.addChip(sym);


        cp.setMode(ChipCloud.Mode.MULTI);
        cp.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                //Toast.makeText(getApplicationContext(),INPUT_LABELS.get(index),Toast.LENGTH_LONG).show();
                selectedList.set(index,1.0f);
            }
            @Override
            public void chipDeselected(int index) {
                selectedList.set(index,0.0f);
                //...
            }
        });



        final FloatingActionButton ana = findViewById(R.id.analysisBtn);
        ana.setElevation(10f);

        ana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean selectedBool = false;
                for(Object k : selectedList.toArray()){
                    if(k.toString().contains("1.0")){
                        selectedBool=true;
                        break;
                    }
                }

                if(selectedBool)
                try {

                    //get array from cloudChips

                    //Convert into array.
                    //pass into the model.

                    //TF lite initialization.
                    tflite = new Interpreter((MappedByteBuffer) loadModelFile(getAssets(),"model.tflite"));

//            Object input = Collections.unmodifiableList(
//                    Arrays.asList("1", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));
                    float[][] output = new float[][]{{0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
                    float[][] input = new float[1][selectedList.size()];
                    for(int k=0;k<selectedList.size();k++){
                        input[0][k]=selectedList.get(k);
                    }

            //Log.d("sgk", "Before------");
                    /*for(Object i:selectedList.toArray())
                        Log.d("sgk", String.valueOf(i)+":"+input[0].length);
                    Toast.makeText(getApplicationContext(),"Analysing..",Toast.LENGTH_LONG).show();*/
                    tflite.run(input,output);
/*

            Log.d("sgk", "After------");
            for(float i:output[0])
            Log.d("sgk", String.valueOf(i));
                    int maxAt = 0;

                    for (int i = 0; i < output[0].length; i++) {
                        maxAt = output[0][i] > output[0][maxAt] ? i : maxAt;
                    }

                    Toast.makeText(getApplicationContext(),OUTPUT_LABELS.get(maxAt),Toast.LENGTH_LONG).show();
*/

                Intent i = new Intent(MainActivity.this, ResultScreen.class);
                i.putExtra("output",output[0]);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this,
                                    ana,
                                    ViewCompat.getTransitionName(ana));

                startActivity(i,options.toBundle());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please select some symptoms.",Toast.LENGTH_LONG).show();

            }
        });



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
