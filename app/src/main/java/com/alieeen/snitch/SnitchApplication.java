package com.alieeen.snitch;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.LruCache;

import org.apache.http.client.CookieStore;
import org.apache.http.protocol.HttpContext;

/**
 * Created by alinekborges on 15/04/15.
 */
public class SnitchApplication extends Application {

    private static SnitchApplication singleton;
    private CookieStore store;
    private HttpContext httpcontext;
    private int totalNotifications;
    private LruCache<String, Bitmap> mMemoryCache;

    public SnitchApplication getInstance(){
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public CookieStore getCookieStore()
    {
        return this.store;
    }

    public void setCookieStore(CookieStore store)
    {
        this.store = store;
    }

    public HttpContext getHttpContext()
    {
        return this.httpcontext;
    }

    public void setHttpContext (HttpContext context)
    {
        this.httpcontext = context;
    }

    public int getTotalNotifications ()
    {
        return this.totalNotifications;
    }

    public void setTotalNotifications (int total)
    {
        this.totalNotifications = total;
    }

    public LruCache<String, Bitmap> getBitmapCache ()
    {
        return this.mMemoryCache;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}

