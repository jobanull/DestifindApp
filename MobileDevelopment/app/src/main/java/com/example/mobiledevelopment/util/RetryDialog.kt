package com.example.mobiledevelopment.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.mobiledevelopment.R

class RetryDialog(context: Context, private val retryListener: RetryListener) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.retry_layout)

        val errorMessageTextView: TextView = findViewById(R.id.textViewErrorMessage)
        val retryButton: Button = findViewById(R.id.buttonRetry)

        retryButton.setOnClickListener {
            dismiss()
            retryListener.onRetry()
        }
    }
}

interface RetryListener {
    fun onRetry()
}