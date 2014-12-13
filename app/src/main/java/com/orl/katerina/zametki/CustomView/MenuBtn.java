package com.orl.katerina.zametki.CustomView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.orl.katerina.zametki.R;

/**
 * Created by ZZ on 07.11.2014.
 */
public class MenuBtn extends ImageView{
    Context myContext;
    MenuBtn menuBtn;
    View myLayout, mySlideMenu;
    int startX, finishX;
    int width;



    public MenuBtn(Context context) {
        super(context);
        myContext = context;
        initMenuBtn();
    }



    public MenuBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        initMenuBtn();
    }

    public MenuBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        myContext = context;
        initMenuBtn();
    }

    private void initMenuBtn() {
        menuBtn = this;
        menuBtn.setImageBitmap(BitmapFactory.decodeResource(myContext.getResources(), R.drawable.ic_menu));
    }


    public void setLayout(final View layout, View slidemenu) {
        myLayout=layout;
       mySlideMenu=slidemenu;
        Log.d("aaa", "on slide width "+width);
        menuBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                width= mySlideMenu.getWidth();
                if (myLayout.getX() == 0) openMenu();
                else closeMenu();
            }
        });
    }
    public void openMenu(){
        startX=0;
        finishX=-width;
        ObjectAnimator animShow = ObjectAnimator.ofFloat(myLayout, "x" , startX, finishX);
        animShow.setDuration(500);
        animShow.start();
    }

    public void closeMenu(){
        startX=-width;
        finishX=0;
        ObjectAnimator animShow = ObjectAnimator.ofFloat(myLayout, "x" , startX, finishX);
        animShow.setDuration(500);
        animShow.start();
    }
}
