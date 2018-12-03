package com.dubhe.broken.newheartrec.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 自定义网络回调接口
 */
public interface MyNetCall {
    void success(Call call, Response response) throws IOException;
    void failed(Call call, IOException e);
}