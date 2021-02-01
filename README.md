# [One-X-Pay]

### 简介
> 支持微信，支付宝，银联等主流支付的集成库
> 丝滑般享受，快速接入支付功能

### 引入

```Gradle
    implementation 'com.github.cocomikes.OneXPay:onexpay-core:x'
    微信支付
    implementation 'com.github.cocomikes.OneXPay:onexpay-wxpay:x'
    支付宝支付
    implementation 'com.github.cocomikes.OneXPay:onexpay-alipay:x'
    银联支付
    implementation 'com.github.cocomikes.OneXPay:onexpay-unionpay:x'
```

### 更新日志


### 使用说明

#### 1. 微信支付使用
```
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

```

>注意：这里没有金额设置，金额的信息已经包含在预支付码prepayid了。

#### 2. 支付宝支付使用
> 支付宝支付第一种方式(不建议用这种方式，商户私钥暴露在客户端，极其危险，推荐用第二种支付方式)
```
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

```
>支付宝支付第二种方式(**强烈推荐**)

```
        //1.创建支付宝支付订单的信息
        val rawAliPayOrderInfo = AliPayConfig.builder {
                                                 partner = ""                    //设置商户
                                                 seller = ""                     //设置商户收款账号
                                                 outTradeNo = ""                 //设置唯一订单号
                                                 price = ""                      //设置订单价格
                                                 subject = ""                    //设置订单标题
                                                 body = ""                       //设置订单内容
                                                 callbackUrl = ""                //设置回调地址
                                                 build()
                                             }.createOrderInfo()

        //2.签名  支付宝支付订单的信息 ===>>>  商户私钥签名之后的订单信息
        //TODO 这里需要从服务器获取用商户私钥签名之后的订单信息
        val signedAliPayOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);

        //3.发送支付宝支付请求
        OneXPay.doPay(AliPayReqSafely.generate {
            mActivity = this@MainActivity
            rawAliPayOrderInfo = ""
            signedAliPayOrderInfo = ""
            aliPayInfoByServer = ""
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
```

>支付宝支付第三种方式(**强烈推荐**)

```
        //1. 依据开发者服务器的订单号拿到支付宝订单信息
        val aliPayInfoByServer = getAliPayOrderInfoByPayOrderNum(payOrderNum);
        //2. 直接发起支付
        OneXPay.doPay(AliPayReqSafely.generate {
            mActivity = this@MainActivity
            aliPayInfoByServer = ""
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
```

#### 3. 银联支付使用
```
        OneXPay.doPay(UnionPayReq.generate {
            mActivity = this@MainActivity
            unionPayConfig = UnionPayConfig.builder {
                tn = ""                 // 交易流水号
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

```


### 存在的问题

### 文档

> 微信支付官方文档 支付流程
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5

> 支付宝支付官方文档 支付流程
https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.sdGXaH&treeId=204&articleId=105296&docType=1


### 注意

#### 微信支付

 - 微信支付必须要在**正式签名**和**正确包名**的应用中才能成功调起。(**重点)

    即商户在微信开放平台申请开发应用后对应包名和对应签名的应用才能成功调起。
    详情请参考微信支付的开发流程文档。

    https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5

 - 微信支付API没有在客户端显示的设置回调，回调是在Server端设置的。(与支付宝支付的区别，支付宝的回调是在客户端设置的)

#### 支付宝支付

 - 支付宝支付为了保证交易双方的身份和数据安全， 需要配置双方密钥。

    详情请参考支付宝支付的密钥处理体系文档。

    https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.1wPnBT&treeId=204&articleId=106079&docType=1



