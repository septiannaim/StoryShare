package com.example.storyshare.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomNameEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = ""
    }

    private fun setup() {
        maxLines = 1
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
    }
}
