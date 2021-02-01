package com.android.onexpay.unionpay

interface UnionPayResultCallBack {
    /**
     * 支付成功, 商户需送去商户后台做验签. 验签成功才是真正的支付成功
     * @param dataOrg
     * @param sign
     * @param debugMode  true : "01" - 连接银联测试环境; false : "00" - 启动银联正式环境
     */
    fun paySuccess(dataOrg : String?, sign : String?, debugMode : Boolean)

    /**
     * 支付取消
     */
    fun payCancel()

    /**
     * 支付失败
     */
    fun payFail()
}