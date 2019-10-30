package com.poct.android.net;

/**
 * Created by xpx on 16-7-5.
 */
public class NameValuePair {

    private String key;
    private String value;

    public NameValuePair(String key, String value)
    {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
