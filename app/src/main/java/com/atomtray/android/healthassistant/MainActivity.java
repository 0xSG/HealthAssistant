package com.atomtray.android.healthassistant;

import android.animation.ValueAnimator;
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
import android.support.v4.util.Pair;

import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;

import android.view.View;
import android.widget.Toast;
import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import org.tensorflow.lite.Interpreter;

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
            Arrays.asList("abdominal pain", "anxiety", "arm pain", "back pain", "running nose",
                    "blurred vision", "bone loss", "burning sensation", "cavity",
                    "coughing", "depression", "discomfort", "dry cough", "ear pain",
                    "fast breathing", "feeling cold", "fever", "foot pain",
                    "hand pain", "headache", "insomnia", "knee pain", "leg pain",
                    "chest pain", "red rashes", "diabetes", "rib pain", "sadness"));
    private ChipCloud cp;
    public static List<Float> selectedList;
    private ValueAnimator animator1;

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


        animator1 = ValueAnimator.ofFloat(0f, 1000f);
        animator1.setDuration(2000);
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {

                if(Float.parseFloat(animation.getAnimatedValue().toString()) <= 500f) {
                float value = Float.parseFloat(animation.getAnimatedValue().toString());

                ana.setScaleX(1+ value/1500);
                ana.setScaleY(1+ value/1500);

            }else{
                float value =1000f- Float.parseFloat(animation.getAnimatedValue().toString());
                    ana.setScaleX(1+ value/1500);
                    ana.setScaleY(1+ value/1500);


            }
            }
        });
        /*.addUpdateListener(); {
            animation ->
            if(animation.animatedValue as Float <= 500f) {
                var value = animation.animatedValue as Float

                nextBtn?.scaleX =1+ value/1000
                nextBtn?.scaleY =1+ value/1000
            }else{
                var value =1000f- animation.animatedValue as Float
                nextBtn?.scaleX =1+ value/1000
                nextBtn?.scaleY =1+ value/1000
            }

        }*/
        animator1.start();


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
                    animator1.cancel();
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
                    Pair<View, String> p1 = Pair.create((View)ana, "analysebtn");
                    Pair<View, String> p2 = Pair.create(findViewById(R.id.retryHolder), "retry");
                    Pair<View, String> p3 = Pair.create(findViewById(R.id.imageView), "actionBar");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this,
                                   p1,p2,p3);

                startActivity(i,options.toBundle());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please select some symptoms.",Toast.LENGTH_LONG).show();

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        animator1.start();
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
