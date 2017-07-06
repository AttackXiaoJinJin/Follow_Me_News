/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.project.chenjin.follow_me_news.volley;
import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * 对Volley的封装
 */
public class VolleyManager {
	/**
	 * 请求队列
	 */
	private static RequestQueue mRequestQueue;

	/**
	 * 图片加载工具类
	 */
	private static ImageLoader mImageLoader;

	private VolleyManager() {
		// no instances
	}

	/**
	 * 使用Volley请求网络和图片是首先调用该方法
	 * 建议在Application 的onCreater中调用
	 * @param context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		//告诉你你的机器还有多少内存，在计算缓存大小的时候会比较有
		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;//图片缓存的空间
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	/**
	 * 得到消息队列
	 * @return
	 */
	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/**
	 * 把请求添加到队列中
	 * @param request
	 * @param tag
	 */
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

	/**
	 * 请求的取消
	 * @param tag
	 */
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
