package com.orl.katerina.zametki.CustomView;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orl.katerina.zametki.DB.MyDBHelper;
import com.orl.katerina.zametki.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ZZ on 30.10.2014.
 */
public class SlideMenu extends LinearLayout {
    Context mycontext;
    LinearLayout root;
    View mainLoyoutNew,topLoyoutNew;
    ListView settingsList;
    ArrayList<String> arrayList, arraySetting,arrayColor, arraySort;
    final int SETTINGS =0;
    final int COLOR = 1;
    final int SORT = 2;
    TextView tvSettings;

    final String TOP="top";
    final String MAIN="main";

    SharedPreferences sPref;
    int colorTop, colorMain;

    public static int width;





    ArrayAdapter<String> adapter;


    public SlideMenu(Context context) {
        super(context);
        mycontext = context;
        initSlideMenu();

    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mycontext = context;
        initSlideMenu();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mycontext = context;
        initSlideMenu();
    }

    private void initSlideMenu() {
        LayoutInflater layoutInflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root=(LinearLayout)layoutInflater.inflate(R.layout.slide_menu, this);
        //createSettingsList
        arraySetting = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.settingsArray)));
        arrayColor = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.colorArray)));
        arraySort = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sortArray)));
        arrayList = new ArrayList<String>();
        arrayList.addAll(arraySetting);
        settingsList = (ListView) root.findViewById(R.id.settingsList);
        adapter = new ArrayAdapter<String>(mycontext, R.layout.slide_menu_item, arrayList );
        settingsList.setAdapter(adapter);
        settingsList.setTag(SETTINGS);
        tvSettings = (TextView) root.findViewById(R.id.tvSettings);
        tvSettings.setText(getResources().getString(R.string.settings));
        settingsList.setOnItemClickListener(onItemClickListener());
        //set color
        sPref = mycontext.getSharedPreferences(getContext().getString(R.string.prefs_name), mycontext.MODE_PRIVATE);
        colorTop = sPref.getInt(TOP, mycontext.getResources().getColor(R.color.yellow_top));
        colorMain = sPref.getInt(MAIN, mycontext.getResources().getColor(R.color.yellow_main));
        sortChangeListener = (onSortChangeListener) mycontext;

    }
    public  interface onSortChangeListener {
        public void sortChange(String s);
    }
    onSortChangeListener sortChangeListener;

    public AdapterView.OnItemClickListener onItemClickListener()
    {   AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                switch ((Integer)settingsList.getTag()){
                    case SETTINGS:
                        switch (i) {
                            case 0: // set Color List
                                changeList(arrayColor,COLOR, getResources().getStringArray(R.array.settingsArray)[i]);
                            break;
                            case 1: // set Sort List
                                changeList(arraySort,SORT,getResources().getStringArray(R.array.settingsArray)[i]);
                            break;
                        }
                    break;
                    case COLOR:
                        switch (i) {
                            case 0:
                                colorTop = getResources().getColor(R.color.yellow_top);
                                colorMain = getResources().getColor(R.color.yellow_main);
                                break;
                            case 1:
                                colorTop = getResources().getColor(R.color.grey_top);
                                colorMain = getResources().getColor(R.color.grey_main);
                                break;
                            case 2:
                                colorTop = getResources().getColor(R.color.blue_top);
                                colorMain = getResources().getColor(R.color.blue_main);
                                break;
                            case 3:
                                colorTop = getResources().getColor(R.color.green_top);
                                colorMain = getResources().getColor(R.color.green_main);
                                break;
                        }
                        //save color
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt(TOP, colorTop);
                        ed.putInt(MAIN, colorMain);
                        ed.commit();
                        setColor();
                        changeList(arraySetting,SETTINGS,getResources().getString(R.string.settings));
                    break;
                    case SORT:
                        switch (i){
                            case 0: sortChangeListener.sortChange(MyDBHelper.COLUMN_TEXT);
                                break;
                            case 1: sortChangeListener.sortChange(MyDBHelper.COLUMN_TIME);
                                break;
                            case 2: sortChangeListener.sortChange(MyDBHelper.COLUMN_STATUS);
                                break;
                            case 3: sortChangeListener.sortChange(null);
                                break;
                        }

                        changeList(arraySetting,SETTINGS,getResources().getString(R.string.settings));
                    break;
                }

            }
        };

        return onItemClickListener;
    }

    private void changeList(ArrayList<String> arrayListCurent, int tag, String label) {
        arrayList.clear();
        arrayList.addAll(arrayListCurent);
        settingsList.setTag(tag);
        tvSettings.setText(label);
        adapter.notifyDataSetChanged();
    }

    public void changeColor(View mainLayout, View topLayout) {
        sPref = mycontext.getSharedPreferences(getContext().getString(R.string.prefs_name), mycontext.MODE_PRIVATE);
        colorTop = sPref.getInt(TOP, mycontext.getResources().getColor(R.color.yellow_top));
        colorMain = sPref.getInt(MAIN, mycontext.getResources().getColor(R.color.yellow_main));
        mainLoyoutNew = mainLayout;
        topLoyoutNew = topLayout;
        setColor();
    }
    public void setColor(){
        mainLoyoutNew.setBackgroundColor(colorMain);
        topLoyoutNew.setBackgroundColor(colorTop);
    }
}
