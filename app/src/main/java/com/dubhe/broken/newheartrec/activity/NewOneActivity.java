package com.dubhe.broken.newheartrec.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.dubhe.broken.newheartrec.AppData;
import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.entity.TextEntity;
import com.dubhe.broken.newheartrec.utils.Constant;
import com.dubhe.broken.newheartrec.utils.DbManager;
import com.dubhe.broken.newheartrec.utils.SqliteHelper;
import com.dubhe.broken.newheartrec.utils.SystemBarTintManager;
import com.dubhe.broken.newheartrec.utils.TextManager;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zero on 2017/2/16.
 */

public class NewOneActivity extends Activity {

    private Context context = this;

    private Intent intent;
    private String id = "";

    private SqliteHelper sqliteHelper;
    //    注册控件
    private EditText editText_substance;
    private TextView btn_cancel;
    private ConstraintLayout layout;

    //    数据变量
    private String nowtime, substance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newone_layout);

        //        获取时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date curtime = new Date(System.currentTimeMillis());//获取当前时间
        nowtime = formatter.format(curtime);

        initView();//初始化界面
        AppData.setFinalPage(1);

//        获取intent
        intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("id")) {
//            获取intent中的值
                id = intent.getStringExtra("id");
//            为控件赋值
                initData(id);
            }
        }

        //        沉浸式状态栏
        // 4.4以上版本开启
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);


            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);

            // 状态栏背景色
            if (Build.VERSION.SDK_INT < 23) {
                tintManager.setStatusBarTintResource(R.color.colorAccent);
            } else {
                tintManager.setTintColor(getColor(R.color.colorAccent));
            }
        }


    }

    //    沉浸式状态栏
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initView() {
        sqliteHelper = DbManager.getIntance(this);
//        option_helper = new OptionHelper(context);
        //        实例化控件
        editText_substance = findViewById(R.id.edittext_substance);
        btn_cancel = findViewById(R.id.btn_cancel);
        layout = findViewById(R.id.layout_newone);

//        点击监听
        btn_cancel.setOnClickListener(v -> finish());
        editText_substance.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //文本内容变化时

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //文本内容变化前

            }

            @Override
            public void afterTextChanged(Editable s) {
                //文本内容变化后
                substance = editText_substance.getText().toString();
            }
        });

    }

    @Override
    protected void onPause() {
        save();
        super.onPause();
    }

    private String escape(String str) {
        if (str.contains("'")) {
            str = str.replace("'", "''");
        }
        return str;
    }

    //    保存
    public void save() {
        if (substance != null && !"".equals(substance)) {
            while (TextManager.operation > 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Log.e("---getTexts---", e.getMessage());
                }
            }
            SQLiteDatabase db = sqliteHelper.getWritableDatabase();//打开数据库
            TextManager.addOperation();
            substance = escape(substance);
            if (id != null && !id.equals("")) {
                try {
                    String sql = "update " + Constant.TABLE_NAME + " set " + "" + Constant.SUBSTANCE + "='" + substance + "'," + Constant.TIME + "=\"" + nowtime + "\" where " + Constant.ID + "=" + id + ";";
                    DbManager.execSQL(db, sql);
                } catch (Exception e) {
                    Log.e("---保存异常---", "", e);
                }
            } else {
                try {
                    String sql = "insert into " + Constant.TABLE_NAME + " values(null,'" + substance + "','" + nowtime + "');";
                    DbManager.execSQL(db, sql);
                } catch (Exception e) {
                    Log.e("---保存异常---", "", e);
                }
            }
            TextManager.removeOperation();
            while (TextManager.operation > 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //关闭数据库
            db.close();
        }
    }

    /**
     * 查询并赋值控件
     */
    private void initData(String id) {
        //查询数据
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String sql = "select * from " + Constant.TABLE_NAME + " where " + Constant.ID + "=" + id + ";";
        Cursor cursor = DbManager.selectDataBySql(db, sql, null);
        if (cursor != null && db.isOpen()) {
//                为控件赋值
            if (cursor.moveToFirst()) {
                editText_substance.setText(cursor.getString(cursor.getColumnIndex("substance")));
            }
        }
        TextManager.removeOperation();
        while (TextManager.operation > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //关闭数据库
        db.close();
    }

}
