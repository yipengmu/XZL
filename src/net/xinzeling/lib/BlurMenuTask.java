package net.xinzeling.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

public class BlurMenuTask extends AsyncTask<Integer,Void, Void> {
	private View winView;
    private Bitmap drawCache;
    private Bitmap blurMask;
    private Runnable runnable;
    private View showView;
    private View clickView;
    private Bitmap clickCache;
    private Context ctx;
    
    public BlurMenuTask(Context ctx, View frameView, View toShow, View clickView, Runnable runnable){
    	winView= frameView;
    	this.ctx = ctx;
    	this.showView = toShow;
    	this.clickView = clickView;
    	this.runnable = runnable;
    }
    
    @Override
    protected void onPreExecute() {
        winView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        winView.setDrawingCacheEnabled(true);
        winView.buildDrawingCache();
        drawCache = winView.getDrawingCache();
        
        clickView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        clickView.setDrawingCacheEnabled(true);
        clickView.buildDrawingCache();
        clickCache = clickView.getDrawingCache();
    }
    
    @Override
	protected Void doInBackground(Integer... params) {
		Bitmap blurBitmap= Blur.apply(ctx, drawCache, 12);
		blurMask = Bitmap.createBitmap(winView.getWidth(), winView.getHeight(),  Bitmap.Config.ARGB_8888);
		Canvas ca = new Canvas(blurMask);
		Rect rect0 = new Rect(0,0, blurBitmap.getWidth(), blurBitmap.getHeight());
		Rect rect01 = new Rect(0,0,winView.getWidth(), winView.getHeight());
		ca.drawBitmap(blurBitmap, rect0, rect01,null);
		
		Rect rect = new Rect(0,0,clickCache.getWidth(),clickCache.getHeight());
		Rect rect2 = new Rect(clickView.getLeft(),clickView.getTop()+params[0], clickView.getRight(),
				clickView.getBottom()+params[0]);
		ca.drawBitmap(clickCache,rect,rect2, null);
		ca.save();
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Void result) {
		 winView.destroyDrawingCache();
         winView.setDrawingCacheEnabled(false);
         clickView.destroyDrawingCache();
         clickView.setDrawingCacheEnabled(false);
       
         showView.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), blurMask));
         showView.setVisibility(View.VISIBLE);
         if(runnable !=null){
        	 runnable.run();
         }
         drawCache.recycle();
	 }
}
