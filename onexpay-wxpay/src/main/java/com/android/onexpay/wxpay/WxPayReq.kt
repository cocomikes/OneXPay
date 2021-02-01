package com.android.onexpay.wxpay

import android.content.Context
import com.android.onexpay.core.IPaySend
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WxPayReq private constructor(): IPaySend{
    var mContext : Context?=null
    var wxPayConfig: WxPayConfig?=null
    var resultCallBack : WXPayResultCallBack?=null

    companion object{
        fun generate(block : WxPayReq.() -> WxPayReq) : WxPayReq{
            return with(WxPayReq()){
                block()
                create()
            }
        }
    }

    fun create() : WxPayReq{
        requireNotNull(mContext)
        requireNotNull(wxPayConfig)
        WXPayApiCache.wxPayConfig = wxPayConfig
        WXPayApiCache.wXPayAppID = wxPayConfig!!.appId
        WXPayApiCache.resultCallBack = resultCallBack
        return this
    }

    override fun send() {
        val _wxPayConfig = wxPayConfig!!
        val mWXApi = WXAPIFactory.createWXAPI(mContext, null)
        mWXApi.registerApp(_wxPayConfig.appId)

        if (!mWXApi.isWXAppInstalled) {
            resultCallBack?.payFail(3, "Not Installed or Not Support")
            return
        }

        val payReq = PayReq().apply {
            //微信支付AppID
            appId = _wxPayConfig.appId
            //微信支付商户号
            partnerId = _wxPayConfig.partnerId
            //预支付码（重要）
            prepayId = _wxPayConfig.prepayId
            packageValue = _wxPayConfig.packageValue
            nonceStr = _wxPayConfig.nonceStr
            //时间戳
            timeStamp = _wxPayConfig.timeStamp
            //签名
            sign = _wxPayConfig.sign
        }

        if (payReq.checkArgs()) {
            //调起支付
            mWXApi.sendReq(payReq)
        } else {
            resultCallBack?.payFail(4, "Pay args not legal")
        }
    }
}