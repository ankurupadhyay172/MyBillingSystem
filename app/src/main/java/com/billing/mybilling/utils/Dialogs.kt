package com.billing.mybilling.utils

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.billing.mybilling.R
import com.billing.mybilling.databinding.FormDialogBinding
import com.billing.mybilling.databinding.FormMobileDialogBinding
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
            tvErrorMessage.text = str
            btnYes.setOnClickListener {
                dismiss()
                actionYes.invoke()
            }
            btnNo.setOnClickListener {
                dismiss()
                actionNo.invoke()
            }
        }
    }
}


fun updateMobileDialog(
    context: Activity,
    layoutInflater: LayoutInflater,
    msg : String,
    mobile:String?,
    actionSubmit:(str:String)->Unit
): Dialog {
    val binding = DataBindingUtil.inflate<FormMobileDialogBinding>(
        layoutInflater,
        R.layout.form_mobile_dialog, null, true
    )
    return Dialog(context, R.style.DialogFragmentThemeCompact).apply {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        binding.apply {
            var str = msg
            title.text = msg
            edtMobile.setText(mobile)
            submit.setOnClickListener {
                if (edtMobile.text.toString().length==10){
                    actionSubmit.invoke(edtMobile.text.toString())
                    dismiss()
                }else{
                    edtMobile.error = "Please enter 10 digit mobile number"
                }
            }
        }
    }
}

fun showFormDialog(
    context: Activity,
    layoutInflater: LayoutInflater,
    msg : String,
    amount:Int,
    actionSubmit:(str:String)->Unit
): Dialog {
    val binding = DataBindingUtil.inflate<FormDialogBinding>(
        layoutInflater,
        R.layout.form_dialog, null, true
    )
    return Dialog(context, R.style.DialogFragmentThemeCompact).apply {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        binding.apply {
            var str = msg
            title.text = msg
            submit.setOnClickListener {
                if (edtAmount.text.toString().toInt()<=amount){
                    actionSubmit.invoke(edtAmount.text.toString())
                    dismiss()
                }else{
                    edtAmount.error = "Amount Should not be greater then total amount"
                }
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