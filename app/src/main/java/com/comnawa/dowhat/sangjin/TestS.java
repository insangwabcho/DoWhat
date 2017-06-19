package com.comnawa.dowhat.sangjin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.comnawa.dowhat.R;

import java.util.ArrayList;

public class TestS extends AppCompatActivity {
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_s);
        txtResult=(TextView)findViewById(R.id.txtResult);
        ArrayList<ScheduleDTO> list = (ArrayList<ScheduleDTO>) getIntent().getSerializableExtra("dto");
        txtResult.setText(list.indexOf(1));
    }
}
