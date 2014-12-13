package com.orl.katerina.zametki.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orl.katerina.zametki.CustomView.MenuBtn;
import com.orl.katerina.zametki.CustomView.SlideMenu;
import com.orl.katerina.zametki.Main_with_list;
import com.orl.katerina.zametki.R;

import java.util.Calendar;

public class ManageItem extends Activity implements
        View.OnClickListener,
        SlideMenu.onSortChangeListener,
        CompoundButton.OnCheckedChangeListener {

    MenuBtn btnMenu;
    ImageView btnItemAdd, btnItemDel;
    LinearLayout mainLayout, topLayout;
    SlideMenu slideMenu;
    CheckBox checkBoxStatus, checkBoxNotify;
    Boolean isExist;
    EditText etText;
    String text;
    int idRec, width;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_item);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        topLayout = (LinearLayout) findViewById(R.id.top_layout);
                btnItemAdd = (ImageView) findViewById(R.id.btnItemAdd);
        btnItemAdd.setOnClickListener(this);
        etText = (EditText) findViewById(R.id.etText);
        btnItemDel = (ImageView) findViewById(R.id.btnItemDel);
        btnItemDel.setOnClickListener(this);
        slideMenu = (SlideMenu) findViewById(R.id.slideMenu);
        slideMenu.changeColor(mainLayout, topLayout);
        btnMenu = (MenuBtn) findViewById(R.id.btnMenu);
        btnMenu.setLayout(mainLayout,slideMenu);
        checkBoxStatus = (CheckBox) findViewById(R.id.checkBoxStatus);
        checkBoxNotify = (CheckBox) findViewById(R.id.checkBoxNotify);
        checkBoxNotify.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        Intent intent = getIntent();
        isExist = intent.getBooleanExtra("isExist", false);
        if (isExist) {
            idRec = intent.getIntExtra("ID", 0);
            text = intent.getStringExtra("text");
            etText.setText(text);
            if (intent.getIntExtra("status", 0) == 1) checkBoxStatus.setChecked(true);
            time = intent.getLongExtra("timeNotify",0);
            if ((time!=0)&(time!=-1)) {
                setCheckBoxNotify(time);
                checkBoxNotify.setChecked(true);
            }
        }
        checkBoxNotify.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            Intent intent = new Intent(this, DataTimePicker.class);
            startActivityForResult(intent, 1);
        }
        else{
            checkBoxNotify.setText(getResources().getString(R.string.checkBoxNotify));
            time=0;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            time = data.getLongExtra("time", 0);
            setCheckBoxNotify(time);}

    }

    public void setCheckBoxNotify(long time){
        String min;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (calendar.get(Calendar.MINUTE)<10)
            min = "0"+calendar.get(Calendar.MINUTE);
        else min = ""+calendar.get(Calendar.MINUTE);
        checkBoxNotify.setText(getResources().getString(R.string.checkBoxNotify)+
                " "+day+"."+month+"."+year+"."+ " в "+hour+":"+min);

}

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (!isExist){
        switch (view.getId()) {
            case (R.id.btnItemAdd):
                intent.putExtra("text", etText.getText().toString());
                int status = 0;
                if (checkBoxStatus.isChecked()) status = 1;
                intent.putExtra("status", status);
                if (checkBoxNotify.isChecked())intent.putExtra("notify", time);
                else intent.putExtra("notify", 0);
                setResult(11, intent);
                break;
            case (R.id.btnItemDel):
                setResult(12, intent);
                break;
        }}

        if (isExist){
            intent.putExtra("ID",idRec);
            switch (view.getId()) {
                case (R.id.btnItemAdd):
                    intent.putExtra("text", etText.getText().toString());
                    int status=0;
                    if (checkBoxStatus.isChecked()) status=1;
                    intent.putExtra("status", status );
                    if (checkBoxNotify.isChecked())intent.putExtra("notify", time);
                    else intent.putExtra("notify", 0);
                    setResult(21, intent);
                    break;
                case (R.id.btnItemDel):
                    setResult(22, intent);
                    break;
            }
        }

        finish();
    }

    @Override
    public void sortChange(String s) {

        SharedPreferences sPref = getSharedPreferences("mysettings", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Main_with_list.ORDER_BY, s);
        ed.commit();

    }

}