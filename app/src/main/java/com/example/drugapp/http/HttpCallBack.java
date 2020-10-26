package com.example.drugapp.http;


import java.io.IOException;

import okhttp3.Call;

public interface HttpCallBack {

    void Error(Call call, IOException e);

    //回调返回内容
    void Success(Call call, String res) throws Exception;

}