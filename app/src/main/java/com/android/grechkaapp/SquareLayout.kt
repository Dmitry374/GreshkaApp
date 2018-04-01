package com.android.grechkaapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by Lenovo on 30.03.2018.
 */

/*
* class SquareLayout(var mCtx: Context, var attrs: AttributeSet, var defStyleAttr: Int, var defStyleRes: Int)
    : RelativeLayout(mCtx, attrs, defStyleAttr, defStyleRes)
* */

class SquareLayout : RelativeLayout {

    constructor(mCtx: Context) : super(mCtx)

    constructor(mCtx: Context, attrs: AttributeSet) : super(mCtx, attrs)

    constructor(mCtx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(mCtx, attrs, defStyleAttr)

    constructor(mCtx: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(mCtx, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}