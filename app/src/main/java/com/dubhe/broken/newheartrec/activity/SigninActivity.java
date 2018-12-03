package com.dubhe.broken.newheartrec.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.application.AppData;
import com.dubhe.broken.newheartrec.constant.ServerInfo;
import com.dubhe.broken.newheartrec.entity.User;
import com.dubhe.broken.newheartrec.utils.MD5andKL;
import com.dubhe.broken.newheartrec.utils.MyNetCall;
import com.dubhe.broken.newheartrec.utils.OkHttpUtil;
import com.dubhe.broken.newheartrec.utils.ToastUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：DubheBroken
 * 时间：2018/12/3 16:48:30
 * 邮箱：z1574507001@gmail.com
 * 说明：注册活动
 */
public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "newheartrec.SigninActivity";
    private Context context = this;
    private TextView btnCancelSignin;
    private TextView textViewa;
    private ConstraintLayout toolbarSignin;
    private TextView textSignin;
    private MaterialEditText metPhoneSignin;
    private MaterialEditText metNameSignin;
    private MaterialEditText metPasswordSignin;
    private Button buttonSubmitSignin;
    private ConstraintLayout constraintSignin;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        btnCancelSignin = findViewById(R.id.btn_cancel_signin);
        btnCancelSignin.setOnClickListener(v -> finish());
        textViewa = findViewById(R.id.textViewa);
        toolbarSignin = findViewById(R.id.toolbar_signin);
        textSignin = findViewById(R.id.text_signin);
        metPhoneSignin = findViewById(R.id.met_phone_signin);
        metNameSignin = findViewById(R.id.met_name_signin);
        metPasswordSignin = findViewById(R.id.met_password_signin);
        buttonSubmitSignin = findViewById(R.id.button_submit_signin);
        buttonSubmitSignin.setOnClickListener(this);
        constraintSignin = findViewById(R.id.constraint_signin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit_signin:
                doSignin();
                break;
        }
    }

    private void doSignin() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            Map<String, String> map = new HashMap();
            map.put("REQUESTCODE", ServerInfo.REQUESTCODE);
            map.put("phone", metPhoneSignin.getEditableText().toString());
            map.put("name", metNameSignin.getEditableText().toString());
            map.put("password", MD5andKL.MD5(metPasswordSignin.getEditableText().toString()));
            new OkHttpUtil().doPost(ServerInfo.Method.SIGNIN, map, new MyNetCall() {
                @Override
                public void success(Call call, Response response) throws IOException {
                    emitter.onNext(response.body() != null ? response.body().string() : "");
                }

                @Override
                public void failed(Call call, IOException e) {
                    emitter.onError(e);
                }
            });
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                showLoadingDialog();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onNext(String string) {
                dialog.cancel();
                switch (string) {
                    case "1":
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            User user = objectMapper.readValue(string, User.class);
                            AppData.updateUser(user);
                        } catch (IOException e) {
                            Log.e(TAG, "", e);
                        }
                        break;
                    case "0":
                        ToastUtil.showToast(context, "注册失败");
                        break;
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onError(Throwable e) {
                dialog.cancel();
                Log.e(TAG, "发送网络请求", e);
            }

            @Override
            public void onComplete() {
                dialog.cancel();
            }
        });
    }

    /**
     * 显示加载对话框
     */
    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        View dialogView = View.inflate(this.getApplicationContext(), R.layout.loading_layout, null);
        ((AlertDialog) dialog).setView(dialogView);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * @param phone 手机号字符串
     * @return 手机号是否合法
     */
    public boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            Toast.makeText(context, "手机号应为11位数", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();

            if (!isMatch) {
                Toast.makeText(context, "请填入正确的手机号", Toast.LENGTH_SHORT).show();
            }
            return isMatch;
        }
    }


}
