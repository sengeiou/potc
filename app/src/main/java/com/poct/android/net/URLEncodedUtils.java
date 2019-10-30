package com.poct.android.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by xpx on 16-7-5.
 */
public class URLEncodedUtils {


    public static String format(List<NameValuePair> nvps, String model) {


        String url = "";
        try {
            url = "";
            for (int i = 0; i < nvps.size(); i++) {
                url += URLEncoder.encode(nvps.get(i).getKey(), model) + "=" + URLEncoder.encode(nvps.get(i).getValue(), model);
                if (i != nvps.size() - 1) {
                    url += "&";
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
