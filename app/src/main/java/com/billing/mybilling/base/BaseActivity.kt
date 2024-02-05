package com.billing.mybilling.base

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.billing.mybilling.R

open class BaseActivity:AppCompatActivity() {
    private var progressDialog : ProgressDialog? = null

    fun showLoading(visible:Boolean){
        if (visible){
            progressDialog = showLoadingDialog(this)
        }else{
            hideLoading()
        }
    }

    fun hideLoading() {
        progressDialog?.cancel()
    }

    private fun showLoadingDialog(context: Context): ProgressDialog {

        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window!=null){
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.spin_view)
        progressDialog.isIndeterminate = true
        //  progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
}