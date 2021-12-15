package com.android.onexpay.alipay

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log

internal class AliPayResultHandler(private val mAliPayListener : AliPayResultCallback?) : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            SDK_PAY_FLAG -> {
                Log.d(TAG, "" + msg.obj)
                if (msg.obj == null) {
                    Log.d(TAG, "支付异常")
                    return
                }
                try {
                    val payResult = AliPayResult(msg.obj as Map<String, String?>)

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    val resultInfo = payResult.result
                    val resultStatus = payResult.resultStatus

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Log.d(TAG, "支付成功")
                        mAliPayListener?.onPaySuccess(resultInfo)
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Log.d(TAG, "支付结果确认中")
                            mAliPayListener?.onPayConfirming(resultInfo)
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Log.d(TAG, "支付失败")
                            mAliPayListener?.onPayFailure(payResult.memo)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "支付失败", e)
                    mAliPayListener?.onPayFailure("支付失败")
                }
            }
            SDK_CHECK_FLAG -> {
                Log.d(TAG, "检查结果为：" + msg.obj)
                mAliPayListener?.onPayCheck(msg.obj.toString())
            }
            else -> { }
        }
    }

    companion object {
        private const val TAG = "OneXPay_AliPay"

        /**
         * ali pay sdk flag
         */
        const val SDK_PAY_FLAG = 1
        const val SDK_CHECK_FLAG = 2
    }
}