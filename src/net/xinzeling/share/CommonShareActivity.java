package net.xinzeling.share;

import net.xinzeling.base.BaseActivity;
import net.xinzeling.share.SNSShareMenu.CloseShareViewListener;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class CommonShareActivity extends BaseActivity {
	private String mShareTextMsg = Utils.SNS_SHARE_BUNDLE_MSG;
	private String mShareUrl = Utils.SNS_SHARE_BUNDLE_URL;
	private String mShareBitmapUrl = Utils.SNS_SHARE_BUNDLE_BITMAP_URL;
	/** 默认需要对mShareUrl？后面的url 就要截取并拼上 from=share */
	private String mIsCleanUrl = "true";
	private String mTitleContent = "信则聆";

	// 微信仅分享分享
	private boolean mShareWeixinOnlyText = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mShareTextMsg = bundle.getString("text_content");
			mTitleContent = bundle.getString("title_content");
			mShareUrl = bundle.getString("url_content");
			mIsCleanUrl = bundle.getString("cleaned_url");
			mShareBitmapUrl = bundle.getString("image_url");
		}

		setContentView(CreateView());
		
		getActionBar().hide();
		View ll_share_icons = findViewById(R.id.ll_share_icons);

		ll_share_icons.setVisibility(View.VISIBLE);
		ll_share_icons.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_up));

	}

	public View CreateView() {

		SNSShareMenu mSNSShareMenu = new SNSShareMenu(this);
		mSNSShareMenu.setMsgText(mShareTextMsg);
		mSNSShareMenu.setShareUrl(mShareUrl);
		mSNSShareMenu.setIsCleanUrl(mIsCleanUrl);
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

}
