package com.android.onexpay.unionpay

import android.content.Context
import com.unionpay.UPPayAssistEx
import com.unionpay.UPQuerySEPayInfoCallback

object UnionPayUtil {
    /** 检测是否已安装云闪付客户端 */
    fun checkUnionPayWalletInstalled(context: Context): Boolean = UPPayAssistEx.checkWalletInstalled(context)

    /**
     * 获取银联支付状态（调用指定手机Pay支付接口（startSEPay()）之前，需要先调用检查手机Pay状态 getSEPayInfo()
     * 获取seType，后调用startSEPay()）
     */
    fun getSEPayInfo(context : Context, callback : UPQuerySEPayInfoCallback){
        UPPayAssistEx.getSEPayInfo(context, callback)
    }

    /**
     * 银联支付（指定支付类别）
     * @param tn 交易流水号，为商户后台从银联后台获取
     * @param debugMode  true : "01" - 连接银联测试环境; false : "00" - 启动银联正式环境
     * @param type 手机pay支付类别
     */
    fun startSpecialPay(context : Context, tn : String, debugMode : Boolean, type : String){
        UPPayAssistEx.startSEPay(context, null, null, tn, if(debugMode) "00" else "01", type)
    }

    /**
     * 银联支付（普通）
     * @param tn 交易流水号，为商户后台从银联后台获取
     * @param debugMode  true : "01" - 连接银联测试环境; false : "00" - 启动银联正式环境
     */
    fun startPay(context : Context, tn : String, debugMode : Boolean){
        UPPayAssistEx.startPay(context, null, null, tn, if(debugMode) "00" else "01")
    }
}