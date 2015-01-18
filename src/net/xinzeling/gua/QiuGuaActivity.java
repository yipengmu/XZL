package net.xinzeling.gua;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import net.xinzeling.MyApplication;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.lib.DateTime;
import net.xinzeling.lib.HttpCommon;
import net.xinzeling.model.GuaCntModel;
import net.xinzeling.model.GuaCntModel.GuaCnt;
import net.xinzeling.model.GuaModel;
import net.xinzeling.model.GuaModel.Gua;
import net.xinzeling.model.LunarModel;
import net.xinzeling.model.LunarModel.Lunar;
import net.xinzeling2.R;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QiuGuaActivity extends BaseActivity {
	public final static int REQUEST_PHOTO = 1;
	public final static int REQUEST_CAMERA = 2;
	private int mode;
	private int type;
	private boolean isNum = false;
	private boolean isPhoto = false;
	private Bitmap photo;
	private EditText name1Input;
	private EditText name2Input;
	private EditText num1Input;
	private EditText num2Input;
	private String title = "";
	private int rgb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		rgb = 0;
		super.onCreate(savedInstanceState);
		BlurBehind.getInstance().withAlpha(240).setBackground(this);
		Intent reqIntent = getIntent();
		mode = reqIntent.getIntExtra("mode", MyApplication.nav_gua_time);
		type = reqIntent.getIntExtra("type", 0);

		switch (mode) {
		case MyApplication.nav_gua_num:
			this.setContentView(R.layout.activity_gua_num);
			this.isNum = true;
			this.num1Input = (EditText) this.findViewById(R.id.input_num);
			this.num2Input = (EditText) this.findViewById(R.id.input_num_other);
			this.num2Input.setVisibility(View.VISIBLE);
			break;
		case MyApplication.nav_gua_photo:
			this.setContentView(R.layout.activity_gua_photo);
			this.findViewById(R.id.input_name_other).setVisibility(View.GONE);
			this.isPhoto = true;
			break;
		case MyApplication.nav_gua_time:
			this.setContentView(R.layout.activity_gua_time);
//			findViewById(R.id.ll_qiugua_name_cell).setVisibility(View.GONE);
			break;
		}
		this.name1Input = (EditText) this.findViewById(R.id.input_name);
		this.name2Input = (EditText) this.findViewById(R.id.input_name_other);
		if (!isDouble(type)) {
			name2Input.setVisibility(View.GONE);
		}

		setInGuaTitle(type);
		// FontManager.changeFonts((ViewGroup)AppBase.getRootView(QiuGuaActivity.this),
		// QiuGuaActivity.this);
	}

	private boolean isDouble(int type) {
		boolean ret = false;
		for (int i = 0; i < MyApplication.double_type_int.length; i++) {
			if (MyApplication.double_type_int[i] == type) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	private void setInGuaTitle(int type) {
		ImageView qiuguaIcon = (ImageView) findViewById(R.id.icon_qiugua);
		switch (type) {
		// 交易
		case 1:
			title = "交易";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_3);
			break;
		// 婚姻
		case 2:
			title = "婚姻";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_7);
			break;
		// 恋爱－恋人
		case 3:
			title = "恋爱·恋人";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_5);
			break;
		// 求财－财运－讨债
		case 4:
			title = "求财·财运·讨债";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_3);
			break;
		// 事业－工作
		case 5:
			title = "事业工作";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12);
			break;
		// 健康－疾病
		case 6:
			title = "健康疾病";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_10);
			break;
		// 豪宅－房屋
		case 7:
			title = "豪宅房屋";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_11);
			break;
		// 官司诉讼－申诉投诉
		case 8:
			title = "官司诉讼申诉投诉";
			break;
		// 其他－难分类－谋事
		case 9:
			title = "其他－难分类－谋事";
			break;
		// 近期财运（财运）
		case 10:
			title = "财运·近期财运";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_1_pure);
			break;
		// 求财之事（财运）
		case 11:
			title = "财运·求财之事";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_3_pure);
			break;
		// 借贷（财运）
		case 12:
			title = "财运·借贷";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_3_pure);
			break;
		// 讨债（财运）
		case 13:
			title = "财运·讨债";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_3_pure);
			break;
		// 学业
		case 14:
			title = "学业";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_13_pure);
			break;
		// 考试
		case 15:
			title = "考试";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_15_pure);
			break;
		// 今日吃啥
		case 16:
			title = "今日吃啥";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_1_pure);
			break;
		// 今日穿啥
		case 17:
			title = "今日穿啥";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_2_pure);
			break;
		// 公务出差（出行）
		case 18:
			title = "出行·公务出差";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_14_pure);
			break;
		// 旅游出游（出行）
		case 19:
			title = "出行·旅游出游";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_14_pure);
			break;
		// 每日出行（出行）
		case 20:
			title = "出行·每日出行";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_14_pure);
			break;
		// 单身－今日运程（单身）
		case 21:
			title = "单身·今日运程";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_8_pure);
			break;
		// 单身－近期桃花（单身）
		case 22:
			title = "单身·近期桃花";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_4_pure);
			break;
		// 有恋人－今日运程（恋爱）
		case 23:
			title = "有恋人·今日运程";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_5_pure);
			break;
		// 有恋人－恋爱状况（恋爱）
		case 24:
			title = "有恋人·恋爱状况";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_5_pure);
			break;
		// 有恋人－今日桃花（恋爱）
		case 25:
			title = "有恋人·今日桃花";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_5_pure);
			break;
		// 婚姻状况（已婚）
		case 26:
			title = "已婚·婚姻状况";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_7_pure);
			break;
		// 将来婚姻状况（未婚）
		case 27:
			title = "未婚·将来婚姻状况";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_8_pure);
			break;
		// 婚事情况（已婚）
		case 28:
			title = "已婚·婚事情况";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_7_pure);
			break;
		// 年内事业运（事业）
		case 29:
			title = "事业·年内事业运";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12_pure);
			break;
		// 创业选择（事业）
		case 30:
			title = "事业·创业选择";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12_pure);
			break;
		// 合作项目（事业）
		case 31:
			title = "事业·合作项目";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12_pure);
			break;
		// 谈判成败（事业）
		case 32:
			title = "事业·谈判成败";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12_pure);
			break;
		// 另谋高就（事业）
		case 33:
			title = "事业·另谋高就";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12_pure);
			break;
		// 今日运程（事业）
		case 34:
			title = "事业·今日运程";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_12_pure);
			break;
		// 去逛街（购物）
		case 35:
			title = "购物·去逛街";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_6_pure);
			break;
		// 网购（购物）
		case 36:
			title = "购物·网购";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_6_pure);
			break;
		// 买房（居家建设）
		case 37:
			title = "居家建设·买车";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_9_pure);
			break;
		// 买车（居家建设）
		case 38:
			title = "居家建设·买车";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_9_pure);
			break;
		// 装修（居家建设）
		case 39:
			title = "居家建设·今日运程";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_9_pure);
			break;
		// 今日运程（健康）
		case 40:
			title = "健康·今日运程";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_10_pure);
			break;
		// 近期状况（健康）
		case 41:
			title = "健康·近期状况";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_10_pure);
			break;
		// 病症状况（健康）
		case 42:
			title = "健康·病症状况";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_10_pure);
			break;
		// 家宅房屋（房屋）
		case 43:
			title = "房屋·家宅房屋";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_11_pure);
			break;
		// 出租房屋（房屋）
		case 44:
			title = "房屋·出租房屋";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_11_pure);
			break;
		// 求租房屋（房屋）
		case 45:
			title = "房屋·求租房屋";
			qiuguaIcon.setImageResource(R.drawable.icon_gua_11_pure);
			break;
		// 投诉申诉（其他）
		case 46:
			title = "其他·投诉申诉";
			break;
		// 其他－难分类（其他）
		case 47:
			title = "其他·难分类";
			break;
		}
		((TextView) findViewById(R.id.txt_qiugua)).setText(title);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_qiugua:
			if (!checkInputForm()) {
				toast("请检查输入内容");
				break;
			}
			new GuaPostTask().execute();
			break;
		case R.id.txt_photo:
		case R.id.btn_photo:
			Intent photoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(photoIntent, REQUEST_PHOTO);
			break;
		case R.id.txt_camera:
		case R.id.btn_camera:
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camera, REQUEST_CAMERA);
			break;
		case R.id.btn_home:
			MyApplication.sendBroadcastBackHomeFromThread();
			finish();
			break;
		}
	}

	public boolean checkInputForm() {
		if (num1Input == null || num2Input == null) {
			return false;
		}
		if (TextUtils.isEmpty(num1Input.getText().toString()) || TextUtils.isEmpty(num2Input.getText().toString())) {
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_PHOTO) {
			try {
				Uri originalUri = data.getData();
				photo = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
			photo = (Bitmap) data.getExtras().get("data");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class GuaPostTask extends AsyncTask<Void, Void, Boolean> {
		private GuaCnt gua_xz_info;
		private ProgressDialog progress;
		private String resMsg = "";
		private HashMap<String, Object> params = new HashMap<String, Object>();
		private String url = MyApplication.gua_time_url;
		private long guaid = 0;

		public GuaPostTask() {
			if (type != 0) {
				gua_xz_info = GuaCntModel.fetch(type);
			}

			params.put("usertoken", MyApplication.userToken);
			params.put("type", type);

			params.put("gender", MyApplication.gender);
			params.put("nameone", name1Input.getText().toString());
			params.put("nametwo", isDouble(type) ? name2Input.getText().toString() : "");
			try {
				initView(params);
			} catch (Exception e) {
				e.printStackTrace();
				toast("请检查输入内容是否为空");
			}
		}

		public void initView(HashMap<String, Object> params) throws Exception {
			if (isNum) {
				params.put("numone", Integer.valueOf(num1Input.getText().toString()));
				params.put("numtwo", Integer.valueOf(num2Input.getText().toString()));
				url = MyApplication.gua_double_num_url;
			} else if (isPhoto) {
				MyApplication.saveBitmap(photo);
				rgb = photo.getPixel(10, 10);
				rgb = rgb & 0x00ffffff;
				int red = rgb >> 16;
				int green = (rgb & 0xff00) >> 8;
				int blue = (rgb & 0xff);
				params.put("colorone", red + "," + green + "," + blue);// 颜色一
																		// 255,255,255
																		// 就用屏幕中间的那个像素点
				params.put("colortwo", "");// 颜色二不需要填
				url = MyApplication.gua_photo_url;
			}
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(QiuGuaActivity.this, null, "请求中……");
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			if (!gua_xz_info.isUsable)
				return false;
			Lunar lunar = LunarModel.fetchByDate(new Date());
			params.put("lunardate", lunar.lunarCalendar);
			try {
				JSONObject jsonResp;
				if (mode == MyApplication.nav_gua_photo) {
					jsonResp = HttpCommon.getPost(url, params, new File(MyApplication.photo_path));
				} else {
					jsonResp = HttpCommon.getGet(url, params);
				}
				int resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					// 在这里需要记录算卦次数及剩余次数及下次可进行时间戳
					GuaCnt guacnt = GuaCntModel.fetch(type);
					if (guacnt != null) {
						// 判断可算几次 已经算了几次 剩余几次 如果没有剩余计算下次可算的时间戳
						if (guacnt.cnt + 1 >= guacnt.all_cnt) {
							// 计算下次可算时间戳 计数器清零
							guacnt.cnt = 0;
							Date date = new Date();
							Calendar calendar = new GregorianCalendar();
							calendar.setTime(date);
							SimpleDateFormat s;
							String str;
							Date date1;
							switch (guacnt.xz_id) {
							case MyApplication.xz_id_day:
								calendar.add(calendar.DATE, 1);
								s = new SimpleDateFormat("yyyy-MM-dd");
								str = s.format(calendar.getTime());
								date1 = s.parse(str);
								guacnt.next_time = (int) (date1.getTime() / 1000);
								break;
							case MyApplication.xz_id_month:
								calendar.add(calendar.MONTH, 1);
								s = new SimpleDateFormat("yyyy-MM-01");
								str = s.format(calendar.getTime());
								date1 = s.parse(str);
								guacnt.next_time = (int) (date1.getTime() / 1000);
								break;
							case MyApplication.xz_id_year:
								calendar.add(calendar.YEAR, 1);
								s = new SimpleDateFormat("yyyy-01-01");
								str = s.format(calendar.getTime());
								date1 = s.parse(str);
								guacnt.next_time = (int) (date1.getTime() / 1000);
								break;
							}
						} else {
							// 计数器累加
							guacnt.cnt = guacnt.cnt + 1;
						}
						GuaCntModel.update(guacnt);
					}
					SimpleDateFormat sdFormat = new SimpleDateFormat("H:m", Locale.CHINA);

					Gua gua = new Gua();
					gua.type = jsonResp.getInt("type");
					gua.title = title;
					gua.date = lunar.date;
					gua.time = sdFormat.format(new Date());
					gua.doubleNumID = 0;// Integer.valueOf(jsonResp.getString("doubleNumID"));
					gua.body = jsonResp.getString("body");// 体卦
					gua.yao = jsonResp.getString("yao");// 爻
					gua.result = jsonResp.getString("result");// 卦象
					gua.inference = jsonResp.getString("inference");// 解卦
					guaid = GuaModel.save(gua);
					return true;
				} else {
					resMsg = jsonResp.getString("resMsg");
				}
			} catch (Exception e) {
				resMsg = "服务器链接失败";
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (progress != null) {
				progress.dismiss();
			}
			if (result) {
				Intent intent = new Intent(getApplicationContext(), JieGuaActivity.class);
				intent.putExtra("guaid", (int) guaid);
				startActivity(intent);
				finish();
			} else {
				if (gua_xz_info.isUsable) {
					Toast.makeText(getApplicationContext(), resMsg, Toast.LENGTH_LONG).show();
				} else {
					String d = DateTime.Timestamp2String(((long) gua_xz_info.next_time * 1000), "yyyy年MM月dd日");
					Toast.makeText(getApplicationContext(), "今天不可算了,下次可算时间：" + d, Toast.LENGTH_LONG).show();
				}
			}
		}
	}

}
