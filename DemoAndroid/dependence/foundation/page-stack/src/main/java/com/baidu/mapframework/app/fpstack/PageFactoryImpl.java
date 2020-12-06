package com.baidu.mapframework.app.fpstack;

import java.util.HashMap;
import java.util.Set;

//import com.baidu.platform.comapi.DebugConfig;
//import com.baidu.platform.comapi.util.MLog;

import android.os.Bundle;
import android.support.v4.util.LruCache;

/**
 * 页面生成工厂类
 * <p>生成并维护page的实例，page实例依据类名和tag标识</p>
 *
 * @author liguoqing
 * @version 2.0
 * @data 13-10-09
 * @date 13-6-4 下午11:14
 * @data 15-04-16 增加页面数量缓存大小，增加页面参数缓存（用于还原因超出缓存数量被清理的页面）。
 */
class PageFactoryImpl implements PageFactory {

    private static final boolean DEBUG = /*DebugConfig.DEBUG*/false;
    private static final String TAG = "PageFactory";

    private static class PageFactoryHolder {
        static final PageFactory instance = new PageFactoryImpl();
    }

    public static PageFactory getInstance() {
        return PageFactoryHolder.instance;
    }

    private PageLruCache mPagesLruCache;

    private static final int MAX_LRU_SIZE = 32; //the max size of our PageCache

    private PageFactoryImpl() {
        mPagesLruCache = new PageLruCache(MAX_LRU_SIZE);
    }

    @Override
    public BasePage getBasePageInstance(String pageClsName) {
        return getBasePageInstance(pageClsName, DEFAULT_PAGE_TAG);
    }

    /**
     * <p>获取页面对象</p>
     * <p/>
     * 首先从LRUcache中查找，没有再从softcache中查找
     *
     * @param pageClsName   页面类名
     * @param pageTagString 页面标签，用于区分多实例
     *
     * @return 页面对象
     */
    @Override
    public BasePage getBasePageInstance(String pageClsName, String pageTagString) {

        if (pageClsName == null) {
            return null;
        }

        String pageCacheKey = pageTagString == null ? pageClsName + "@" + DEFAULT_PAGE_TAG : pageClsName + "@" +
                pageTagString;

        BasePage page;

        synchronized(mPagesLruCache) {
            page = mPagesLruCache.get(pageCacheKey);
            if (null == page) {
                page = newBasePageInstance(getDefaultClassLoader(), pageClsName);
                if (null != page) {
                    mPagesLruCache.put(pageCacheKey, page);
                    if (DEBUG) {
                        //MLog.i(TAG, "Put Page to Cache:" + pageCacheKey);
                        //MLog.i(TAG, "Lru Cache size:" + mPagesLruCache.size());
                    }
                }
            }
        }
        return page;
    }

    @Override
    public BasePage getBasePageInstance(ClassLoader classLoader, String pageClsName) {
        return getBasePageInstance(classLoader, pageClsName, DEFAULT_PAGE_TAG);
    }

    @Override
    public BasePage getBasePageInstance(ClassLoader classLoader, String pageClsName, String pageTagString) {
        long startTick = System.currentTimeMillis();
        if (pageClsName == null) {
            return null;
        }

        String pageCacheKey = getPageCachedKey(pageClsName, pageTagString);

        BasePage page;

        synchronized(mPagesLruCache) {
            page = mPagesLruCache.get(pageCacheKey);
            if (null == page) {
                page = newBasePageInstance(classLoader, pageClsName);
                if (page != null) {
                    mPagesLruCache.put(pageCacheKey, page);
                    if (DEBUG) {
                        //MLog.i(TAG, "Put Page to Cache:" + pageCacheKey);
                        //MLog.i(TAG, "Lru Cache size:" + mPagesLruCache.size());
                    }
                }
            }
        }
        if (DEBUG) {
            //MLog.d(TAG,
            //        "getBasePageInstance " + pageClsName + " time:" + (System.currentTimeMillis() - startTick) + " ms");
        }
        return page;
    }

    @Override
    public void clearCache() {
        synchronized(mPagesLruCache) {
            mPagesLruCache.evictAll();
        }
    }

    @Override
    public void removePage(BasePage page) {
        String pageTag = page.getPageTag();
        String name = ((Object) page).getClass().getName();
        String pageCacheKey = getPageCachedKey(name, pageTag);
        if (DEBUG) {
            //MLog.i(TAG, "RemovePage:" + pageCacheKey);
        }
        synchronized(mPagesLruCache) {
            mPagesLruCache.remove(pageCacheKey);
        }
    }

    @Override
    public Bundle getPageSavedArguments(String pageClsName, String pageTagString) {
        String key = getPageCachedKey(pageClsName, pageTagString);
        return mPagesLruCache.getCachedPageArguments(key);
    }

    private String getPageCachedKey(String pageClsName, String pageTagString) {
        return pageTagString == null ? pageClsName + "@" + DEFAULT_PAGE_TAG : pageClsName + "@" +
                pageTagString;
    }

    /**
     * 获取新的页面实例
     *
     * @param classLoader class loader
     * @param pageClsName 页面类名
     *
     * @return
     */
    private BasePage newBasePageInstance(ClassLoader classLoader, String pageClsName) {

        Class<?> pgCls;
        BasePage page = null;
        try {
            page = (BasePage) ReflectionUtils.newInstance(classLoader, pageClsName);
        } catch (ClassNotFoundException e) {
            //MLog.e(TAG, "exception", e);
        } catch (IllegalAccessException e) {
            //MLog.e(TAG, "exception", e);
        } catch (InstantiationException e) {
            //MLog.e(TAG, "exception", e);
        } catch (Exception e) {
            //MLog.e(TAG, "exception", e);
        }

        return page;
    }

    private ClassLoader getDefaultClassLoader() {
        return this.getClass().getClassLoader();
    }

    private void log() {
        if (DEBUG) {
            //MLog.e(TAG, "LRU cache size:" + mPagesLruCache.size());
        }
    }

    /**
     * 页面缓存内部类。
     */
    static class PageLruCache extends LruCache<String, BasePage> {

        private HashMap<String, Bundle> mCachedArguments = new HashMap<>();

        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public PageLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, BasePage oldValue, BasePage newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            if (oldValue != null) {
                if (DEBUG) {
                    //MLog.i(TAG, "LruCache entryRemoved key:" + key);
                    //MLog.i(TAG, "LruCache entryRemoved size:" + this.size());
                }
                if (evicted) {
                    mCachedArguments.put(key, oldValue.getPageArguments());
                } else {
                    mCachedArguments.remove(key);
                }
            }
        }

        public Bundle getCachedPageArguments(String key) {
            if (DEBUG) {
                Bundle bundle = mCachedArguments.get(key);
                if (bundle != null) {
                    Set<String> keys = bundle.keySet();
                    for (String k : keys) {
                        //MLog.i(TAG, bundle.get(k).toString());
                    }
                }
            }
            return mCachedArguments.get(key);
        }

    }

}
