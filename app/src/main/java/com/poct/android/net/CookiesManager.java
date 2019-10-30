package com.poct.android.net;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.poct.android.view.PoctApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自动管理Cookies
 */
class CookiesManager implements CookieJar {
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(PoctApplication.mApp.getApplicationContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
        	 syncCookie(cookies,url.url().toString());
            for (Cookie item : cookies) {
                cookieStore.add(url, item);         
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    private void syncCookie(List<Cookie> cookies, String url){
        try{
            CookieSyncManager.createInstance(PoctApplication.mApp.getApplicationContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            String oldCookie = cookieManager.getCookie(url);
            if(oldCookie != null){
                Log.d("mycookie0", oldCookie);
            }
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format(cookies.get(0).name()+"=%s",cookies.get(0).value()));
            sbCookie.append(String.format(";domain=%s", cookies.get(0).domain()));
            sbCookie.append(String.format(";path=%s",cookies.get(0).path()));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
            String newCookie = cookieManager.getCookie(url);
            if(newCookie != null){
                Log.d("mycookie2", newCookie);
            }
        }catch(Exception e){
            Log.e("mycookie3", e.toString());
        }
    }

}
