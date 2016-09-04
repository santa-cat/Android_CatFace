package com.example.santa.catface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by santa on 16/7/24.
 */
public class Eye extends View {
    private int mColor = 0xffaa5c53;


    public Eye(Context context) {
        this(context, null);
    }

    public Eye(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Eye(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Eye(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        oval.getPaint().setColor(Color.WHITE);
        oval.setBounds(0, 0, getWidth(), getHeight());
        oval.draw(canvas);

        oval.getPaint().setColor(mColor);
        oval.setBounds(getWidth()/3, getHeight()/4, getWidth()*2/3, getHeight()*3/4);
        oval.draw(canvas);
    }


}
