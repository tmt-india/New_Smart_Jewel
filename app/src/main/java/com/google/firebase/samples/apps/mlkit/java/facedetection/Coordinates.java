package com.google.firebase.samples.apps.mlkit.java.facedetection;

import java.io.Serializable;

public class Coordinates implements Serializable {


    public float mValX ;
    public float mValY ;

    public Coordinates(float mValX, float mValY) {
        this.mValX = mValX;
        this.mValY = mValY;
    }

    public float getmValX() {
        return mValX;
    }

    public void setmValX(float mValX) {
        this.mValX = mValX;
    }

    public float getmValY() {
        return mValY;
    }

    public void setmValY(float mValY) {
        this.mValY = mValY;
    }
}
