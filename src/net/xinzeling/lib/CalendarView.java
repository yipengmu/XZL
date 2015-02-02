package net.xinzeling.lib;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import net.xinzeling.MyApplication;
import net.xinzeling.model.ItemModel;
import net.xinzeling.model.ItemModel.DayItem;
import net.xinzeling.model.LunarModel;
import net.xinzeling.model.LunarModel.Lunar;
import net.xinzeling2.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 首页 日历控件 主类
 * 
 * 负责绘制日历控件
 * 
 * 
 * */
public class CalendarView extends TextView implements View.OnTouchListener {
	private Date today = new Date();
	private Date downDate; // 手指按下状态时临时日期
	private int downIndex; // 按下的格子索引
	private Calendar calendar;
	private LunarCalendar lunar;
	private Surface surface;
	private int[] dateList = new int[42]; // 日历显示数字
	private ArrayList<String[]> lunarList = new ArrayList<String[]>();//String[]  日期数字 节假日情况 当日记事情况 当日算卦情况
	private int startIndex, endIndex; // 当前显示的日历起始的索引
	private int todayIndex=-1;
	private int dateSize=0;
	private int dateRows=7;
	// 给控件设置监听事件
	private CalendarListener calendarListener;
	public int choiceMode =1;
	public boolean singleMonth=false;
	private SparseIntArray selectedDate = new SparseIntArray();
	private GestureDetector gestureDetector;
	private boolean isWeek=true;
	private int clickCnt;
	private long firClick,secClick;
	private Date from_title_txt_date;
	private int from_title_txt_date_fresh_cnt = 0;
	private float strokeSize = 0.4f;
	 
	public CalendarView(Context context, AttributeSet attrs){
		this(context, attrs,0);
	}

	public CalendarView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs,defStyle);
		calendar = Calendar.getInstance();
		lunar = new LunarCalendar(calendar);
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyle, 0);
		int borderColor = arr.getColor(R.styleable.CalendarView_grid_color, Color.parseColor("#CCCCCC"));
		int selectColor = arr.getColor(R.styleable.CalendarView_select_color, Color.parseColor("#99CCFF"));
		int pass_selectColor = arr.getColor(R.styleable.CalendarView_pass_select_color, Color.parseColor("#e6e5e6"));
		//默认文字颜色
		int defaultTextColor = arr.getColor(R.styleable.CalendarView_text_color, Color.parseColor("#393b31"));
		//选中文字颜色
		int selectTextColor = arr.getColor(R.styleable.CalendarView_select_text_color, getResources().getColor(R.color.common_focus_green));
		int notTextColor = arr.getColor(R.styleable.CalendarView_not_text_color, Color.parseColor("#AAAAAA"));
		isWeek = arr.getBoolean(R.styleable.CalendarView_is_week, false);
		arr.recycle();
		float borderWidth = 1;//getResources().getDisplayMetrics().density;
		surface = new Surface(getTextSize(),defaultTextColor,selectTextColor,notTextColor,borderWidth, borderColor,selectColor,pass_selectColor,context);
		setOnTouchListener(this);
		gestureDetector= new GestureDetector(context,new GestureListener());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		//selectedDate.clear();
		todayIndex=-1;
		if(isWeek){
			this.weekDateList();
		}else{
			this.monthDateList();
		}
		this.setMeasuredDimension(widthMeasureSpec, (int)Math.ceil(surface.boxHeight));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		surface.init(getWidth());
		canvas.drawPath(surface.boxPath, surface.borderPaint);
		if(todayIndex>0){
			boolean isOtherSelected = false;
			for(int i=0;i<selectedDate.size();i++){
				if(selectedDate.keyAt(i)!=todayIndex){
					isOtherSelected = true;break;
				}
			}
			if(isOtherSelected){
				setNewCellBg(canvas,todayIndex,surface.cellPassSelectedColor,true);
			}else{
				setNewCellBg(canvas,todayIndex,surface.cellSelectedColor,true);
			}
		}
		for(int i=0;i<selectedDate.size();i++){
			if(i!=todayIndex){
				setNewCellBg(canvas, selectedDate.keyAt(i), surface.cellSelectedColor,true);
			}
		}

		for (int i = 0; i< dateSize; i++) {
			if (isLastMonth(i)||isNextMonth(i)) {
				if(!singleMonth){
					setNewCellBg(canvas,i,0,false);
					surface.drawCellText(canvas, i, String.valueOf(dateList[i]), lunarList.get(i)[0], surface.notColor);
				}
			} else{
				setNewCellBg(canvas,i,0,false);
				if(todayIndex==i){
					boolean isOtherSelected = false;
					for(int j=0;j<selectedDate.size();j++){
						if(selectedDate.keyAt(j)!=i){
							isOtherSelected = true;break;
						}
					}
					if(isOtherSelected){
						surface.drawCellText(canvas, i, String.valueOf(dateList[i]), lunarList.get(i)[0], surface.textColor);
					}else{
						//fix 2月头月 颜色不对问题
						surface.drawCellText(canvas, i, String.valueOf(dateList[i]), lunarList.get(i)[0], surface.textColor);
//						surface.drawCellText(canvas, i, String.valueOf(dateList[i]), lunarList.get(i)[0], surface.selectTextColor);
					}
				}else{
					boolean isSelected = false;
					for(int j=0;j<selectedDate.size();j++){
						if(selectedDate.keyAt(j)==i){
							isSelected = true;break;
						}
					}
					//是否选中态，开始绘制每一天的cell格子
					if(isSelected){
						surface.drawCellText(canvas, i, String.valueOf(dateList[i]), lunarList.get(i)[0], surface.selectTextColor);
					}else{
						surface.drawCellText(canvas, i, String.valueOf(dateList[i]), lunarList.get(i)[0], surface.textColor);
					}
				}
			}
		}
	}

	private void setNewCellBg(Canvas canvas,int index,int color,boolean isredrawbg){
		int oth = 0;
		if(index == lunarList.size()){
			return ;
		}
		
		if("1".equals(lunarList.get(index)[1])){
			oth = oth | 4;
		}
		if("1".equals(lunarList.get(index)[2])){
			oth = oth | 2;
		}
		if("1".equals(lunarList.get(index)[3])){
			oth = oth | 1;
		}
//		oth = 7;
		Log.d("cell dian", "cell dian :" + lunarList.get(index)[0]+" "+lunarList.get(index)[1]+" "+lunarList.get(index)[2]+" "+lunarList.get(index)[3]);
		
		if(oth==0&&!isredrawbg)return;
		surface.drawCellBg(canvas, index, color, oth, isredrawbg);
	}

	private void monthDateList(){
		Date date;
		if(from_title_txt_date!=null){
			date = from_title_txt_date;
		}else{
			date = calendar.getTime();
		}
		dateSize=0;
		todayIndex=-1;
		lunarList.clear();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, -dayInWeek+1);
		//第一天
		int startYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(calendar.getTime()));
		
		//加50天,一会减50天
		calendar.add(Calendar.DAY_OF_MONTH, +50);
		//最后一天
		int endYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(calendar.getTime()));
		//减回去
		calendar.add(Calendar.DAY_OF_MONTH, -50);
		
		//取得大概的数据信息
		HashMap<String,DayItem> it = ItemModel.fetchDayFlag(startYYYYMMDD,endYYYYMMDD);
		for(int i=0;i<dayInWeek-1;i++){
			Date date_1 = calendar.getTime();
			Lunar l_info = LunarModel.fetchByDate(date_1);
			String down_text = "";
			//国家节日
			if(!"".equals(l_info.globalHoliday)){
				down_text = l_info.globalHoliday;
			}//农历节气
			else if(!"".equals(l_info.holiday)){
				down_text = l_info.holiday;
			}
			//西方节日
			else if(!"".equals(l_info.weekDayHoliday)){
				down_text = l_info.weekDayHoliday;				
			}
			//农历日期
			else{
				down_text = lunar.caculate().getChinaDay();
			}
			String isNote = "0";
			String isGua = "0";
			String sel_date = DateTime.getTodayYmd(date_1);
			if(it.containsKey(sel_date)){
				isNote = it.get(sel_date).isNote?"1":"0";
				isGua = it.get(sel_date).isGua?"1":"0";
			}
			String[] day_info = new String[]{down_text,"0",isNote,isGua};
			day_info[1] = "".equals(l_info.globalHoliday)?"0":"1";
			//TODO
			lunarList.add(day_info);
			dateList[dateSize] = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			dateSize++;
		}
		startIndex=dateSize;
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		endIndex=dateSize+monthDay;
		dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);	
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		for(int i=0;i<monthDay+(7-dayInWeek);i++){
			Date date_1 = calendar.getTime();
			Lunar l_info = LunarModel.fetchByDate(date_1);
			String down_text = "";
			//国家节日
			if(!"".equals(l_info.globalHoliday)){
				down_text = l_info.globalHoliday;
			}//农历节气
			else if(!"".equals(l_info.holiday)){
				down_text = l_info.holiday;
			}
			//西方节日
			else if(!"".equals(l_info.weekDayHoliday)){
				down_text = l_info.weekDayHoliday;				
			}
			//农历日期
			else{
				down_text = lunar.caculate().getChinaDay();
			}
			String isNote = "0";
			String isGua = "0";
			String sel_date = DateTime.getTodayYmd(date_1);
			if(it.containsKey(sel_date)){
				isNote = it.get(sel_date).isNote?"1":"0";
				isGua = it.get(sel_date).isGua?"1":"0";
			}
			String[] day_info = new String[]{down_text,"0",isNote,isGua};
			day_info[1] = "".equals(l_info.globalHoliday)?"0":"1";
			//TODO
			lunarList.add(day_info);
			dateList[dateSize]= calendar.get(Calendar.DAY_OF_MONTH);
			if(calendar.get(Calendar.MONTH)==today.getMonth() && calendar.get(Calendar.DAY_OF_MONTH) == today.getDate()){
				todayIndex = dateSize;
				selectedDate.put(dateSize, calendar.get(Calendar.DAY_OF_MONTH));
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			dateSize++;
		}
		dateRows =dateSize/7;
		calendar.setTime(date);
		if(from_title_txt_date!=null){
			from_title_txt_date_fresh_cnt++;
			selectedDate.put(getIndexByMonthDay(calendar.get(Calendar.DAY_OF_MONTH)), calendar.get(Calendar.DAY_OF_MONTH));
			if(from_title_txt_date_fresh_cnt==2){
				from_title_txt_date = null;
				from_title_txt_date_fresh_cnt = 0;
			}
		}
	}

	private void weekDateList(){
		Date date;
		if(from_title_txt_date!=null){
			date = from_title_txt_date;
		}else{
			date = calendar.getTime();
		}
		this.dateRows=1;
		this.dateSize=7;
		this.startIndex=0;
		this.endIndex=7;
		lunarList.clear();
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_MONTH, 1-dayInWeek);
		
		//第一天
		int startYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(calendar.getTime()));
		
		//加10天,一会减10天
		calendar.add(Calendar.DAY_OF_MONTH, +10);
		//最后一天
		int endYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(calendar.getTime()));
		//减回去
		calendar.add(Calendar.DAY_OF_MONTH, -10);
		//取得大概的数据信息
		HashMap<String,DayItem> it = ItemModel.fetchDayFlag(startYYYYMMDD,endYYYYMMDD);
		for(int i=0;i<7;i++){
			Date date_1 = calendar.getTime();
			Lunar l_info = LunarModel.fetchByDate(date_1);
			String down_text = "";
			//国家节日
			if(!"".equals(l_info.globalHoliday)){
				down_text = l_info.globalHoliday;
			}//农历节气
			else if(!"".equals(l_info.holiday)){
				down_text = l_info.holiday;
			}
			//西方节日
			else if(!"".equals(l_info.weekDayHoliday)){
				down_text = l_info.weekDayHoliday;				
			}
			//农历日期
			else{
				down_text = lunar.caculate().getChinaDay();
			}
			String isNote = "0";
			String isGua = "0";
			String sel_date = DateTime.getTodayYmd(calendar.getTime());
			if(it.containsKey(sel_date)){
				isNote = it.get(sel_date).isNote?"1":"0";
				isGua = it.get(sel_date).isGua?"1":"0";
			}
			String[] day_info = new String[]{down_text,"0",isNote,isGua};
			day_info[1] = "".equals(l_info.globalHoliday)?"0":"1";
			//TODO
			lunarList.add(day_info);
			dateList[i]= calendar.get(Calendar.DAY_OF_MONTH);
			if(calendar.get(Calendar.MONTH)==today.getMonth() && calendar.get(Calendar.DAY_OF_MONTH) == today.getDate()){
				todayIndex = i;
				selectedDate.put(i, calendar.get(Calendar.DAY_OF_MONTH));
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.setTime(date);
		if(from_title_txt_date!=null){
			from_title_txt_date_fresh_cnt++;
			selectedDate.put(getIndexByMonthDay(calendar.get(Calendar.DAY_OF_MONTH)), calendar.get(Calendar.DAY_OF_MONTH));
			if(from_title_txt_date_fresh_cnt==2){
				from_title_txt_date = null;
				from_title_txt_date_fresh_cnt = 0;
			}
		}
	}

	private boolean isLastMonth(int index) {
		return index < startIndex;
	}

	private boolean isNextMonth(int index) {
		return index >= endIndex;
	}

	private int getXByIndex(int i) {
		return i % 7 + 1; // 1 2 3 4 5 6 7
	}

	private int getYByIndex(int i) {
		return i / 7 + 1; // 1 2 3 4 5 6
	}	

	//根据坐标获得选中时间
	private void setSelectedDateByCoor(float x, float y) {
		int m = (int) (Math.floor(x / surface.cellWidth) + 1);
		int n = (int) (Math.floor((y )/ Float.valueOf(surface.cellHeight)) + 1);
		int index = (n - 1) * 7 + m - 1;
		if(!isLastMonth(index)&& !isNextMonth(index)){
			downIndex = index;
			calendar.set(Calendar.DAY_OF_MONTH, dateList[downIndex]);
			downDate = calendar.getTime();
			invalidate();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		boolean isDoubleClick = false;
		if(MotionEvent.ACTION_DOWN == event.getAction()){  
			clickCnt++;  
			if(clickCnt == 1){  
				firClick = System.currentTimeMillis();  
			} else if (clickCnt == 2){  
				secClick = System.currentTimeMillis();  
				if(secClick - firClick < 1000){  
					//双击事件  
					isDoubleClick = true;
					setSelectedDateByCoor(event.getX(), event.getY());
					if(downDate!=null){
						if(calendarListener!=null){
							calendarListener.onDoubleClick(downDate);
						}
					}
				}  
				clickCnt = 0;  
				firClick = 0;  
				secClick = 0;   
			} 
		}  
		if(!isDoubleClick){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setSelectedDateByCoor(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_UP:
				if (downDate != null) {
					if(calendarListener !=null){
						calendarListener.onDateSelect(downDate,downDate.equals(today));
					}
					downDate = null;
					if(choiceMode==1){
						selectedDate.clear();
					}
					selectedDate.put(downIndex, dateList[downIndex]);
					invalidate();
				}
				break;
			}
		}
		gestureDetector.onTouchEvent(event);
		return true;
	}

	public interface CalendarListener{
		public void onMonthChange(Date date, boolean isCurrent);	
		public void onDateSelect(Date date,boolean isCurrent);
		public void onDoubleClick(Date date);
	}

	public void setCalendarListener(CalendarListener listener){
		this.calendarListener=listener;
	}

	public int[] getSelect(){
		int[] ret = new int[selectedDate.size()];
		for(int i=0;i<selectedDate.size();i++){
			ret[i] = selectedDate.valueAt(i);
		}
		return ret;
	}

	public void setSelect(int[] dates){
		selectedDate.clear();
		for(int date:dates){
			for(int i=startIndex;i<endIndex;i++){
				if(date ==dateList[i]){
					selectedDate.put(i, date);
				}
			}
		}
//		this.invalidate();
	}

	public int getYear(){
		return calendar.get(Calendar.YEAR); 
	}

	public int getMonth(){
		return calendar.get(Calendar.MONTH)+1;
	}

	public Date getDate(){
		return calendar.getTime();
	}

	public void goBackToday(){
		calendar.setTime(today);
		requestLayout();
	}

	public void refreshByDate(String newDay) throws ParseException {
		from_title_txt_date = DateTime.String2Date(newDay,"yyyy-MM-dd");
		requestLayout();
	}
	
	private int getIndexByMonthDay(int MonthDay){
		int ret = 0;
		for(int i=startIndex;i<endIndex;i++){
			if(dateList[i]==MonthDay){
				return i;
			}
		}
		return ret;
	}

	private class Surface {
		private float borderWidth;
		private float cellWidth; // 日期方框宽度
		private float cellHeight; // 日期方框高度
		private int textColor ;//默认
		private int notColor;//非本月
		private int selectTextColor;//选中

		private float textSize;
		private float fontHeight;
		private float minHeight;
		
		public float boxHeight;
		public int cellSelectedColor;
		public int cellPassSelectedColor;
		
		private Paint borderPaint;
		private Paint datePaint;
		private Paint cellBgPaint;

		private Path boxPath; // 边框路径
		//常量参数
		private float textLineSpaceCellHeightBegin = 0.618f;
		//字体高宽比
		private final static float fontHeightWidthRate = 1.07f;
		//推荐宽高比,高度会计算按最大的来
		private final static float cellWidthHeightRate = 1.0f;
		public Surface(float textSize,int textColor,int selectTextColor,int notColor, float borderWidth, int borderColor, int cellSelectedColor,int cellPassSelectedColor,Context context){
			this.textColor=textColor;
			this.selectTextColor = selectTextColor;
			this.notColor = notColor;
			this.textSize = 0.8f*textSize;
			
			this.borderWidth=borderWidth;
			this.cellSelectedColor = cellSelectedColor;
			this.cellPassSelectedColor = cellPassSelectedColor;
			
			borderPaint = new Paint();
			borderPaint.setColor(borderColor);
			borderPaint.setStyle(Paint.Style.STROKE);
			borderPaint.setStrokeWidth(borderWidth);

			datePaint = new Paint();
			datePaint.setColor(textColor);
			datePaint.setAntiAlias(true);
			datePaint.setTextSize(this.textSize);

			fontHeight = datePaint.measureText("中")*fontHeightWidthRate;
			minHeight = fontHeight*(2.0f + textLineSpaceCellHeightBegin*2.0f);//中 该字体的高宽比 2倍

			init_cellHeight((int)MyApplication.getWidthHeight()[0]);
			
			datePaint.setFakeBoldText(false);
			
			cellBgPaint = new Paint();
			cellBgPaint.setAntiAlias(true);
			cellBgPaint.setStyle(Paint.Style.FILL);
			boxPath = new Path();
		}

		private void init_cellHeight(int width){
			boxHeight = 0.0f;
			cellWidth = width / 7f;
			//取大者
			if((cellWidth*cellWidthHeightRate)>minHeight){
				cellHeight = cellWidth*cellWidthHeightRate;
				textLineSpaceCellHeightBegin = cellHeight/2.0f/fontHeight - 1;
			}else{
				cellHeight = minHeight;
			}
			boxHeight = dateRows * cellHeight;			
		}
		
		public void init(int width) {
			init_cellHeight(width);
			boxPath.reset();
			boxPath.rLineTo(width, 0);
			//画竖
			for (int i = 1; i <= 6; i++) {
				boxPath.moveTo(i * cellWidth, 0);
				boxPath.rLineTo(0, boxHeight );
			}
			//画横
			for(int i=0; i<= dateRows;i++){
				boxPath.moveTo(0,   i * cellHeight);
				boxPath.rLineTo(width, 0);
			}
		}

		//绘制单元格背景
		//增加微图标显示0x b000 b假日｜左下脚｜右下脚
		void drawCellBg(Canvas canvas, int index, int color, int oth,boolean isredrawbg) {
			int x = getXByIndex(index);
			int y = getYByIndex(index);
			float left = cellWidth * (x - 1) + borderWidth;
			float top =  (y - 1)* cellHeight + borderWidth;
			if(isredrawbg){
				cellBgPaint.setColor(color);
				canvas.drawRect(left, top, left + cellWidth- borderWidth, top + cellHeight- borderWidth, cellBgPaint);
			}
			//b100
			if((oth & 4)>0){
				//右上角 假日
				//float n_left = left + cellWidth - borderWidth - 10;
				//cellBgPaint.setColor(Color.parseColor("#AA0000"));
				//canvas.drawRect(n_left, top, n_left + 10, top + 10, cellBgPaint);
				Bitmap iconbit = MyApplication.getResIcon(R.drawable.dr_7_4);
				canvas.drawBitmap(iconbit, left+cellWidth-borderWidth - iconbit.getWidth(), top -  borderWidth,cellBgPaint);
			}
			//b10
			if((oth & 2)>0){
				//左下角 记事
				//float n_top = top + cellHeight - borderWidth - 10;
				//cellBgPaint.setColor(Color.parseColor("#dd0000"));
				//canvas.drawRect(left, n_top, left + 10, n_top + 10, cellBgPaint);
				Bitmap iconbit = MyApplication.getResIcon(R.drawable.dr_7_2);
				canvas.drawBitmap(iconbit, left + 0.2f*iconbit.getWidth() - borderWidth, top + cellHeight - borderWidth - 1.4f* iconbit.getHeight(), cellBgPaint);
			}
			if((oth & 1)>0){
				//右下角
				//float n_left = left + cellWidth - borderWidth - 10;
				//float n_top = top + cellHeight - borderWidth - 10;
				//cellBgPaint.setColor(Color.parseColor("#00AA00"));
				//canvas.drawRect(n_left, n_top, n_left + 10, n_top + 10, cellBgPaint);
				//右下角 算卦 ，绘制当前有算卦的 小八卦
				Bitmap iconbit = MyApplication.getResIcon(R.drawable.dr_7_1);
				canvas.drawBitmap(iconbit, left + cellWidth - 1.4f*iconbit.getWidth() - borderWidth , top + cellHeight - borderWidth - 1.4f*iconbit.getHeight(), cellBgPaint);
			}
		}

		//绘制单元格字符,即公历日期 和 农历日期
		void drawCellText(Canvas canvas, int index, String text,String lunar, int color) {
			int x = getXByIndex(index);
			int y = getYByIndex(index);
			datePaint.setColor(color);
			
			datePaint.setTextSize(2*textSize);
			//公历日期
			float cellY =  (y - 1)* cellHeight + fontHeight * (textLineSpaceCellHeightBegin+1.0f);//字是以中文点为计算点的
			float cellX = (cellWidth * (x - 1))+ (cellWidth - datePaint.measureText(text))/ 2f;
			canvas.drawText(text, cellX, cellY, datePaint);
//			makeBorderDrawText(canvas, text, cellX, cellY, datePaint, color,2*textSize);
			
			// 农历日期
			float topMagin = 12f;
			datePaint.setTextSize(textSize);
			cellY = (y - 1)* cellHeight + fontHeight*(textLineSpaceCellHeightBegin+1.0f+1.0f ) + topMagin;
			cellX = (cellWidth * (x - 1))+ (cellWidth - datePaint.measureText(lunar))/ 2f;
			canvas.drawText(lunar, cellX, cellY, datePaint);
//			makeBorderDrawText(canvas, lunar, cellX, cellY, datePaint, color,textSize);
		}
	}

	private class GestureListener extends SimpleOnGestureListener {

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			if (e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 200) {  
				selectedDate.clear();
				if(isWeek){
					calendar.add(Calendar.DAY_OF_MONTH, +7);
				}else{
					calendar.add(Calendar.MONTH, +1);
				}
				requestLayout();
				if(calendarListener !=null){
					boolean isToday = false;
					if(calendar.getTime().getMonth()==today.getMonth()){
						isToday = true;
					}
					calendarListener.onMonthChange(calendar.getTime(),isToday);
				}
			} else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 200) {  
				selectedDate.clear();
				if(isWeek){
					calendar.add(Calendar.DAY_OF_MONTH, -7);
				}else{
					calendar.add(Calendar.MONTH, -1);
				}
				requestLayout();
				if(calendarListener !=null){
					boolean isToday = false;
					if(calendar.getTime().getMonth()==today.getMonth()){
						isToday = true;
					}
					calendarListener.onMonthChange(calendar.getTime(),isToday);
				}
			}  
			return true;  
		}
	}
	
	public void makeBorderDrawText(Canvas canvas, String text, float x, float y, Paint mPaint, int originalColor, float originalColorFontSize) {
		
		Paint borderP = new Paint();
		borderP.setAntiAlias(true);
		borderP.setStyle(Paint.Style.FILL);
		borderP.setColor(getResources().getColor(R.color.common_light_green));
		borderP.setTextSize(originalColorFontSize);
		
		canvas.drawText(text, x, y - strokeSize, borderP);
		canvas.drawText(text, x, y + strokeSize, borderP);
		canvas.drawText(text, x + strokeSize, y, borderP);
		canvas.drawText(text, x + strokeSize, y + strokeSize, borderP);
		canvas.drawText(text, x + strokeSize, y - strokeSize, borderP);
		canvas.drawText(text, x - strokeSize, y, borderP);
		canvas.drawText(text, x - strokeSize, y + strokeSize, borderP);
		canvas.drawText(text, x - strokeSize, y - strokeSize, borderP);
		
		canvas.drawText(text, x, y, mPaint);
		
		
	}

}