package com.baiduvolunteer.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Toast;

import com.baiduvolunteer.MyApplication;
import com.baiduvolunteer.R;
import com.lidroid.xutils.BitmapUtils;

public class ViewUtils {
	private static Context mContext;
	private static float density;
	private static Handler handler;
	private static BitmapUtils bmUtils;
	private static Toast mToast;

	private static Object initLock = new Object();

	public static void init(Context context) {
		if (mContext != null)
			return;
		mContext = context.getApplicationContext();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		handler = new Handler();
		density = dm.density;

		// ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
		// bmUtils.configDefaultBitmapConfig(Config.ARGB_8888);
	}

	public static BitmapUtils bmUtils() {
		if (bmUtils == null) {
			synchronized (initLock) {
				if (bmUtils != null)
					return bmUtils;
				if (mContext != null)
					bmUtils = new BitmapUtils(mContext);
				else {
					bmUtils = new BitmapUtils(MyApplication.getApplication());
				}
				bmUtils.configDiskCacheEnabled(true);
				bmUtils.clearCache();
				bmUtils.clearDiskCache();
				bmUtils.configDefaultLoadFailedImage(R.drawable.default_icon);
			}
		}
		return bmUtils;
	}

	public static int rp(double d) {
		if (mContext == null) {
			throw (new IllegalStateException(
					"view util should be called after initialized"));
		}
		return (int) (density * d + 0.5);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap circleBitmapWithoutRing(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int r = w > h ? h : w;
		Bitmap output = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		// paint.setColor(0xff000000);

		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(new Rect(0, 0, r, r));
		// RectF rectf = new RectF(0F, 0F, w, h);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		canvas.drawRoundRect(rectF, r, r, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);
		return output;
	}

	public static Bitmap circleBitmap(Bitmap bitmap, int widthPx) {
		widthPx = rp(widthPx);
		int off = widthPx + 1;
		int w = bitmap.getWidth() + off * 2;
		int h = bitmap.getHeight() + off * 2;
		Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		final Rect rect = new Rect(widthPx, widthPx, w - widthPx, h - widthPx);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setStrokeWidth(widthPx);
		paint.setStyle(Style.STROKE);

		// final Rect rect1 = new Rect(off, off, w-off,h-off);
		// final RectF rectF1 = new RectF(rect1);
		canvas.drawBitmap(circleBitmapWithoutRing(bitmap), off, off, paint);
		// canvas.drawBitmap(circleBitmapWithourRing(bitmap), rect1, rectF1,
		// paint);
		// paint.set
		paint.setColor(Color.WHITE);

		canvas.drawOval(rectF, paint);
		return output;

	}

	public static Bitmap circleBitmap(Bitmap bitmap, int widthDp,
			int ringWidthDp, int color) {
		int widthPx = rp(widthDp);
		int ringWidthPx = rp(ringWidthDp);

		int off = ringWidthPx;
		int w = widthPx + ringWidthPx * 2;
		Bitmap output = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		final Rect rect = new Rect(ringWidthPx, ringWidthPx, w - ringWidthPx, w
				- ringWidthPx);
		final RectF rectF = new RectF(ringWidthPx / 2, ringWidthPx / 2, w
				- ringWidthPx / 2, w - ringWidthPx / 2);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setStrokeWidth(ringWidthPx);
		paint.setStyle(Style.STROKE);

		// final Rect rect1 = new Rect(off, off, w-off,h-off);
		// final RectF rectF1 = new RectF(rect1);
		Bitmap bm = circleBitmapWithoutRing(bitmap);
		canvas.drawBitmap(bm, new Rect(0, 0, bm.getWidth(), bm.getHeight()),
				rect, paint);
		// canvas.drawBitmap(circleBitmapWithourRing(bitmap), rect1, rectF1,
		// paint);
		// paint.set
		paint.setColor(color);

		canvas.drawOval(rectF, paint);
		return output;

	}

	public static <T extends View> T findViewById(View view, int id) {
		return (T) view.findViewById(id);
	}

	public static void removeFromSuperView(Object o) {
		if (o == null)
			return;

		if (o instanceof View) {
			View view = (View) o;
			final ViewParent parent = view.getParent();
			if (parent == null)
				return;
			if (parent instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) parent;
				group.removeView(view);
			}
		} else if (o instanceof Dialog) {
			Dialog dialog = (Dialog) o;
			dialog.hide();
		}
	}

	public static int[] screenSize() {
		if (mContext == null) {
			return null;
		} else {
			WindowManager wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			return new int[] { dm.widthPixels, dm.heightPixels };
		}
	}

	public static void runInMainThread(Runnable r) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			r.run();
		} else {
			handler.post(r);
		}
	}

//	public static void showToast(final CharSequence message,
//			final int timeMillis) {
//		runInMainThread(new Runnable() {
//
//			@Override
//			public void run() {
//				Toast.makeText(mContext, message, timeMillis).show();
//				;
//			}
//		});
//	}

	public static Context getContext() {
		return mContext;
	}

	// public static void login() {
	// if (mContext == null)
	// return;
	// Intent intent = new Intent(mContext, LoginActivity.class);
	// intent.putExtra(BaseActivity.IntentKeyFragmentClass,
	// LoginFragment.class);
	// intent.putExtra(BaseActivity.IntentKeyHasTitle, true);
	// intent.putExtra(BaseActivity.IntentKeyFullScreen, true);
	// intent.putExtra(BaseActivity.IntentKeyBlockBackButton, true);
	// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
	// | Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_NEW_TASK);
	//
	// mContext.startActivity(intent);
	// }

	public static void showToast(String msg, int length) {
		if (mContext == null)
			return;
		if (mToast == null) {
			mToast = Toast.makeText(mContext, msg, length);
			mToast.show();
		} else {
			mToast.setText(msg);
			mToast.setDuration(length);
			mToast.show();
		}
	}

	public static String toUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		String uStr = "";
		for (int i = 0; i < arChar.length; i++) {
			iValue = (int) str.charAt(i);
			if (iValue <= 256) {
				uStr += "\\" + Integer.toHexString(iValue);
			} else {
				uStr += "\\u" + Integer.toHexString(iValue);
			}
		}
		return uStr;
	}
}
