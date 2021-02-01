package com.android.onexpay.wxpay

interface WXPayResultCallBack {
    //支付成功
    fun paySuccess()
    //支付失败
    fun payFail(errorCode: Int, errorMsg: String)
}