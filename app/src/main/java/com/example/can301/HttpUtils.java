package com.example.can301;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static String gethttpresult(String urlStr) {
        try {
            java.net.URL url = new URL(urlStr);//获取url对象
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();//url对象进行http连接
            InputStream input = connect.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            System.out.println(connect.getResponseCode());
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);//逐行读取传来的String
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}

