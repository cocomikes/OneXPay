package com.android.onexpay.alipay

import android.app.Activity
import android.os.Message
import com.alipay.sdk.app.PayTask
import com.android.onexpay.alipay.util.AliPaySignUtils
import com.android.onexpay.core.IPaySend
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * 支付宝支付请求
 * 商户私钥暴露在客户端
 */
class AliPayReq private constructor(): IPaySend {
    //上下文
    var mActivity: Activity?=null
    //支付宝支付的配置
    var aliPayConfig: AliPayConfig?=null
    //支付宝支付监听
    var aliPayResultCallback: AliPayResultCallback? = null

    private lateinit var mHandler: AliPayResultHandler

    companion object{
        fun generate(block : AliPayReq.() -> AliPayReq) : AliPayReq{
            return with(AliPayReq()){
                block()
                create()
            }
        }
    }

    fun create() : AliPayReq{
        requireNotNull(mActivity)
        requireNotNull(aliPayConfig)
        mHandler = AliPayResultHandler(aliPayResultCallback)
        return this
    }

    /**
     * 发送支付宝支付请求
     */
    override fun send() {
        // 创建订单信息
        val orderInfo = aliPayConfig!!.createOrderInfo()
        // 对订单做RSA 签名
        var sign = AliPaySignUtils.sign(orderInfo, aliPayConfig!!.aliRsaPrivate)
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        // 完整的符合支付宝参数规范的订单信息
        val payInfo = ("${orderInfo}&sign=\"$sign\"&${AliPayConfig.signType}")
        val payRunnable = Runnable { // 构造PayTask 对象
            val alipay = PayTask(mActivity)
            // 调用支付接口，获取支付结果
            //orderInfo : app支付请求参数字符串，主要包含商户的订单信息，key=value形式，以&连接。
            //isShowPayLoading :
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