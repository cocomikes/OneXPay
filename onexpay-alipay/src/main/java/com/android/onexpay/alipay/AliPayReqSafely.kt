package com.android.onexpay.alipay

import android.app.Activity
import android.os.Message
import com.alipay.sdk.app.PayTask
import com.android.onexpay.core.IPaySend
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * 支付宝支付请求2
 * 安全的的支付宝支付流程，用法
 */
class AliPayReqSafely private constructor(): IPaySend {
    //上下文
    var mActivity: Activity? = null
    //未签名的订单信息
    var rawAliPayOrderInfo: String? = null
    //服务器签名成功的订单信息
    var signedAliPayOrderInfo: String? = null
    //服务器依据订单号返回的AliPay支付信息,可以直接发起支付
    var aliPayInfoByServer: String?=null
    //支付宝支付监听
    var aliPayResultCallback: AliPayResultCallback? = null

    private lateinit var mHandler: AliPayResultHandler

    companion object{
        fun generate(block : AliPayReqSafely.() -> AliPayReqSafely) : AliPayReqSafely{
            return with(AliPayReqSafely()){
                block()
                create()
            }
        }
    }

    fun create() : AliPayReqSafely{
        requireNotNull(mActivity)
        mHandler = AliPayResultHandler(aliPayResultCallback)
        return this
    }

    /**
     * 发送支付宝支付请求
     */
    override fun send() {
        val payInfo: String
        if (aliPayInfoByServer?.isEmpty() == true) {
            payInfo = aliPayInfoByServer!!
        } else {
            requireNotNull(rawAliPayOrderInfo) { "you must provide raw info and signed info" }
            requireNotNull(signedAliPayOrderInfo)  { "you must provide raw info and signed info" }
            // 创建订单信息
            val orderInfo: String = rawAliPayOrderInfo!!
            // 做RSA签名之后的订单信息
            var sign: String = signedAliPayOrderInfo!!
            try {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            // 完整的符合支付宝参数规范的订单信息
            payInfo = ("${orderInfo}&sign=\"$sign\"&${AliPayConfig.signType}")
        }

        val payRunnable = Runnable { // 构造PayTask 对象
            // PayTask对象主要为商户提供订单支付、查询功能，及获取当前开发包版本号。
            val alipay = PayTask(mActivity)
            // orderInfo : app支付请求参数字符串，主要包含商户的订单信息，key=value形式，以&连接。
            // isShowPayLoading :
            // 用户在商户app内部点击付款，是否需要一个loading做为在钱包唤起之前的过渡，这个值设置为true，
            // 将会在调用pay接口的时候直接唤起一个loading，直到唤起H5支付页面或者唤起外部的钱包付款页面loading才消失。
            // 建议将该值设置为true，优化点击付款到支付唤起支付页面的过渡过程。）
            val result = alipay.payV2(payInfo, true)
            val msg = Message()
            msg.what = AliPayResultHandler.SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }
}