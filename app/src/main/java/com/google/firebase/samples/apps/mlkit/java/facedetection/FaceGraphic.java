// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.samples.apps.mlkit.java.facedetection;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay.Graphic;

import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.lx;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.lx_temp2;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.ly;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.ly_temp2;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mAvgLeftX;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mAvgLeftY;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mAvgNeckX;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mAvgNeckY;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mAvgRightX;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mAvgRightY;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mEarringId;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mLeftx1;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mLeftx2;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mLefty1;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mLefty2;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mMainVal;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mMainValY;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mRightx1;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mRightx2;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mRighty1;
import static com.google.firebase.samples.apps.mlkit.java.LivePreviewActivity.mRighty2;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */

public class FaceGraphic extends Graphic {
    private static final float FACE_POSITION_RADIUS = 4.0f;
    private static final float ID_TEXT_SIZE = 30.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private int facing;


    public int mLeftEarX;
    public int mLeftEarY;
    public int mRightEarX;
    public int mRightEarY;



    public int mCount = 0;


    private final Paint facePositionPaint;
    private final Paint idPaint;
    private final Paint boxPaint;

    private volatile FirebaseVisionFace firebaseVisionFace;

    private final Bitmap overlayBitmap;
    private final Bitmap necklaceOverlayBitmap;
    private final Bitmap mHairStyleOverlayBitmap;


    public FaceGraphic(GraphicOverlay overlay, FirebaseVisionFace face, int facing, Bitmap overlayBitmap, Bitmap necklaceOverlayBitmap, Bitmap mHairStyleOverlayBitmap) {
        super(overlay);

        firebaseVisionFace = face;
        this.facing = facing;
        this.overlayBitmap = overlayBitmap;
        this.necklaceOverlayBitmap = necklaceOverlayBitmap;
        this.mHairStyleOverlayBitmap = mHairStyleOverlayBitmap;
        final int selectedColor = Color.WHITE;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);

        idPaint = new Paint();
        idPaint.setColor(selectedColor);
        idPaint.setTextSize(ID_TEXT_SIZE);

        boxPaint = new Paint();
        boxPaint.setColor(selectedColor);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {


        FirebaseVisionFace face = firebaseVisionFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        // An offset is used on the Y axis in order to draw the circle, face id and happiness level in the top area
        // of the face's bounding box
//        float x = translateX(face.getBoundingBox().centerX());
//        float y = translateY(face.getBoundingBox().centerY());
//        canvas.drawCircle(x, y - 4 * ID_Y_OFFSET, FACE_POSITION_RADIUS, facePositionPaint);
//        canvas.drawText("id: " + face.getTrackingId(), x + ID_X_OFFSET, y - 3 * ID_Y_OFFSET, idPaint);
//        canvas.drawText(
//                "happiness: " + String.format("%.2f", face.getSmilingProbability()),
//                x + ID_X_OFFSET * 3,
//                y - 2 * ID_Y_OFFSET,
//                idPaint);
//        if (facing == CameraSource.CAMERA_FACING_FRONT) {
//            canvas.drawText(
//                    "right eye: " + String.format("%.2f", face.getRightEyeOpenProbability()),
//                    x - ID_X_OFFSET,
//                    y,
//                    idPaint);
//            canvas.drawText(
//                    "left eye: " + String.format("%.2f", face.getLeftEyeOpenProbability()),
//                    x + ID_X_OFFSET * 6,
//                    y,
//                    idPaint);
//        } else {
//            canvas.drawText(
//                    "left eye: " + String.format("%.2f", face.getLeftEyeOpenProbability()),
//                    x - ID_X_OFFSET,
//                    y,
//                    idPaint);
//            canvas.drawText(
//                    "right eye: " + String.format("%.2f", face.getRightEyeOpenProbability()),
//                    x + ID_X_OFFSET * 6,
//                    y,
//                    idPaint);
//        }

        // Draws a bounding box around the face.
//        float xOffset = scaleX(face.getBoundingBox().width() / 2.0f);
//        float yOffset = scaleY(face.getBoundingBox().height() / 2.0f);
//        float left = x - xOffset;
//        float top = y - yOffset;
//        float right = x + xOffset;
//        float bottom = y + yOffset;
//        canvas.drawRect(left, top, right, bottom, boxPaint);

        // draw landmarks
//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.LEFT_EAR);
//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.MOUTH_LEFT);
//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.LEFT_EYE);
//        drawBitmapOverLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.NOSE_BASE);

//        //System.out.println("********************************************************************************");
        FirebaseVisionFaceLandmark landmark = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
        assert landmark != null;
        FirebaseVisionPoint point = landmark.getPosition();
        System.out.println("Check 2 FirebaseVisionFaceLandmark.NOSE_BASE "+ point.getX() +" = "+ point.getY()+" = "+ point.getZ());
//            canvas.drawCircle( translateX(point.getX()), translateY(point.getY()), 10f, idPaint);



        drawBitmapOverLandmarkNecklacePosition(canvas, face, FirebaseVisionFaceLandmark.MOUTH_BOTTOM);

        if (point.getX() < 200){
            drawBitmapOverLandmarkPositionRight(canvas, face, FirebaseVisionFaceLandmark.RIGHT_EAR);
//            FirebaseVisionFaceLandmark landmarkRIGHT_EAR = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
//            assert landmarkRIGHT_EAR != null;
//            FirebaseVisionPoint pointRIGHT_EAR = landmarkRIGHT_EAR.getPosition();
            //System.out.println("Check  RIGHT_EAR "+ pointRIGHT_EAR.getX() +" = "+ pointRIGHT_EAR.getY());
        }

        if (point.getX() > 165){
            drawBitmapOverLandmarkPositionLeft(canvas, face, FirebaseVisionFaceLandmark.LEFT_EAR);
//            FirebaseVisionFaceLandmark landmarkLEFT_EAR = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
//            assert landmarkLEFT_EAR != null;
//            FirebaseVisionPoint pointLEFT_EAR = landmarkLEFT_EAR.getPosition();
            //System.out.println("Check  Left_EAR "+ pointLEFT_EAR.getX() +" = "+ pointLEFT_EAR.getY());
        }

        drawBitmapOverLandmarkHeadPosition(canvas, face, FirebaseVisionFaceLandmark.NOSE_BASE);


//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.LEFT_CHEEK);
//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.RIGHT_CHEEK);

//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.RIGHT_EAR);
//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.RIGHT_EYE);
//        drawLandmarkPosition(canvas, face, FirebaseVisionFaceLandmark.MOUTH_RIGHT);
    }


    private void drawLandmarkPosition(Canvas canvas, FirebaseVisionFace face, int landmarkID) {
        FirebaseVisionFaceLandmark landmark = face.getLandmark(landmarkID);
        if (landmark != null) {
            FirebaseVisionPoint point = landmark.getPosition();
            canvas.drawCircle(
                    translateX(point.getX()),
                    translateY(point.getY()),
                    10f, idPaint);
        }
    }

    private void drawBitmapOverLandmarkPositionRight(Canvas canvas, FirebaseVisionFace face, int landmarkID) {
        int left,right,top,bottom;
        FirebaseVisionFaceLandmark landmark = face.getLandmark(landmarkID);
        if (landmark == null) {
            return;
        }

        FirebaseVisionPoint point = landmark.getPosition();

        if (overlayBitmap != null) {
            float imageEdgeSizeBasedOnFaceSize = (face.getBoundingBox().width() / 2.8f);


//            mRightx5 = mRightx4;
//            mRightx4 = mRightx3;
//            mRightx3 = mRightx2;
            mRightx2 = mRightx1;
            mRightx1 = point.getX();

//            mRighty5 = mRighty4;
//            mRighty4 = mRighty3;
//            mRighty3 = mRighty2;
            mRighty2 = mRighty1;
            mRighty1 = point.getY();


            mAvgRightX = ( mRightx2 + mRightx1) / 2 ;
            //System.out.println("Demo Values mRightx " +" mRightx5 "+mRightx5+" mRightx4 "+mRightx4+" mRightx3 "+mRightx3+" mRightx2 "+mRightx2+" mRightx1 "+mRightx1 +" Avg = "+mAvgRightX);

            mAvgRightY = ( mRighty2 + mRighty1) / 2 ;
            //System.out.println("Demo Values mRighty " +" mRighty5 "+mRighty5+" mRighty4 "+mRighty4+" mRighty3 "+mRighty3+" mRighty2 "+mRighty2+" mRighty1 "+mRighty1 +" Avg = "+mAvgRightY);


//            mRMainVal = rx;
//            rx = point.getX();
//            rx_temp2 = (mRMainVal + rx) / 2 ;
//            mCoordinatesArrayList.clear();
//            mCoordinatesArrayList.add(new Coordinates(mRMainVal,point.getY()));
//            //System.out.println("Values mRMainVal "+mRMainVal +" rx "+rx +" rx_temp "+rx_temp + "rx_temp2 " +rx_temp2 );
//
//
//            mRMainValY = ry;
//            ry = point.getY();
//            ry_temp2 = (mRMainValY + ly) / 2 ;
//            //System.out.println("Values mRMainVal "+mRMainValY +" ry "+ry +" ry_temp "+ry_temp + "ry_temp2 " +ry_temp2);



            if(mEarringId == 8 || mEarringId == 9 || mEarringId == 11) {
                left = (int) (translateX(mAvgRightX-8) - imageEdgeSizeBasedOnFaceSize);
                top = (int) (translateY(mAvgRightY+30) - imageEdgeSizeBasedOnFaceSize);
                right = (int) (translateX(mAvgRightX+30) + imageEdgeSizeBasedOnFaceSize+20);
                bottom = (int) (translateY(mAvgRightY) + imageEdgeSizeBasedOnFaceSize+40);
            }else {

                left = (int) (translateX(mAvgRightX-8) - imageEdgeSizeBasedOnFaceSize);
                top = (int) (translateY(mAvgRightY+25) - imageEdgeSizeBasedOnFaceSize);
                right = (int) (translateX(mAvgRightX+20) + imageEdgeSizeBasedOnFaceSize+20);
                bottom = (int) (translateY(mAvgRightY) + imageEdgeSizeBasedOnFaceSize+180);
            }

            canvas.drawBitmap(overlayBitmap,null,new Rect(left, top, right+20, bottom),null);

        }

    }

    private void drawBitmapOverLandmarkPositionLeft(Canvas canvas, FirebaseVisionFace face, int landmarkID) {
        int left,right,top,bottom;
        FirebaseVisionFaceLandmark landmark = face.getLandmark(landmarkID);
        if (landmark == null) {
            return;
        }

        FirebaseVisionPoint point = landmark.getPosition();

        if (overlayBitmap != null) {
            float imageEdgeSizeBasedOnFaceSize = (face.getBoundingBox().width() / 2.8f);



//            mLeftx5 = mLeftx4;
//            mLeftx4 = mLeftx3;
//            mLeftx3 = mLeftx2;
            mLeftx2 = mLeftx1;
            mLeftx1 = point.getX();

//            mLefty5 = mLefty4;
//            mLefty4 = mLefty3;
//            mLefty3 = mLefty2;
            mLefty2 = mLefty1;
            mLefty1 = point.getY();


            mAvgLeftX = ( mLeftx2 + mLeftx1) / 2 ;
            //System.out.println("Demo Values mLeftx " +" mLeftx5 "+mLeftx5+" mLeftx4 "+mLeftx4+" mLeftx3 "+mLeftx3+" mLeftx2 "+mLeftx2+" mLeftx1 "+mLeftx1 +" Avg = "+mAvgLeftX);

            mAvgLeftY = (mLefty2 + mLefty1) / 2 ;
            //System.out.println("Demo Values mLefty " +" mLefty5 "+mLefty5+" mLefty4 "+mLefty4+" mLefty3 "+mLefty3+" mLefty2 "+mLefty2+" mLefty1 "+mLefty1 +" Avg = "+mAvgLeftY);





            mMainVal = lx;
            lx = point.getX();
            lx_temp2 = (mMainVal + lx) / 2 ;
//            mCoordinatesArrayList.clear();
//            mCoordinatesArrayList.add(new Coordinates(mMainVal,point.getY()));
            //System.out.println("Values mMainVal "+mMainVal +" lx "+lx +" lx_temp "+lx_temp + "lx_temp2 " +lx_temp2);


            mMainValY = ly;
            ly = point.getY();
            ly_temp2 = (mMainValY + ly) / 2 ;
            //System.out.println("Values mMainVal "+mMainValY +" ly "+ly +" ly_temp "+ly_temp + "ly_temp2 " +ly_temp2);




            if(mEarringId == 8 || mEarringId == 9 || mEarringId == 11){
                left = (int) (translateX( lx_temp2-8) - imageEdgeSizeBasedOnFaceSize);
                top = (int) (translateY(ly_temp2+30) - imageEdgeSizeBasedOnFaceSize);
                right = (int) (translateX(lx_temp2+30) + imageEdgeSizeBasedOnFaceSize+20);
                bottom = (int) (translateY(ly_temp2) + imageEdgeSizeBasedOnFaceSize+40);
            }else {

                left = (int) (translateX( lx_temp2-8) - imageEdgeSizeBasedOnFaceSize);
                top = (int) (translateY(ly_temp2+25) - imageEdgeSizeBasedOnFaceSize);
                right = (int) (translateX(lx_temp2+20) + imageEdgeSizeBasedOnFaceSize+20);
                bottom = (int) (translateY(ly_temp2) + imageEdgeSizeBasedOnFaceSize+180);

            }




            //System.out.println("Left Values "+left +" = "+  top +" = "+  right+20 +" = "+ bottom);
            canvas.drawBitmap(overlayBitmap,null,new Rect(left, top, right+20, bottom),null);
        }

    }

    private void drawBitmapOverLandmarkNecklacePosition(Canvas canvas, FirebaseVisionFace face, int landmarkID) {
        FirebaseVisionFaceLandmark landmark = face.getLandmark(landmarkID);
        if (landmark == null) {
            return;
        }

        FirebaseVisionPoint point = landmark.getPosition();

        if (necklaceOverlayBitmap != null) {
            float imageEdgeSizeBasedOnFaceSize = (face.getBoundingBox().width());


//            mNeckx5 = mNeckx4;
//            mNeckx4 = mNeckx3;
//            mNeckx3 = mNeckx2;
//            mNeckx2 = mNeckx1;
//            mNeckx1 = point.getX();
//
//            mNecky5 = mNecky4;
//            mNecky4 = mNecky3;
//            mNecky3 = mNecky2;
//            mNecky2 = mNecky1;
//            mNecky1 = point.getY();
//
//
//            mAvgNeckX = ( mNeckx2 + mNeckx1) / 2 ;
//            System.out.println("Demo Values mNeckx " +" mNeckx5 "+mNeckx5+" mNeckx4 "+mNeckx4+" mNeckx3 "+mNeckx3+" mNeckx2 "+mNeckx2+" mNeckx1 "+mNeckx1 +" Avg = "+mAvgNeckX);
//
//            mAvgNeckY = ( mNecky2 + mNecky1) / 2 ;
//            System.out.println("Demo Values mNecky " +" mNecky5 "+mNecky5+" mNecky4 "+mNecky4+" mNecky3 "+mNecky3+" mNecky2 "+mNecky2+" mNecky1 "+mNecky1 +" Avg = "+mAvgNeckY);

            mAvgNeckX = point.getX();
//            System.out.println("mAvgNeckX "+mAvgNeckX);

            mAvgNeckY = point.getY();
//            System.out.println("mAvgNeckY "+mAvgNeckY);

            if (mAvgNeckX > 240 ) {
                mAvgNeckX = 190;
            } else {
                mAvgNeckX = 0;
            }

            if (mAvgNeckX < 125 ) {
                mAvgNeckX = 190;
            } else {
                mAvgNeckX = 0;
            }


            if (mAvgNeckY > 300 ) {
                mAvgNeckY = 280;
            } else {
                mAvgNeckY = 0;
            }

            if (mAvgNeckY < 125 ) {
                mAvgNeckY = 280;
            } else {
                mAvgNeckY = 0;
            }


            if(mAvgNeckX == 0 || mAvgNeckY ==0){
//                System.out.println("Hide necklace");
            } else {
                int left = (int) (translateX(mAvgNeckX) - imageEdgeSizeBasedOnFaceSize-30);
                int top = (int) (translateY(mAvgNeckY) - imageEdgeSizeBasedOnFaceSize+250);
                int right = (int) (translateX(mAvgNeckX) + imageEdgeSizeBasedOnFaceSize+70);
                int bottom = (int) (translateY(mAvgNeckY) + imageEdgeSizeBasedOnFaceSize+300);
                canvas.drawBitmap(necklaceOverlayBitmap,null,new Rect(left, top, right, bottom),null);
            }


        }
    }

    private void drawBitmapOverLandmarkHeadPosition(Canvas canvas, FirebaseVisionFace face, int landmarkID) {
        FirebaseVisionFaceLandmark landmark = face.getLandmark(landmarkID);
        if (landmark == null) {
            return;
        }

        FirebaseVisionPoint point = landmark.getPosition();

        if (mHairStyleOverlayBitmap != null) {
            float imageEdgeSizeBasedOnFaceSize = (face.getBoundingBox().width());

            int left = (int) (translateX(point.getX()) - imageEdgeSizeBasedOnFaceSize-290);
            int top = (int) (translateY(point.getY()) - imageEdgeSizeBasedOnFaceSize-280);
            int right = (int) (translateX(point.getX()) + imageEdgeSizeBasedOnFaceSize+240);
            int bottom = (int) (translateY(point.getY()) + imageEdgeSizeBasedOnFaceSize+500);
            canvas.drawBitmap(mHairStyleOverlayBitmap,null,new Rect(left, top, right, bottom),null);


        }
    }

}
