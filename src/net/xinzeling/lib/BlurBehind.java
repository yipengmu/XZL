package net.xinzeling.lib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

public class BlurBehind {

    private static final int CONSTANT_BLUR_RADIUS = 12;
    private static final int CONSTANT_DEFAULT_ALPHA = 100;

    private static  Bitmap mImageCache;
    private static CacheBlurBehindAndExecuteTask cacheBlurBehindAndExecuteTask;

    private int mAlpha = CONSTANT_DEFAULT_ALPHA;
    private int mFilterColor = -1;

    private enum State {READY,EXECUTING}

    private State mState = State.READY;

    private static BlurBehind mInstance;

    public static BlurBehind getInstance() {
        if (mInstance == null) {
            mInstance = new BlurBehind();
        }
        return mInstance;
    }

    public void execute(Activity activity, Runnable runnable) {
        if (mState.equals(State.READY)) {
            mState = State.EXECUTING;
            cacheBlurBehindAndExecuteTask = new CacheBlurBehindAndExecuteTask(activity, runnable);
            cacheBlurBehindAndExecuteTask.execute();
        }
    }

    public BlurBehind withAlpha(int alpha) {
        this.mAlpha = alpha;
        return this;
    }

    public BlurBehind withFilterColor(int filterColor) {
        this.mFilterColor = filterColor;
        return this;
    }

    public void setBackground(Activity activity) {
        if (mImageCache !=null) {
            BitmapDrawable bd = new BitmapDrawable(activity.getResources(), mImageCache);
            bd.setAlpha(mAlpha);
            if (mFilterColor != -1) {
                bd.setColorFilter(mFilterColor, PorterDuff.Mode.DST_ATOP);
            }
            activity.getWindow().setBackgroundDrawable(bd);
            cacheBlurBehindAndExecuteTask = null;
        }
    }

    private class CacheBlurBehindAndExecuteTask extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        private Runnable runnable;

        private View decorView;
        private Bitmap image;

        public CacheBlurBehindAndExecuteTask(Activity a, Runnable r) {
            activity = a;
            runnable = r;
        }

        @Override
        protected void onPreExecute() {
//            decorView = activity.getWindow().getDecorView();
//            decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
//            decorView.setDrawingCacheEnabled(true);
//            decorView.buildDrawingCache();
//            image = decorView.getDrawingCache();
        }

        @Override
        protected Void doInBackground(Void... params) {
//        	mImageCache = Blur.apply(activity, image, CONSTANT_BLUR_RADIUS);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            decorView.destroyDrawingCache();
//            decorView.setDrawingCacheEnabled(false);
//            activity = null;
            runnable.run();
            mState = State.READY;
        }
    }
}
