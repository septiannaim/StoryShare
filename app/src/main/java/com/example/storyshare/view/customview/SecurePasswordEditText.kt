package com.example.storyshare.view.customview

import com.example.storyshare.R
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SecurePasswordEditText : AppCompatEditText {

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
        hint = ""
    }

    private fun initialize() {
        maxLines = 1
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
                setSelection(length())

                error = when {
                    text.isNullOrEmpty() -> null
                    text.length < 8 -> resources.getString(R.string.minimum_password)
                    else -> null
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // No action needed here
            }
        })
    }
}
