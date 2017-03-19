package com.whf.indicatorfromios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private IOSIndicator iosIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iosIndicator = (IOSIndicator) findViewById(R.id.ios);

        ArrayList<String> list = new ArrayList<>();
        list.add("新浪");
        list.add("网易");
        list.add("腾讯7777");

        iosIndicator.setItemTitles(list);
    }
}
