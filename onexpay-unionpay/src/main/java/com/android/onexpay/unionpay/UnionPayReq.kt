package com.android.onexpay.unionpay

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.android.onexpay.core.IPaySend

class UnionPayReq private constructor(): IPaySend {
    //上下文
    var mActivity: Activity?=null
    //银联支付的配置
    var unionPayConfig: UnionPayConfig?=null
    var resultCallBack : UnionPayResultCallBack?=null

    companion object{
        fun generate(block : UnionPayReq.() -> UnionPayReq) : UnionPayReq{
            return with(UnionPayReq()){
                block()
            }
        }
    }

    fun create() : UnionPayReq{
        requireNotNull(mActivity)
        requireNotNull(unionPayConfig)
        return this
    }

    override fun send() {
        mActivity?.let {  activity ->
            if(activity is FragmentActivity){
                val fpm = activity.supportFragmentManager

                val unionPayFragment = UnionPayFragment.getUnionPayFragment(fpm)
                unionPayFragment.resultCallBack = resultCallBack
                unionPayFragment.unionPayConfig = unionPayConfig
                unionPayFragment.startUnionPay()
            }
        }
    }
}