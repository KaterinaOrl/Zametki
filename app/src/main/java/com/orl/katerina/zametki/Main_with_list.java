package com.orl.katerina.zametki;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.orl.katerina.zametki.CustomView.MenuBtn;
import com.orl.katerina.zametki.CustomView.SlideMenu;
import com.orl.katerina.zametki.DB.MyCursorLoader;
import com.orl.katerina.zametki.DB.MyDBHelper;
import com.orl.katerina.zametki.DB.MySimlpeCursorAdapter;
import com.orl.katerina.zametki.UI.ManageItem;

import java.util.Calendar;


public class Main_with_list extends FragmentActivity implements
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener,
        SearchView.OnQueryTextListener,
        SlideMenu.onSortChangeListener
{


    MenuBtn btnMenu;
    SlideMenu slideMenu;
    LinearLayout mainLayout, topLayout;
    ImageView btnAddItem;
    MyDBHelper dbHelper;
    ListView listView;
    MySimlpeCursorAdapter scAdapter;
    SearchView searchView;
    SharedPreferences sPref;
    public final static String ORDER_BY = "order by";
    int width;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_with_list);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        topLayout = (LinearLayout) findViewById(R.id.top_layout);
        btnAddItem = (ImageView) findViewById(R.id.btnItemAdd);
        btnAddItem.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        dbHelper = MyDBHelper.getInstance(this);
        String[] from = {dbHelper.COLUMN_TEXT, dbHelper.COLUMN_TIME};
        int[] to = {R.id.tvText, R.id.tvDate};
        scAdapter = new MySimlpeCursorAdapter(this, R.layout.item_listview, null, from, to, 0);
        listView.setAdapter(scAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
        listView.setOnItemClickListener(this);
        slideMenu = (SlideMenu) findViewById(R.id.slideMenu);
        slideMenu.changeColor(mainLayout, topLayout);
        btnMenu= (MenuBtn)findViewById(R.id.btnMenu);
        btnMenu.setLayout(mainLayout,slideMenu);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        slideMenu.changeColor(mainLayout, topLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mainLayout.getX()!=0)btnMenu.closeMenu();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ManageItem.class);
        intent.putExtra("isExist", false);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        int idRec = cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_ID));
        String text = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_TEXT));
        int status = cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_STATUS));
        long timeNotify = cursor.getLong(cursor.getColumnIndex(dbHelper.COLUMN_NOTIFY));
        Intent intent = new Intent(this, ManageItem.class);
        intent.putExtra("ID", idRec);
        intent.putExtra("text", text);
        intent.putExtra("status", status);
        intent.putExtra("isExist", true);
        intent.putExtra("timeNotify", timeNotify );
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    int idRec;
    if (data == null) {
        return;
    }
    ContentValues cv = new ContentValues();
    cv.put(dbHelper.COLUMN_TEXT, data.getStringExtra("text"));
    cv.put(dbHelper.COLUMN_TIME, getCurrentTime());
    cv.put(dbHelper.COLUMN_STATUS, data.getIntExtra("status",0));
    cv.put(dbHelper.COLUMN_NOTIFY, data.getLongExtra("notify",0));
    Log.d("aaa", "take an item in main activity, time ="+data.getLongExtra("notify",0));
    switch (resultCode) {
        case 11:
            dbHelper.addRec(cv);
            break;
        case 21:
            idRec=data.getIntExtra("ID",0);
            dbHelper.editRec(idRec, cv);
            break;
        case 22:
            idRec=data.getIntExtra("ID",0);
            dbHelper.delRec(idRec);
            break;
    }
    getSupportLoaderManager().getLoader(0).forceLoad();
    if (data.getLongExtra("notify",0)!=0) dbHelper.startNotify();
}

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String query=null;
        sPref = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);
        String orderBy=sPref.getString(ORDER_BY, null);
        if (i==1) query=bundle.getString("query");
        return new MyCursorLoader(this, query, orderBy);
    }
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoader, Cursor cursor) {
        scAdapter.changeCursor(cursor);
    }
    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> cursorLoader) {
        scAdapter.changeCursor(null);
    }

   private String getCurrentTime() {
       String curentdate;
        Calendar calendar = Calendar.getInstance();
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
       if (day<10) curentdate = ""+year+"."+month+".0"+day;
       else curentdate = ""+year+"."+month+"."+day;
        return curentdate;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        showResults(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        showResults(s);
        return false;
    }

    private void showResults(String query) {
        Cursor searchCursor = null;
        if (query!=null){
            Bundle bundleSearch = new Bundle();
            bundleSearch.putString("query", query);
            getSupportLoaderManager().restartLoader(1, bundleSearch , this);
        }

        if (searchCursor!=null){
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }

    @Override
    public void sortChange(String s) {
        btnMenu.closeMenu();
        sPref = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(ORDER_BY, s);
        ed.commit();
        getSupportLoaderManager().restartLoader(0, null, this);
    }


}
