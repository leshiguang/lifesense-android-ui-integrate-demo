package com.lifesense.android.demo.wxapi;

import com.lifesense.share.ui.ShareActionActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Create by qwerty
 * Create on 2021/5/19
 **/
public class WXEntryActivity extends ShareActionActivity implements IWXAPIEventHandler {

	public void onReq(BaseReq baseReq) {
	}

	public void onResp(BaseResp resp) {
		this.handleResp(resp);
	}
}
