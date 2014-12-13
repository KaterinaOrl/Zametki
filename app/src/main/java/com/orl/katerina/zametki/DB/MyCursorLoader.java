package com.orl.katerina.zametki.DB;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by ZZ on 28.11.2014.
 */
public class MyCursorLoader extends android.support.v4.content.CursorLoader {

    MyDBHelper dbHelper;
    Context myContext;
    String query, groupBy;


    public MyCursorLoader(Context context, String query, String groupBy) {
        super(context);
        myContext=context;
        this.query=query;
        this.groupBy=groupBy;
    }

    @Override
    public Cursor loadInBackground() {
        dbHelper = MyDBHelper.getInstance(myContext);
        return dbHelper.getCursor(query, groupBy);
    }
}
