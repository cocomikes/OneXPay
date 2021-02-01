package com.android.onexpay.wxpay

import android.app.Activity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXPayHandler(private val activity: Activity) : IWXAPIEventHandler {
    override fun onReq(baseReq: BaseReq) {}
    override fun onResp(baseResp: BaseResp) {
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (baseResp.errCode) {
                BaseResp.ErrCode.ERR_OK -> WXPayApiCache.resultCallBack?.paySuccess()
                BaseResp.ErrCode.ERR_COMM -> WXPayApiCache.resultCallBack?.payFail(
                    baseResp.errCode,
                    "可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。"
                )
                else -> WXPayApiCache.resultCallBack?.payFail(
                    baseResp.errCode,
                    "无需处理。发生场景：用户不支付了，点击取消，返回APP。"
                )
            }
            //销毁
            activity.finish()
        }
    }
}