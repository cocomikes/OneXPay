package com.android.onexpay.core

interface IPaySend {
    // 发起支付
    fun send()
}

object OneXPay{
    fun doPay(paySend: IPaySend){
        paySend.send()
    }
}