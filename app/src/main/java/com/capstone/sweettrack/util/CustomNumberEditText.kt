package com.capstone.sweettrack.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.coding.sweettrack.R

class CustomNumberEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateNumber(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateNumber(number: String) {
        error = if (number.length < 4) {
            context.getString(R.string.invalid_format_number)
        } else {
            null
        }
    }
}