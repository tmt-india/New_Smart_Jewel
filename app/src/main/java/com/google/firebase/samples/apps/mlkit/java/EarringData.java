package com.google.firebase.samples.apps.mlkit.java;

import java.io.Serializable;

public class EarringData implements Serializable {

    public String mName;
    public int mImgUrl;


    public EarringData(int mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public int getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(int mImgUrl) {
        this.mImgUrl = mImgUrl;
    }
}
