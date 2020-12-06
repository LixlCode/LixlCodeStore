package com.wecome.demo.network;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieManger implements CookieJar {

    private static PersistentCookieStore cookieStore;

    CookieManger(Context context) {
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(context);
        }
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @NonNull
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return  cookieStore.get(url);
    }

    static class Customer {

        private String userID;
        private String token;

        public Customer(String userID, String token) {
            this.userID = userID;
            this.token = token;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}