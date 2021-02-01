package com.android.onexpay.wxpay

internal object WXPayApiCache{
    //微信APP_ID
    var wXPayAppID: String? = null
    //微信支付配置
    var wxPayConfig: WxPayConfig? = null
    var resultCallBack : WXPayResultCallBack?=null
}