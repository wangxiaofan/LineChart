package com.yitong.wmy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Float[] floatlist = new Float[]{2.3000f, 2.3500f, 2.7400f, 3.2900f,
            3.2000f, 3.4000f, 3.6000f};

    String[] Strlist = new String[]{"11-22", "11-23", "11-24", "11-25",
            "11-26", "11-27", "11-28"};

    private LineChartView lcv1, lcv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lcv1 = (LineChartView) findViewById(R.id.linechart_one);
        lcv2 = (LineChartView) findViewById(R.id.linechart_two);

        lcv1.setData(MainActivity.this, Strlist, floatlist, LineChartView.BLUE);
        lcv2.setData(MainActivity.this, Strlist, floatlist, LineChartView.YELLOW);
    }
}
