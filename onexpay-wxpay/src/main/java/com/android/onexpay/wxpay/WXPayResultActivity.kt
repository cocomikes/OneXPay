package com.android.onexpay.wxpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXPayResultActivity : Activity() {
    private lateinit var api: IWXAPI
    private var handler: WXPayHandler? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = WXPayHandler(this)
        api = WXAPIFactory.createWXAPI(this, WXPayApiCache.wXPayAppID, false)
        api.handleIntent(intent, handler)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, handler)
    }
}