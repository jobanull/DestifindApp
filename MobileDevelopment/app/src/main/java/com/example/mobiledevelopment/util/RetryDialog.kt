package com.example.mobiledevelopment.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RetryDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Retry")
            .setMessage("Location is not valid. Retry?")
            .setPositiveButton("Retry") { _, _ ->
                // Handle retry action here
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancel action here or dismiss the dialog
            }
            .create()
    }
}