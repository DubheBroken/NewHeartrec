package com.dubhe.broken.newheartrec.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dubhe.broken.newheartrec.application.AppData;
import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.activity.PainterActivity;
import com.dubhe.broken.newheartrec.adapter.PaintAdapter;
import com.dubhe.broken.newheartrec.entity.PaintEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：DubheBroken
 * 时间：2018/11/22 15:25:20
 * 邮箱：z1574507001@gmail.com
 * 说明：
 */
public class PaintListFragment extends Fragment implements View.OnTouchListener {

    private Context context;
    private List<PaintEntity> list;
    private RecyclerView recyclerList;
    private LinearLayout linearRecycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_recycler, container, false);
        view.setOnTouchListener(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerList = view.findViewById(R.id.recycler_list);
        linearRecycler = view.findViewById(R.id.linear_recycler);

    }

    private void initAdapter() {
        list = new ArrayList<>();
        getImage(list);
        PaintAdapter paintAdapter = new PaintAdapter(context, list);
        paintAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(context, PainterActivity.class);
            intent.putExtra("fileName", list.get(position).getFilename());//将被点击的item文件名传递到新活动
            startActivity(intent);
        });
        paintAdapter.setOnItemLongClickListener((view, position) ->
                showDeleteDia(list.get(position).getFilename()));
        recyclerList.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerList.setAdapter(paintAdapter);
    }

    //    长按菜单
    private void showDeleteDia(final String idorfilename) {
        AlertDialog.Builder multiDia = new AlertDialog.Builder(context);
        multiDia.setTitle("选择操作");
        multiDia.setPositiveButton("删除", (dialog, which) -> delete(idorfilename));
        multiDia.create().show();
    }

    private void getImage(List<PaintEntity> list) {
        File file = new File(AppData.getImageFilePath());
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
                    if (suffix.toLowerCase().equals(".png")) {
                        PaintEntity paint_entity = new PaintEntity();
                        paint_entity.setTime(fi.getName());
                        paint_entity.setFilename(fi.getPath());
                        list.add(paint_entity);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
