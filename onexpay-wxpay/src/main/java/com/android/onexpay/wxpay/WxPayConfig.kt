package com.android.onexpay.wxpay

/**
 * 微信支付请求参数
 */
class WxPayConfig private constructor() {
    //微信支付AppID
    var appId: String? = null

    //微信支付商户号
    var partnerId: String? = null

    //预支付码（重要）
    var prepayId: String? = null

    //"Sign=WXPay"
    var packageValue = "Sign=WXPay"

    var nonceStr: String? = null

    //时间戳
    var timeStamp: String? = null

    //签名
    var sign: String? = null

    fun build(): WxPayConfig {
        return this
    }

    companion object {
        fun builder(block: WxPayConfig.() -> WxPayConfig): WxPayConfig {
            return with(WxPayConfig()) {
                block()
            }
        }
    }
}