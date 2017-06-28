package com.comnawa.dowhat.kwanwoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;

import com.comnawa.dowhat.R;

public class ColorChoice extends AppCompatActivity {
    RadioButton btnRed, btnBlue, btnGreen, btnYellow, btnGray;
    Button btnCancel;
    CalendarView calview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_choice_kwanwoo);
        setContentView(R.layout.calendar_sangjin);

        btnRed = (RadioButton)findViewById(R.id.btnRed);
        btnBlue = (RadioButton)findViewById(R.id.btnBlue);
        btnGreen = (RadioButton)findViewById(R.id.btnGreen);
        btnYellow = (RadioButton)findViewById(R.id.btnYellow);
        btnGray = (RadioButton)findViewById(R.id.btnGray);

        calview = (CalendarView)findViewById(R.id.calview);


    }


    //매뉴아이템이 선택 되었을 때의 처리

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


          /*  case R.id.btnRed:
                btnRed.setTextColor(Color.RED);
                break;
            case R.id.btnBlue:
                btnBlue.setTextColor(Color.BLUE);
                break;
            case R.id.btnGreen:
                btnGreen.setTextColor(Color.GREEN);
                break;
            case R.id.btnYellow:
                btnYellow.setTextColor(Color.YELLOW);
                break;
            case R.id.btnGray:
                btnGray.setTextColor(Color.GRAY);*/

       // return super.onOptionsItemSelected(item);


}
