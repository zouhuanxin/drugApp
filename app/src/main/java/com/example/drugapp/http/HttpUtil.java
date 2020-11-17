package com.example.drugapp.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String ip = "http://192.168.1.105:8085";
    //private String ip = "http://123.57.45.169:8084";

    private int overtime = 10;

    private static class SingletonClassInstance {
        private static final HttpUtil np = new HttpUtil();
    }

    public HttpUtil() {

    }

    public static synchronized HttpUtil getInstance() {
        return SingletonClassInstance.np;
    }

    public void GET(String afterurl, HttpCallBack h) {
        String url = ip + afterurl;
        doGET(url, h);
    }

    private void doGET(final String urls, final HttpCallBack h) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(overtime, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(overtime, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(overtime, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        Request request = new Request.Builder()
                .url(urls)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                h.Error(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                response.body().close();
                //System.out.println("状态码:"+response.code()+" \n  请求地址:"+urls+"\n  返回数据:"+res+"\n  token:"+token);
                try {
                    h.Success(call, res);
                } catch (Exception e) {
                    h.Error(call, null);
                    // e.printStackTrace();
                }
            }
        });
    }

    public void POST(String afterurl, String json, HttpCallBack h) {
        String url = ip + afterurl;
        doPost(url, json, h);
    }

    private void doPost(String url, String json, final HttpCallBack httpCallBack) {
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(overtime, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(overtime, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(overtime, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.Error(call, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null) {
                    httpCallBack.Error(call, null);
                    return;
                }
                if (response.body() == null) {
                    httpCallBack.Error(call, null);
                    return;
                }
                String str = response.body().string();
                try {
                    httpCallBack.Success(call, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
