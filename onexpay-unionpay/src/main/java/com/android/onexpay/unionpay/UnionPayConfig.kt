package com.android.onexpay.unionpay

class UnionPayConfig private constructor() {
    var debugMode = false        // true : "01" - 连接银联测试环境; false : "00" - 启动银联正式环境
    var tn = ""                  // 交易流水号，为商户后台从银联后台获取

    fun build(): UnionPayConfig {
        return this
    }

    companion object {
        fun builder(block: UnionPayConfig.() -> UnionPayConfig): UnionPayConfig {
            return with(UnionPayConfig()) {
                block()
            }
        }
    }
}