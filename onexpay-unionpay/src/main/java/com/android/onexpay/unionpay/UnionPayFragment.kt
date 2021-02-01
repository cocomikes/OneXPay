package com.android.onexpay.unionpay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONException
import org.json.JSONObject

internal class UnionPayFragment : Fragment() {
    companion object {
        private const val REQUEST_PAY = 0x10
        private const val TAG = "UnionPay"

        fun getUnionPayFragment(fragmentManager: FragmentManager): UnionPayFragment {
            val replyFragment = UnionPayFragment()
            fragmentManager.beginTransaction()
                    .add(replyFragment, TAG)
                    .commitNow()

            return replyFragment
        }
    }

    private lateinit var hostContext : Context
    var resultCallBack: UnionPayResultCallBack? = null
    var unionPayConfig: UnionPayConfig? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startUnionPay() {
        UnionPayUtil.startPay(hostContext, unionPayConfig!!.tn, unionPayConfig!!.debugMode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 处理银联手机支付控件返回的支付结果
        if(data == null) return
        if (requestCode == REQUEST_PAY) {
            when(data.extras?.getString("pay_result")?.toLowerCase()){
                "success" -> {
                    var dataOrg : String?=null
                    var sign : String?=null
                    if(data.hasExtra("result_data")){
                        try{
                            val resultJson = JSONObject(data.extras?.getString("result_data") ?: "{}")
                            sign = resultJson.getString("sign")
                            dataOrg = resultJson.getString("data")
                        } catch (ex : JSONException){
                            // ignore this exception
                        }
                    }
                    resultCallBack?.paySuccess(dataOrg, sign, unionPayConfig!!.debugMode)
                }
                "fail" -> {
                    resultCallBack?.payFail()
                }
                "cancel" -> {
                    resultCallBack?.payCancel()
                }
            }
        }
    }
}