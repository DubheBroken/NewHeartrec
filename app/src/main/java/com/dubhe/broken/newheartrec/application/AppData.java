package com.dubhe.broken.newheartrec.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.dubhe.broken.newheartrec.entity.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Developer on 2017/7/3.
 */

public class AppData extends Application {

    private static String appHomePath = "sdcard/heartrec/";
    private static String imageFilePath = "sdcard/heartrec/paint/";
    private static String recordFilePath = "sdcard/heartrec/record/";
    private static int penColor = 0xff0000;//画笔颜色，默认红色
    private static int penSize = 9;//画笔尺寸
    private static int finalPage = 1;//最后访问的页面，默认文本
    public static User user;//用户信息
    public static final String PREFS_NAME = "config";//设置文件名称
    private static SharedPreferences settings;

    @Override
    public void onCreate() {
        super.onCreate();
        AppData.settings = getSharedPreferences(PREFS_NAME, 0);
        AppData.user = getUser();
    }

    /**
     * 保存用户数据到手机
     */
    private static void saveUser() {
        if (AppData.user instanceof Serializable) {
            SharedPreferences.Editor editor = AppData.settings.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(AppData.user);//把对象写到流里
                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                editor.putString("user", temp);
                editor.apply();
            } catch (IOException e) {
                Log.e("---data---", e.toString());
            }
        } else {
            try {
                throw new Exception("User must implements Serializable");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从手机获取用户数据对象
     * @return 手机保存的用户对象
     */
    private static User getUser() {
        String temp = AppData.settings.getString("user", "");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            user = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e("---data---","getUser", e);
        }
        return user;
    }

    /**
     * 使用服务器返回的用户对象更新当前用户对象
     *
     * @param newUser 服务器返回的用户对象
     */
    public static void updateUser(User newUser) {
        //用于进行更新操作的临时对象
        User user = new User();
        //如果Data中没有用户对象就new一个
        if (AppData.user == null) {
            AppData.user = new User();
        }
        if (newUser != null) {
            //ID↓
            user.setId(newUser.getId() != 0 ? newUser.getId() : AppData.user.getId());
            //姓名↓
            user.setName(newUser.getName() != null ? newUser.getName() : AppData.user.getName());
            //手机号↓
            user.setPhone(newUser.getPhone() != null ? newUser.getPhone() : AppData.user.getPhone());
            //密码↓
            user.setPassword(newUser.getPassword() != null ? newUser.getPassword() : AppData.user.getPassword());
            //银行卡号↓
            user.setTexts(newUser.getTexts() != null ? newUser.getTexts() : AppData.user.getTexts());
            //开户行↓
            user.setRecords(newUser.getRecords() != null ? newUser.getRecords() : AppData.user.getRecords());
            //城市↓
            user.setPaints(newUser.getPaints() != null ? newUser.getPaints() : AppData.user.getPaints());
            //培训状态↓
            AppData.user = user;
            saveUser();
        }
    }

    /**
     * 清空本地数据
     * 登出按钮执行此操作
     */
    public static void clear() {
        AppData.settings.edit().clear().apply();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new User());//把对象写到流里
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            AppData.settings.edit().putString("user", temp);
            AppData.settings.edit().apply();
        } catch (IOException e) {
            Log.e("---data---", e.toString());
        }
    }

    public static int getFinalPage() {
        return finalPage;
    }

    public static void setFinalPage(int finalPage) {
        AppData.finalPage = finalPage;
    }

    public static String getAppHomePath() {
        return appHomePath;
    }

    public static void setAppHomePath(String appHomePath) {
        AppData.appHomePath = appHomePath;
    }

    public static String getImageFilePath() {
        return imageFilePath;
    }

    public static void setImageFilePath(String imageFilePath) {
        AppData.imageFilePath = imageFilePath;
    }

    public static String getRecordFilePath() {
        return recordFilePath;
    }

    public static void setRecordFilePath(String recordFilePath) {
        AppData.recordFilePath = recordFilePath;
    }

    public static int getPenColor() {
        return penColor;
    }

    public static void setPenColor(int penColor) {
        AppData.penColor = penColor;
    }

    public static int getPenSize() {
        return penSize;
    }

    public static void setPenSize(int penSize) {
        AppData.penSize = penSize;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}
