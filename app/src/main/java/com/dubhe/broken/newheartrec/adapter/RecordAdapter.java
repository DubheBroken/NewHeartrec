package com.dubhe.broken.newheartrec.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.entity.RecordEntity;
import com.dubhe.broken.newheartrec.utils.ClickUtils;
import com.dubhe.broken.newheartrec.utils.OnMultiClickListener;
import com.dubhe.broken.newheartrec.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;

/**
 * Created by Developer on 2017/7/3.
 */

public class RecordAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<RecordEntity> list;
    private OnItemClickListener onItemClickListener = null;
    private MediaPlayer player;
    int playingItem = -1;//正在播放的项目，-1表示无
    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 300L;
    private OnDeleteClickListener onDeleteClickListener = null;

    public RecordAdapter(Context context, List<RecordEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_record;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.record_item_layout, parent, false);
    }

    @Override
    public void fillValues(int position, View view) {
        TextView buttonDeleteRecorditem = view.findViewById(R.id.button_delete_recorditem);
        LinearLayout linearRecordItem = view.findViewById(R.id.linear_record_item);
        TextView recordItemTime = view.findViewById(R.id.record_item_time);
        TextView recordItemPlay = view.findViewById(R.id.record_item_play);
        LinearLayout recordLayoutItem = view.findViewById(R.id.record_layout_item);
        SwipeLayout swipeLayout = view.findViewById(R.id.swipe_record);
        //        处理时间字符串
        String time = list.get(position).getTime();
        time = time.substring(6, time.length());
        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        time = timeFormatter(time, oldFormatter, newFormatter);

        //给控件赋值
        recordItemTime.setText(time);
        recordItemPlay.setOnClickListener(v -> {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                play(position, recordItemPlay);
                mLastClickTime = nowTime;
            }
        });
        recordLayoutItem.setOnClickListener(v -> {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                play(position, recordItemPlay);
                mLastClickTime = nowTime;
            }
        });
        buttonDeleteRecorditem.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(v, position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, view.findViewById(R.id.linear_record_item));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
//        recordItemPlay.setOnClickListener(v -> {
//            if (!ClickUtils.isFastClick())
//                play(position, recordItemPlay);
//        });
//        recordLayoutItem.setOnClickListener(v -> {
//            if (!ClickUtils.isFastClick())
//                play(position, recordItemPlay);
//        });

        //通过为条目设置点击事件触发回调
//        if (onItemClickListener != null) {
//        recordLayoutItem.setOnClickListener(v -> recordItemPlay.performClick());
//        }
    }

    //设置回调接口
    public interface OnDeleteClickListener {
        void onDeleteClick(View view, int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void play(int position, TextView recordItemPlay) {
        if (player == null) {
            if (Build.VERSION.SDK_INT < 23) {
                player = MediaPlayer.create(context, Uri.parse("/mnt/" + list.get(position).getFilename()));
            } else {
                player = new MediaPlayer();
                String uri = "/mnt/" + list.get(position).getFilename();
                File file = new File(uri);
                try {
                    FileInputStream fis = new FileInputStream(file);
                    player.setDataSource(fis.getFD());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!player.isPlaying()) {
            start();
            recordItemPlay.setBackgroundResource(R.drawable.btn_pause);
        } else {
            player.stop();
            recordItemPlay.setBackgroundResource(R.drawable.btn_play);
        }
        player.setOnCompletionListener(mp -> {
            player = null;
            playingItem = -1;
            recordItemPlay.setBackgroundResource(R.drawable.btn_play);
        });
    }


    public void start() {
        if (!player.isPlaying()) {
            try {
                if (Build.VERSION.SDK_INT > 23) {
                    player.prepare();
                }
                player.start();
            } catch (IllegalStateException e) {
                Log.e("---播放参数异常---", e.toString(), e);
            } catch (IOException e) {
                Log.e("---播放IO异常---", e.toString(), e);
                ToastUtil.showToast(context, "播放失败，文件损坏");
            }
        }
    }

    public void stop() {
        player.stop();
        player.release();
    }

    //设置回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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

    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param time       String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public final String timeFormatter(String time, SimpleDateFormat
            oldPattern, SimpleDateFormat newPattern) {
        if (time == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = oldPattern;        // 旧格式
        SimpleDateFormat sdf2 = newPattern;        // 新格式
        Date d = null;
        try {
            d = sdf1.parse(time);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        return sdf2.format(d);
    }


}


