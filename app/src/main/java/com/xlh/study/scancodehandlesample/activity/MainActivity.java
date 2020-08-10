package com.xlh.study.scancodehandlesample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xlh.study.scancodehandlesample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpOriginal(View view) {
        startActivity(new Intent(this, OriginalActivity.class));
    }

    public void jumpChainFactoryCache(View view) {
        startActivity(new Intent(this, ChainFactoryCacheActivity.class));
    }
}
