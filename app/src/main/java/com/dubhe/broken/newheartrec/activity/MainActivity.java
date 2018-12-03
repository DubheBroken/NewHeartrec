package com.dubhe.broken.newheartrec.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dubhe.broken.newheartrec.R;
import com.dubhe.broken.newheartrec.entity.RecordEntity;
import com.dubhe.broken.newheartrec.entity.TextEntity;
import com.dubhe.broken.newheartrec.fragment.PaintListFragment;
import com.dubhe.broken.newheartrec.fragment.RecordListFragment;
import com.dubhe.broken.newheartrec.fragment.TextListFragment;
import com.dubhe.broken.newheartrec.utils.FragmentHelper;
import com.dubhe.broken.newheartrec.utils.SqliteHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;
import java.util.concurrent.ExecutorService;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private FloatingActionButton fabNewText;
    private FloatingActionButton fabNewPaint;
    private FloatingActionButton fabNewRecord;
    private FloatingActionsMenu btnNewone;
    private DrawerLayout drawerLayout;
    private SqliteHelper sqliteHelper;
    private Context context = this;
    private List<TextEntity> listText;
    private static final int REQUEST_PERMISSIONS = 0x044;
    private List<RecordEntity> listRecord;


    private Runnable switchRunnable, loopRunnable, toNormalRunnable, recoverRunnable;
    private Thread loopThread;
    private ExecutorService cacheThreadPool;
    private boolean running = true;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initView();
    }

    private void initView() {
        initPermission();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fabNewText = findViewById(R.id.fab_new_text);
        fabNewText.setOnClickListener(this);
        fabNewPaint = findViewById(R.id.fab_new_paint);
        fabNewPaint.setOnClickListener(this);
        fabNewRecord = findViewById(R.id.fab_new_record);
        fabNewRecord.setOnClickListener(this);
        btnNewone = findViewById(R.id.btn_newone);

        initAdapter(1);
    }

    /**
     * 初始化相关权限
     * 适配6.0+手机的运行时权限
     */
    public void initPermission() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.SET_WALLPAPER,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.RECORD_AUDIO

        };
        //检查权限
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //判断权限是否被拒绝过
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
            } else {
                //注册相机权限
                ActivityCompat.requestPermissions(this,
                        permissions,
                        REQUEST_PERMISSIONS);
            }
        }
    }

    //    更新数据
    private void initAdapter(final int i) {
        Observable.create((ObservableOnSubscribe<Fragment>) emitter -> {
            switch (i) {
                case 1:
                    emitter.onNext(new TextListFragment());
                    break;
                case 3:
                    //初始化录音适配器
                    emitter.onNext(new RecordListFragment());
                    break;
            }
            emitter.onComplete();
        }).subscribe(new Observer<Fragment>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Fragment fragment) {
                FragmentHelper.switchFragment(fragment, MainActivity.this);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("---操作sqlite---", e.getMessage());
            }

            @Override
            public void onComplete() {
//                switch (page) {
//                    case 1:
//
//                        break;
//                    case 2:
//                        FragmentHelper.switchFragment(new RecordListFragment().newInstance(listRecord), MainActivity.this);
//                        break;
//                }
            }
        });
    }
//        thread = new Thread(() -> {
//            Log.i("----Thread----", Integer.toString(i));
//            Message message = new Message();
//            switch (i) {
//                case 1:
////                初始化文字适配器
//                    listText = new ArrayList<>();
////                查询数据
//                    SQLiteDatabase db = null;
//                    db = sqliteHelper.getWritableDatabase();
//                    Cursor cursor = null;
//                    String sql = "select * from " + Constant.TABLE_NAME + " order by " + Constant.TIME + " desc;";
//                    cursor = DbManager.selectDataBySql(db, sql, null);
//                    if (cursor != null && db.isOpen()) {
//                        while (cursor.moveToNext()) {
//                            TextEntity text_entity = new TextEntity();
//                            text_entity.setId(cursor.getString(cursor.getColumnIndex("id")));
//                            text_entity.setTime(cursor.getString(cursor.getColumnIndex("time")));
//                            text_entity.setSubstance(cursor.getString(cursor.getColumnIndex("substance")));
//                            listText.add(text_entity);
//                        }
//                    }
//                    sqliteHelper.removeOperation();
//                    while (sqliteHelper.getOperation() > 0) {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    //关闭数据库
//                    db.close();
//                    break;
//                case 2:
//                    //初始化图片适配器
//                    listPaint = new ArrayList<>();
//                    getImage(listPaint);
//                    paintAdapter = new Paint_Adapter(Main_activity.this, listPaint);
//                    paintAdapter.setOnItemClickListener(new Paint_Adapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            Intent intent = new Intent(Main_activity.this, Painter_activity.class);
//                            intent.putExtra("fileName", listPaint.get(position).getFilename());//将被点击的item文件名传递到新活动
//                            startActivity(intent);
//                        }
//                    });
//                    paintAdapter.setOnItemLongClickListener(new Paint_Adapter.OnItemLongClickListener() {
//                        @Override
//                        public void onItemLongClick(View view, int position) {
//                            page = 2;
//                            showDeleteDia(listPaint.get(position).getFilename());
//                        }
//                    });
//                    break;

    //    //同步seekbar与进度条时间
//    private void updateSeekBar() {
//        runed = true;
//        loopRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (running) {
//                    try {
//                        for (int i = 0; i < viewHolderList.size(); i++) {
//                            if (i == playingItem) {
//                                continue;
//                            }
//                            final TextView image = viewHolderList.get(i).image;
//                            final TextView time = viewHolderList.get(i).time;
//                            recoverRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (running) {
//                                        image.setBackgroundResource(R.drawable.btn_play);
//                                        time.setBackgroundResource(R.drawable.bg_player_normal);
//                                    }
//                                }
//                            };
//                            if (running) {
//                                runOnUiThread(recoverRunnable);
//                            }
//
//                        }
//                        if (playingItem >= 0)
//                            displayAnim(viewHolderList.get(playingItem).time, true);
//                        Thread.sleep(200);
//                        if (playingItem >= 0)
//                            displayAnim(viewHolderList.get(playingItem).time, false);
//                        Thread.sleep(200);
//                        updateSeekBar();
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        loopThread = new Thread(loopRunnable);
//        if (running) {
//            cacheThreadPool.execute(loopThread);
//        }
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnNewone.collapse();
//        sqliteHelper = DbManager.getIntance(this);
//        initView();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_text:
                FragmentHelper.switchFragment(new TextListFragment(), this);
                break;
            case R.id.nav_paint:
                FragmentHelper.switchFragment(new PaintListFragment(), this);
                break;
            case R.id.nav_record:
                FragmentHelper.switchFragment(new RecordListFragment(), this);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_new_text:
                startActivity(new Intent(context, NewOneActivity.class));
                break;
            case R.id.fab_new_paint:
                startActivity(new Intent(context, PainterActivity.class));
                break;
            case R.id.fab_new_record:
                startActivity(new Intent(context, RecordActivity.class));
                break;
        }
    }
}
