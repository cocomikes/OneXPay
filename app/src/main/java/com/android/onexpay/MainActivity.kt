package com.android.onexpay

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.onexpay.alipay.AliPayConfig
import com.android.onexpay.alipay.AliPayReq
import com.android.onexpay.alipay.AliPayReqSafely
import com.android.onexpay.alipay.AliPayResultCallback
import com.android.onexpay.core.OneXPay
import com.android.onexpay.unionpay.UnionPayConfig
import com.android.onexpay.unionpay.UnionPayReq
import com.android.onexpay.unionpay.UnionPayResultCallBack
import com.android.onexpay.wxpay.WXPayResultCallBack
import com.android.onexpay.wxpay.WxPayConfig
import com.android.onexpay.wxpay.WxPayReq

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_alipay1).setOnClickListener {
            OneXPay.doPay(AliPayReq.generate {
                mActivity = this@MainActivity
                aliPayConfig = AliPayConfig.builder {
                    aliRsaPrivate = ""              //设置私钥 (商户私钥，pkcs8格式)
                    aliRsaPublic = ""               //设置公钥(支付宝公钥)
                    partner = ""                    //设置商户
                    seller = ""                     //设置商户收款账号
                    outTradeNo = ""                 //设置唯一订单号
                    price = ""                      //设置订单价格
                    subject = ""                    //设置订单标题
                    body = ""                       //设置订单内容
                    callbackUrl = ""                //设置回调地址
                    build()
                }
                aliPayResultCallback = object : AliPayResultCallback{
                    override fun onPaySuccess(resultInfo: String?) {
                    }

                    override fun onPayFailure(resultInfo: String?) {
                    }

                    override fun onPayConfirming(resultInfo: String?) {
                    }

                    override fun onPayCheck(status: String) {
                    }
                }
                create()
            })
        }

        findViewById<Button>(R.id.btn_alipay2).setOnClickListener {
            OneXPay.doPay(AliPayReqSafely.generate {
                mActivity = this@MainActivity
                rawAliPayOrderInfo = AliPayConfig.builder {
                    partner = ""                    //设置商户
                    seller = ""                     //设置商户收款账号
                    outTradeNo = ""                 //设置唯一订单号
                    price = ""                      //设置订单价格
                    subject = ""                    //设置订单标题
                    body = ""                       //设置订单内容
                    callbackUrl = ""                //设置回调地址
                    build()
                }.createOrderInfo()
                signedAliPayOrderInfo = ""
                aliPayResultCallback = object : AliPayResultCallback{
                    override fun onPaySuccess(resultInfo: String?) {
                    }

                    override fun onPayFailure(resultInfo: String?) {
                    }

                    override fun onPayConfirming(resultInfo: String?) {
                    }

                    override fun onPayCheck(status: String) {
                    }
                }
                create()
            })
        }

        findViewById<Button>(R.id.btn_alipay3).setOnClickListener {
            OneXPay.doPay(AliPayReqSafely.generate {
                mActivity = this@MainActivity
                aliPayInfoByServer = "123"
                aliPayResultCallback = object : AliPayResultCallback{
                    override fun onPaySuccess(resultInfo: String?) {
                    }

                    override fun onPayFailure(resultInfo: String?) {
                    }

                    override fun onPayConfirming(resultInfo: String?) {
                    }

                    override fun onPayCheck(status: String) {
                    }
                }
                create()
            })
        }

        findViewById<Button>(R.id.btn_wxpay).setOnClickListener {
            OneXPay.doPay(WxPayReq.generate {
                mContext = this@MainActivity
                wxPayConfig = WxPayConfig.builder {
                    appId = ""                                  //微信支付AppID
                    partnerId = ""                              //微信支付商户号
                    prepayId = ""                               //预支付码
                    packageValue = ""
                    nonceStr = ""
                    sign = ""                                   //签名
                    timeStamp = ""                              //时间戳
                    build()
                }
                resultCallBack = object : WXPayResultCallBack{
                    override fun paySuccess() {
                    }

                    override fun payFail(errorCode: Int, errorMsg: String) {
                    }
                }
                create()
            })
        }

        findViewById<Button>(R.id.btn_unionpay).setOnClickListener {
            OneXPay.doPay(UnionPayReq.generate {
                mActivity = this@MainActivity
                unionPayConfig = UnionPayConfig.builder {
                    tn = ""
                    debugMode = false
                    build()
                }
                resultCallBack = object : UnionPayResultCallBack{
                    override fun paySuccess(dataOrg: String?, sign: String?, debugMode: Boolean) {
                    }

                    override fun payCancel() {
                    }

                    override fun payFail() {
                    }
                }
                create()
            })
        }
    }
}