package com.comnawa.dowhat.kwanwoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;

import com.comnawa.dowhat.R;

public class ColorChoice extends AppCompatActivity {
    RadioButton btnRed, btnBlue, btnGreen, btnYellow, btnPurple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_choice_kwanwoo);
    }
}
