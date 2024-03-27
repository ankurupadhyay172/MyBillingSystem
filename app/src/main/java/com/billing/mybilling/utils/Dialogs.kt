package com.billing.mybilling.utils

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.billing.mybilling.R
import com.billing.mybilling.databinding.RatingDialogBinding
import com.billing.mybilling.databinding.WarningDialogBinding

fun showWarningDialog(
    context: Activity,
    layoutInflater: LayoutInflater,
    msg : String,
    actionYes:()->Unit,
    actionNo : () -> Unit
): Dialog {
    val binding = DataBindingUtil.inflate<WarningDialogBinding>(
        layoutInflater,
        R.layout.warning_dialog, null, true
    )
    return Dialog(context, R.style.DialogFragmentThemeCompact).apply {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        binding.apply {
            var str = msg
            btnYes.setOnClickListener {
                dismiss()
                actionYes.invoke()
            }
            btnNo.setOnClickListener {
                dismiss()
            }
        }
    }
}



fun showRatingDialog(
    context: Activity,
    layoutInflater: LayoutInflater,
    msg : String,
    actionYes:()->Unit,
    actionNo : () -> Unit
): Dialog {
    val binding = DataBindingUtil.inflate<RatingDialogBinding>(
        layoutInflater,
        R.layout.rating_dialog, null, true
    )
    return Dialog(context, R.style.DialogFragmentThemeCompact).apply {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        binding.apply {
            var str = msg
            btnYes.setOnClickListener {
                dismiss()
                actionYes.invoke()
            }
            btnNo.setOnClickListener {
                dismiss()
            }
            rating.setOnRatingBarChangeListener { ratingBar, rating, b ->
                var str = "your rating is $rating"
                tvWelcome.text = str

            }

        }
    }
}