package com.atomtray.android.healthassistant;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultScreen extends AppCompatActivity {
    public static final List<String> OUTPUT_LABELS = Collections.unmodifiableList(
            Arrays.asList("anemia", "asthma", "bone fracture", "bone infection", "common cold", "gastritis", "high blood pressure", "high cholesterol", "sprained ankle", "sprained knee", "tension headache", "type 1 diabetes", "type 2 diabetes", "vitamin d deficiency"));
    public static final List<String> OUTPUT_REME = Collections.unmodifiableList(Arrays.asList(" Make healthy food choices. "," Consume iron content food nd juices "," avoid food or beverages that block iron absorption "," yoga "," breathing exercises "," consume black seed, caffeine, choline and pycnogenol. "," keep your surrounding clean "," Boost your mineral intake "," take sufficient calcium dose "," Enhance vitamin intake "," do not smoke "," have a well balanced diet "," Exercise regularly  "," go for regular checkups "," Stay hydrated. Sip warm liquids. "," Rest.  "," Breathing in steam "," A humidifier or cool mist vaporizer "," wash hands regularly "," avoid dairy products,cauliflowers,cabbage,fried foods,etc "," drink ginger tea "," eat slowly and do not eat chewing gum "," exercise regularly. "," Reduce your sodium intake. "," Cut back on caffeine "," Drink less alcohol "," Focus on Monounsaturated Fats(Olives,olive oil,almonds, walnuts,etc) "," Eat Soluble Fiber(beans, peas, lentils, fruit, psyllium and oats) "," exercise regularly ","  To reduce swelling, elevate your ankle above waist level "," Avoid activities that cause pain "," consult doctors for exercises "," drink water and take adequate sleep "," Avoid Foods High in Histamine(cheeses, fermented food,wine,etc) "," Drink Caffeinated Tea or Coffee "," Relax with Yoga nd try acupunture "," Taking insulin as directed by doctors "," Frequent blood sugar monitoring. "," Exercising regularly "," intake of juice pulp of aloe vera "," Milk thistle is an herb that can  be used  "," ginger,cinnamon,fenugreek,etc can help . "," intake of bitter melon and its seeds is very useful "," Take a vitamin D supplement "," Eat foods rich in vitamin D(Beef liver, cheese, milk and egg yolks  "," Increase exposure to natural sunlight "));
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen_activity);



        findViewById(R.id.finishBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultScreen.super.onBackPressed();

            }
        });

// set an exit transition
        getWindow().setExitTransition(new Explode());
        //Card view
        CardView cardView = (CardView) findViewById(R.id.cardid);
        cardView.setRadius(100f);
        cardView.setElevation(50);



        float[] output = getIntent().getFloatArrayExtra("output");
        float th = 272.72443442208424f;
        int maxAt = 0;

        for (int i = 0; i < output.length; i++) {
            maxAt = output[i] > output[maxAt] ? i : maxAt;
        }


        TextView t = findViewById(R.id.dntext);
        t.setText(""+OUTPUT_REME .get(maxAt));
        Toast.makeText(getApplicationContext(),""+output[maxAt], Toast.LENGTH_LONG).show();


    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()){
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

    }
}
