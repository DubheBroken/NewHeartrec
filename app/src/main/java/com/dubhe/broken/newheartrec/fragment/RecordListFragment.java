package com.dubhe.broken.newheartrec.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dubhe.broken.newheartrec.AppData;
import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.activity.NewOneActivity;
import com.dubhe.broken.newheartrec.adapter.RecordAdapter;
import com.dubhe.broken.newheartrec.adapter.TextAdapter;
import com.dubhe.broken.newheartrec.entity.RecordEntity;
import com.dubhe.broken.newheartrec.entity.TextEntity;
import com.dubhe.broken.newheartrec.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 作者：DubheBroken
 * 时间：2018/11/22 15:25:20
 * 邮箱：z1574507001@gmail.com
 * 说明：
 */
public class RecordListFragment extends Fragment implements View.OnTouchListener {

    private Context context;
    private ListView listView;
    private List<RecordEntity> list;
    private boolean runed = false;//是否允许过动画线程
    private int page = 1;//当前页面
    private Runnable switchRunnable, loopRunnable, toNormalRunnable, recoverRunnable;
    private Thread loopThread;
    private ExecutorService cacheThreadPool;
    private boolean running = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_listview, container, false);
        view.setOnTouchListener(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = view.findViewById(R.id.listview_list);
        initAdapter();
    }

    private void initAdapter() {
        getRecord();
        RecordAdapter recordAdapter = new RecordAdapter(context, list);
        recordAdapter.setOnDeleteClickListener((view1, position) -> {
            delete(list.get(position).getFilename());
        });
        listView.setAdapter(recordAdapter);
    }

    //    删除记录并刷新RecyclerView
    public void delete(final String idorfilename) {

        new AlertDialog.Builder(context).setTitle("确认要删除吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", (dialog, which) -> {
                    // 点击“确认”后的操作
                    File file = new File(idorfilename);
                    try {
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    initAdapter();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 点击“返回”后的操作,这里不设置没有任何操作
                }).show();

    }

    private void getRecord() {
        list = new ArrayList<>();
        File file = new File(AppData.getRecordFilePath());
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] allfiles = file.listFiles();
        if (allfiles != null) {
            for (int i = 0; i < allfiles.length; i++) {
                File fi = allfiles[i];
                if (fi.isFile()) {
                    int idx = fi.getPath().lastIndexOf(".");
                    if (idx <= 0) {
                        continue;
                    }
                    String suffix = fi.getPath().substring(idx);
                    if (suffix.toLowerCase().equals(".amr")) {
                        RecordEntity record_entity = new RecordEntity();
                        record_entity.setTime(fi.getName());
                        record_entity.setFilename(fi.getPath());
                        list.add(record_entity);
                    }
                }
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
