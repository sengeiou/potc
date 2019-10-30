package com.poct.android.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static final String APP_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/poct";

    public static final String IM_PATH = "/im";
    public static final String COLLECT_PATH = "/collect";
    public static final String CHAT_PATH = "/chat";
    public static final String GRID_PATH = "/grid";
    public static final String ATTACHMENT_PATH = "/attachment";


    public String getPath(String path)
    {
        File mFile = new File(path);
        if(!mFile.exists())
        {
            mFile.mkdirs();
        }
        return path;
    }

    public static boolean checkDownload(long size) {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocksLong() * (double) stat
                .getBlockSizeLong()) / 1024 / 1024;
        double addMB = size / 1024 / 1024;
        if (addMB > sdFreeMB) {
            return false;
        } else {
            return true;
        }
    }

    public static List<String> getEmojiFilePng(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji1");// �ļ�����Ϊrose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static List<String> getEmojiFileGif(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji2");// �ļ�����Ϊrose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
