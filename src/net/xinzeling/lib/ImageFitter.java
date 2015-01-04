
package net.xinzeling.lib;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Gravity;

class ImageFitter {
    private int gravity;
    private int fitMode;

    ImageFitter(int mode, int gravity) {
        this.fitMode = mode;
        this.gravity = gravity;
    }

    Rect fit(Bitmap bmp, int viewWidth, int viewHeight) {
        switch (fitMode) {
        case ImageLayout.FIT_VERTICAL:
            return fitVertical(bmp, viewWidth, viewHeight);
        case ImageLayout.FIT_HORIZONTAL:
            return fitHorizontal(bmp, viewWidth, viewHeight);
        case ImageLayout.FIT_BOTH:
            return fitBoth(bmp, viewWidth, viewHeight);
        default:
            return fitAuto(bmp, viewWidth, viewHeight);
        }
    }

    private Rect fitHorizontal(Bitmap bmp, int w, int h) {
        float bitmapAspectRatio = computeBitmapAspectRatio(bmp);
        return fitHorizontal(w, h, bitmapAspectRatio);
    }

    private Rect fitVertical(Bitmap bmp, int w, int h) {
        float bitmapAspectRatio = computeBitmapAspectRatio(bmp);
        return fitVertical(w, h, bitmapAspectRatio);
    }

    private Rect fitBoth(Bitmap bmp, int w, int h) {
        float bitmapAspectRatio = computeBitmapAspectRatio(bmp);
        float viewAspectRatio = w / (float) h;
        if (bitmapAspectRatio < viewAspectRatio) {
            return fitHorizontal(w, h, bitmapAspectRatio);
        }
        return fitVertical(w, h, bitmapAspectRatio);
    }
    
    private Rect fitAuto(Bitmap bmp, int w, int h) {
        float bitmapAspectRatio = computeBitmapAspectRatio(bmp);
        float viewAspectRatio = w / (float) h;
        if (bitmapAspectRatio > viewAspectRatio) {
            return fitHorizontal(w, h, bitmapAspectRatio);
        }
        return fitVertical(w, h, bitmapAspectRatio);
    }

    private Rect fitHorizontal(int w, int h, float bitmapAspectRatio) {
        int destWidth = w;
        int destHeight = (int) (destWidth / bitmapAspectRatio);
        int vGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        int top = 0;
        if (vGravity == Gravity.CENTER_VERTICAL) {
            top = h / 2 - destHeight / 2;
        }
        else if (vGravity == Gravity.BOTTOM) {
            top = h - destHeight;
        }
        return new Rect(0, top, destWidth, top + destHeight);
    }

    private Rect fitVertical(int w, int h, float bitmapAspectRatio) {
        int destHeight = h;
        int destWidth = (int) (destHeight * bitmapAspectRatio);
        int hGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        int left = 0;
        if (hGravity == Gravity.CENTER_HORIZONTAL) {
            left = w / 2 - destWidth / 2;
        }
        else if (hGravity == Gravity.RIGHT) {
            left = w - destWidth;
        }
        return new Rect(left, 0, left + destWidth, destHeight);
    }
    
    private static float computeBitmapAspectRatio(Bitmap bmp) {
        return bmp.getWidth() / (float) bmp.getHeight();
    }
}
