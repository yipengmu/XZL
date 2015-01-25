package net.xinzeling.share;

import net.xinzeling.share.SNSShareMenu.CloseShareViewListener;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

public class CommonShareActivity extends Activity implements IWXAPIEventHandler {
	private String mShareTextMsg = Utils.SNS_SHARE_BUNDLE_MSG;
	private String mShareUrl = Utils.SNS_SHARE_BUNDLE_URL;
	private String mShareBitmapUrl = Utils.SNS_SHARE_BUNDLE_BITMAP_URL;
	private String mTitleContent = "信则聆";

	public static String SHARE_TEXT_CONTENT = "text_content";
	public static String SHARE_TITLE  = "title";
	public static String SHARE_WEBSITE_URL = "";
	public static String SHARE_IMAGA_URL = "";

	// 微信仅分享分享
	private boolean mShareWeixinOnlyText = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mShareTextMsg = bundle.getString("text_content");
			mTitleContent = bundle.getString("title");
			mShareUrl = bundle.getString("website_url");
			mShareBitmapUrl = bundle.getString("image_url");
		}

		setContentView(CreateView());

		View ll_share_icons = findViewById(R.id.ll_share_icons);

		ll_share_icons.setVisibility(View.VISIBLE);
		ll_share_icons.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_up));

	}

	public View CreateView() {

		SNSShareMenu mSNSShareMenu = new SNSShareMenu(this);
		mSNSShareMenu.setMsgText(mShareTextMsg);
		mSNSShareMenu.setShareUrl(mShareUrl);
//		mSNSShareMenu.setIsCleanUrl(mIsCleanUrl);
		mSNSShareMenu.setTitleContent(mTitleContent);
		mSNSShareMenu.setShareBitmapUrl(mShareBitmapUrl);
		mSNSShareMenu.setCloseListener(new CloseShareViewListener() {

			@Override
			public void closeShareView() {
				finish();
			}
		});
		return mSNSShareMenu.mMenuRootView;
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
//		int result = 0;
//
//		switch (resp.errCode) {
//		case BaseResp.ErrCode.ERR_OK:
//			result = R.string.errcode_success;
//			break;
//		case BaseResp.ErrCode.ERR_USER_CANCEL:
//			result = R.string.errcode_cancel;
//			break;
//		case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			result = R.string.errcode_deny;
//			break;
//		default:
//			result = R.string.errcode_unknown;
//			break;
//		}
//
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);

//		Intent intent = new Intent(this, ShowFromWXActivity.class);
//		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//		startActivity(intent);
		finish();
	}

}
