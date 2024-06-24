package com.example.storyshare.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyshare.R

class CustomEmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = " "
    }

    private fun initialize() {
        maxLines = 1
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                setSelection(text?.length ?: 0)

                error = when {
                    sequence.isNullOrEmpty() -> null
                    !Patterns.EMAIL_ADDRESS.matcher(sequence).matches() -> resources.getString(R.string.email_not_valid)
                    else -> null
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // No action needed after text changes
            }
        })
    }
}
