package com.orl.katerina.zametki.DB;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.orl.katerina.zametki.R;

import java.util.Calendar;

/**
 * Created by ZZ on 30.11.2014.
 */
public class MySimlpeCursorAdapter extends SimpleCursorAdapter{

    Context myContext;
    Cursor myC;
    ViewHolder holder;

    public MySimlpeCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        myContext=context;
        myC=c;

    }

    static class ViewHolder {
        public TextView tvText, tvDate, tvNotify;
        public ImageView imageViewStatus, imageViewNotify;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        myC=cursor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;
        root = convertView;
        if (root == null) {
            LayoutInflater layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = layoutInflater.inflate(R.layout.item_listview, null, false);
            holder = new ViewHolder();
            holder.tvText = (TextView) root.findViewById(R.id.tvText);
            holder.tvDate = (TextView) root.findViewById(R.id.tvDate);
            holder.imageViewStatus = (ImageView) root.findViewById(R.id.imageViewStatus);
            holder.imageViewNotify = (ImageView) root.findViewById(R.id.imageViewNotify);
            holder.tvNotify = (TextView) root.findViewById(R.id.tvTimeNotify);
           root.setTag(holder);
        }
        else {
            holder = (ViewHolder) root.getTag();
        }

        long curentTime=System.currentTimeMillis();
        if (myC!=null) {
            myC.moveToPosition(position);
            holder.tvText.setText(myC.getString(myC.getColumnIndex(MyDBHelper.COLUMN_TEXT)));
            holder.tvDate.setText(myC.getString(myC.getColumnIndex(MyDBHelper.COLUMN_TIME)));
            if (myC.getInt(myC.getColumnIndex(MyDBHelper.COLUMN_STATUS))==1)
                holder.imageViewStatus.setVisibility(View.VISIBLE);
            else holder.imageViewStatus.setVisibility(View.INVISIBLE);
            long time = myC.getLong(myC.getColumnIndex(MyDBHelper.COLUMN_NOTIFY));
            holder.tvNotify.setText(time+"");
            long timeNotify=myC.getLong(myC.getColumnIndex(MyDBHelper.COLUMN_NOTIFY));
          if (timeNotify>=curentTime)
          {holder.tvNotify.setText(getDataTime(timeNotify));
          holder.imageViewNotify.setVisibility(View.VISIBLE);}
           else if (timeNotify==0) {
              holder.tvNotify.setText("");
              holder.imageViewNotify.setVisibility(View.INVISIBLE);
        }
                else {
              holder.tvNotify.setText("Прошло");
              holder.imageViewNotify.setVisibility(View.VISIBLE);
          }

        }
        return root;
    }

    private String getDataTime(long time) {
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

        return ""+day+"."+month+"."+year+"."+ " в "+hour+":"+min;
    }
}
