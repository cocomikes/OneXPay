package com.android.onexpay.alipay

/**
 * 支付宝支付配置
 */
class AliPayConfig private constructor(){
    // 商户私钥，pkcs8格式
    var aliRsaPrivate: String?=null
    // 支付宝公钥
    var aliRsaPublic: String?=null
    // 商户PID
    // 签约合作者身份ID
    var partner: String?=null
    // 商户收款账号
    // 签约卖家支付宝账号
    var seller: String?=null
    var outTradeNo : String? = null //设置唯一订单号
    var subject : String? = null //设置订单标题
    var body : String? = null //设置订单详情
    var price : String? = null //设置价格
    var callbackUrl : String? = null //设置请求回调

    fun build() : AliPayConfig{
        return this
    }

    /**
     * 创建订单信息
     * partner 签约合作者身份ID
     * seller 签约卖家支付宝账号
     * outTradeNo 商户网站唯一订单号
     * subject 商品名称
     * body 商品详情
     * price 商品金额
     * callbackUrl 服务器异步通知页面路径
     */
    fun createOrderInfo(): String {
        // 签约合作者身份ID
        val orderInfo = StringBuilder()
        orderInfo.append("partner=\"${partner}\"")
        // 签约卖家支付宝账号
        orderInfo.append("&seller_id=\"${seller}\"")
        // 商户网站唯一订单号
        orderInfo.append("&out_trade_no=\"${outTradeNo}\"")
        // 商品的标题/交易标题/订单标题/订单关键字等。
        orderInfo.append("&subject=\"${subject}\"")
        // 商品详情
        orderInfo.append("&body=\"${body}\"")
        // 商品金额
        orderInfo.append("&total_fee=\"${price}\"")
        // 服务器异步通知页面路径
        orderInfo.append("&notify_url=\"${callbackUrl}\"")
        // 服务接口名称， 固定值
        orderInfo.append("&service=\"mobile.securitypay.pay\"")
        // 支付类型， 固定值
        orderInfo.append("&payment_type=\"1\"")
        // 参数编码， 固定值
        orderInfo.append("&_input_charset=\"utf-8\"")
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo.append("&it_b_pay=\"30m\"")
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//         orderInfo.append("&extern_token=\"$extern_token\"")
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo.append("&return_url=\"m.alipay.com\"")
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo.append("&paymethod=\"$expressGateway\"");
        return orderInfo.toString()
    }

    companion object{
        fun builder(block : AliPayConfig.() -> AliPayConfig) : AliPayConfig{
            return with(AliPayConfig()){
                block()
            }
        }

        /**
         * get the sign type we use. 获取签名方式
         */
        const val signType: String = "sign_type=\"RSA\""
    }
}