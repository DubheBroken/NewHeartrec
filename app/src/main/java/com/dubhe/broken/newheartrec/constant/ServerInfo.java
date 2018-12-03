package com.dubhe.broken.newheartrec.constant;

public class ServerInfo {

    public static final String REQUESTCODE = "request_code";//请求码名称
    public static final String URI = "http://192.168.1.122:8080/";

    public static final int STATUS_SUCCESS = 200;//收到服务器返回结果
    public static final int STATUS_ERROR = 500;//服务器内部错误
    public static final int STATUS_NOT_FOUND = 404;//连接服务器失败
    public static final int LOADING_FAIL = -1;//加载失败

    //服务器方法列表
    public class Method {
        public static final String LOGIN = "Login";
        public static final String SIGNIN = "Signin";
    }
}
