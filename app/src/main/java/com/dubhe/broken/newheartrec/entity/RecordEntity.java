package com.dubhe.broken.newheartrec.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Developer on 2017/7/3.
 */

public class RecordEntity implements Parcelable {
    private String filename;//文件名
    private String time;//最后修改时间

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
