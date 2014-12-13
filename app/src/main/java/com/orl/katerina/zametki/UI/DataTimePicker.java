package com.orl.katerina.zametki.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.orl.katerina.zametki.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DataTimePicker extends Activity implements View.OnClickListener {

    TimePicker timePicker;
    DatePicker datePicker;
    Calendar now;
    GregorianCalendar notifyTime;
    int hourOfDayN, minuteN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_time_picker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        now = Calendar.getInstance();
        timePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(now.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hourOfDayN = hourOfDay;
                minuteN = minute;
            }
        });
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button btnOk = (Button) findViewById(R.id.btnTimeOk);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        notifyTime = new GregorianCalendar
                (datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),hourOfDayN,minuteN);
        Intent intent = new Intent();
        intent.putExtra("time", notifyTime.getTimeInMillis() );
        setResult(0, intent);
        finish();
    }
}
