package com.bignerdranch.android.fyp.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by David on 1/6/2017.
 */

public class VerticalLineDecorator extends RecyclerView.ItemDecoration {
    private int space=0;

    public VerticalLineDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = space;

        outRect.bottom = space;
    }
}