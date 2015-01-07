/**
 * 
 */
package net.xinzeling.share;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.xinzeling.share.weixin.WeixinManager;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SNSShareMenu {

	private Context mContext;
	private Uri mPicUri;

	private PopupWindow mMenu;
	public View mMenuRootView;
	private View view_share_pop_dismiss;
	private GridView gridview_share_main_content;
	private ShareGridViewAdapter mShareGridViewAdapter;
	private CloseShareViewListener mCloseShareViewListener = null;

	// sns app list
	private ArrayList<ShareAppInfo> mShareAppList;
	private String mSubject;
	private String mShareMsgContent = "信则聆";
	private String mShareUrl = "";
	/** mIsCleanUrl == false 代表 不需要url截取 */
	private boolean mIsCleanUrl = true;
	private Bitmap mShareBitmap = null;
	private String mPicUrl = null;
	private String mTitleTag;

	public SNSShareMenu(Context context) {
		mContext = context;

		findViews();
		initShareitemList();
	}

	public void setMsgText(String msgText) {
		if (TextUtils.isEmpty(msgText)) {
			return;
		}
		mShareMsgContent = msgText;
	}

	public void setShareUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		mShareUrl = url;
	}

	public void setIsCleanUrl(String isClean) {
		if (TextUtils.isEmpty(isClean)) {
			return;
		}
		if ("false".equals(isClean)) {
			mIsCleanUrl = false;
		}
	}

	/**
	 * @param mShareBitmapUrl
	 */
	public void setShareBitmapUrl(final String bitmapUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mShareBitmap = Utils.getBitmapFromURL(bitmapUrl);
				if (mShareBitmap == null) {
					mShareBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
				}
			}
		}).start();

		mPicUrl = bitmapUrl;
	}

	public SNSShareMenu(Activity context, String picDir, String msgText) {
		mContext = context;

		findViews();
		initShareitemList();
	}

	/**
	 * 建立绑定UI
	 * 
	 * */
	private void findViews() {
		mMenuRootView = LayoutInflater.from(mContext).inflate(R.layout.share_main, null);

		// 绑定控件
		gridview_share_main_content = (GridView) mMenuRootView.findViewById(R.id.gridview_share_main_content);
		View ll_share_icons = mMenuRootView.findViewById(R.id.ll_share_icons);
		view_share_pop_dismiss = mMenuRootView.findViewById(R.id.view_share_pop_dismiss);

		// 初始化popwindow
		mMenu = new PopupWindow(mContext);
		mMenu.setContentView(mMenuRootView);
		mMenu.setWidth(LayoutParams.MATCH_PARENT);
		mMenu.setHeight(LayoutParams.MATCH_PARENT);
		mMenu.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.half_transparent));
		mMenu.setBackgroundDrawable(dw);

		ll_share_icons.setVisibility(View.GONE);
		// 点击分享上方灰色区域，dismiss popwindow
		view_share_pop_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mCloseShareViewListener != null) {
					mCloseShareViewListener.closeShareView();
					mMenu.dismiss();
				}
			}
		});
	}

	/**
	 * 初始化需要的app item List
	 */
	private void initShareitemList() {
		mShareGridViewAdapter = new ShareGridViewAdapter(mContext);
		mShareAppList = getAppArrList(getShareIntent());
		mShareGridViewAdapter.setData(mShareAppList);
		gridview_share_main_content.setAdapter(mShareGridViewAdapter);
		gridview_share_main_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				appItemClick(position);
			}
		});
	}

	/**
	 * @param position
	 *            点击分享gridView item处理事件
	 */
	protected void appItemClick(int position) {

		mMenu.dismiss();
		ShareAppInfo info = mShareAppList.get(position);
//		if (shareDirectly(info, mShareMsgContent + " " + cleanedUrl(mShareUrl))) {
//			// 短信，复制
//			return;
//		}
		
		if(info.icon == R.drawable.btn_weixin_friend_timeline_dark){
			new WeixinManager().sendReq(mShareMsgContent, true);
		}else if(info.icon == R.drawable.btn_weibo_im_dark){
			new WeixinManager().sendReq(mShareMsgContent, false);
		}else{
			// 非直接分享 ,intent Action 方式
			gotoShare(info, null, mShareMsgContent, mShareUrl);
		}

	}

	private void gotoShare(final ShareAppInfo info, String picDir, String msgText, String url) {

		Intent intent = info.intent;
		intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
		intent.putExtra(Intent.EXTRA_TEXT, msgText + " " + cleanedUrl(mShareUrl));

		String uriPath = handleBitmap2UriPathString(mShareBitmap);
		if (!TextUtils.isEmpty(uriPath)) {
			mPicUri = Uri.fromFile(new File(uriPath));
		}

		/** 来往客户端bug，不能同时分享 图片+文字，需要接入稳定版 来往sdk解决该问题 */
		if (mPicUri != null) {
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, mPicUri);
		} else {
			intent.setType("text/plain");
		}
		try {
			mContext.startActivity(info.intent);
		} catch (Exception e) {
		}
	}

	private String handleBitmap2UriPathString(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		File dir = new File(mContext.getExternalCacheDir() + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File f = new File(dir.getAbsolutePath() + "trip_pic_tmp" + System.currentTimeMillis() + ".png");
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f.getAbsolutePath();
	}

	private String cleanedUrl(String url) {
		if (mIsCleanUrl == false) {
			// 不需要url截取
			return url;
		}

		int i = url.indexOf("?");
		if (i > -1) {
			return url.substring(0, i) + "?from=share";
		} else {
			return url + "?from=share";
		}
	}

	/**
	 * @return
	 */
	private Intent getShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
		intent.putExtra(Intent.EXTRA_TEXT, "text");
		intent.setType("image/*");
		if (mPicUri != null) {
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, mPicUri);
		} else {
			intent.setType("text/plain");
		}
		return intent;
	}

	/**
	 * @return 希望的app list
	 */
	private ArrayList<ShareAppInfo> getAppArrList(Intent shareIntent) {
		List<ResolveInfo> list = new ArrayList<ResolveInfo>();
		ArrayList<ShareAppInfo> nameList = new ArrayList<ShareAppInfo>();
		PackageManager pm = mContext.getPackageManager();
		list = pm.queryIntentActivities(shareIntent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		ShareAppInfo[] array = new ShareAppInfo[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ShareAppInfo item = SNSPluginUtil.getAppName(list.get(i).activityInfo.packageName, list.get(i).activityInfo.name);
			if (item != null) {
				item.intent = new Intent(shareIntent);
				item.intent.setComponent(new ComponentName(list.get(i).activityInfo.packageName, list.get(i).activityInfo.name));
				array[i] = item;
			}
		}

		for (int i = 0; i < array.length; i++) {
			if (array[i] != null && checkInShareList(array[i].icon)) {
				nameList.add(array[i]);
				continue;
			}
		}

		nameList.add(new ShareAppInfo(R.drawable.btn_weibo_im_dark,"微信"));
		nameList.add(new ShareAppInfo(R.drawable.btn_weixin_friend_timeline_dark,"朋友圈"));
		
		return nameList;
	}

	private boolean checkInShareList(int iconId) {
		boolean checkSupport = false;
		int[] appSupportArr = { ShareAppInfo.Target_Weixin_Friend, ShareAppInfo.Target_Weixin_TimeLine, ShareAppInfo.Target_Sina_Weibo, ShareAppInfo.Target_QZone };

		for (int i = 0; i < appSupportArr.length; i++) {
			if (appSupportArr[i] == iconId) {
				checkSupport = true;
				break;
			}
		}
		return checkSupport;
	}

	public void setCloseListener(CloseShareViewListener lis) {
		this.mCloseShareViewListener = lis;
	}

	public interface CloseShareViewListener {
		public void closeShareView();
	}

	public interface UsertrackListener {
		public void onUTButton(String buttonName);
	}

	public void setTitleContent(String title) {
		if (TextUtils.isEmpty(title)) {
			return;
		}
		mTitleTag = title;
	}
}
