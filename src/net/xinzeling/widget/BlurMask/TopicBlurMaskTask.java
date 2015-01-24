package net.xinzeling.widget.BlurMask;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;

public  class TopicBlurMaskTask extends AsyncTask<Void,Void,Void>{
	private View winView;
    private Bitmap drawCache;
    private Bitmap blurMask;
    private Runnable runnable;
    private View showView;
    private Context ctx;
    private Fragment mFragment;
    
    public TopicBlurMaskTask(Context c ,Fragment f ,Runnable r) {
    	mFragment = f;
    	runnable = r;
    	ctx =c;
    }
    
	@Override
	protected Void doInBackground(Void... params) {
		return null;
	}
	
	@Override
	protected void onPostExecute(Void aVoid) {
         if(runnable !=null){
        	 runnable.run();
         }
         mFragment.getView().setBackgroundColor(Color.parseColor("#DF000000"));
         mFragment.getView().setVisibility(View.VISIBLE);
	 }
}

