package com.dubhe.broken.newheartrec.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.entity.TextEntity;

import org.w3c.dom.Text;

import java.util.List;

public class TextAdapter extends BaseSwipeAdapter {

    private List<TextEntity> list;
    private Context context;
    private OnItemClickListener onItemClickListener = null;
    private OnDeleteClickListener onDeleteClickListener = null;


    public TextAdapter(Context context, List<TextEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_text;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.text_item, null);
    }

    //设置回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //设置回调接口
    public interface OnDeleteClickListener {
        void onDeleteClick(View view, int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public void fillValues(final int position, View view) {
        TextView buttonDeleteTextitem = view.findViewById(R.id.button_delete_textitem);
        TextView textItemSubstance = view.findViewById(R.id.text_item_substance);
        TextView textItemTime = view.findViewById(R.id.text_item_time);
        ConstraintLayout textLayoutItem = view.findViewById(R.id.text_layout_item);
        SwipeLayout swipeText = view.findViewById(R.id.swipe_text);
        swipeText.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeText.addDrag(SwipeLayout.DragEdge.Right, view.findViewById(R.id.linear_text_item));
        swipeText.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        textLayoutItem.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
        buttonDeleteTextitem.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(v, position));
        textItemTime.setText(list.get(position).getTime());
        textItemSubstance.setText(list.get(position).getSubstance());
        TextView textItemImgbtnArrow = view.findViewById(R.id.text_item_imgbtn_arrow);
        textItemImgbtnArrow.setOnClickListener(v -> {
//            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate90));//旋转动画，时间不同步弃用
            switch (textItemImgbtnArrow.getBackground().getLevel()) {
                case 0:
                    textItemImgbtnArrow.getBackground().setLevel(1);
                    textItemSubstance.setMaxLines(20);
                    break;
                case 1:
                    textItemImgbtnArrow.getBackground().setLevel(0);
                    textItemSubstance.setMaxLines(1);
                    break;
            }
        });
    }

}



