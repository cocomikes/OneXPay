package com.android.onexpay.alipay

interface AliPayResultCallback {
    fun onPaySuccess(resultInfo: String?)
    fun onPayFailure(resultInfo: String?)
    fun onPayConfirming(resultInfo: String?)
    fun onPayCheck(status: String)
}