package com.dubhe.broken.newheartrec.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.activity.NewOneActivity;
import com.dubhe.broken.newheartrec.adapter.TextAdapter;
import com.dubhe.broken.newheartrec.entity.TextEntity;
import com.dubhe.broken.newheartrec.utils.Constant;
import com.dubhe.broken.newheartrec.utils.DbManager;
import com.dubhe.broken.newheartrec.utils.TextManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：DubheBroken
 * 时间：2018/11/22 15:25:20
 * 邮箱：z1574507001@gmail.com
 * 说明：
 */
public class TextListFragment extends Fragment implements View.OnTouchListener {

    private Context context;
    private ListView listView;
    private List<TextEntity> list;

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
    }

    private void initAdapter() {
        if (list == null) {
            list = TextManager.getTexts(context);
        }
        TextAdapter text_adapter = new TextAdapter(context, list);
        text_adapter.setOnItemClickListener((view1, position) -> {
            startActivity(new Intent(context, NewOneActivity.class).putExtra("id", list.get(position).getId()));
        });
        text_adapter.setOnDeleteClickListener(((view1, position) -> {
            delete(list.get(position));
        }));
        listView.setAdapter(text_adapter);
    }

    @Override
    public void onResume() {
        initAdapter();
        super.onResume();
    }

    //    删除记录并刷新RecyclerView
    public void delete(final TextEntity entity) {

        new AlertDialog.Builder(context).setTitle("确认要删除吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", (dialog, which) -> {
                    // 点击“确认”后的操作
//                                删除自动保存的数据
                    TextManager.delete(context, entity.getId());
                    list.remove(entity);
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
