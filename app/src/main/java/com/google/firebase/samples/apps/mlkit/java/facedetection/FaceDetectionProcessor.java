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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.samples.apps.mlkit.R;
import com.google.firebase.samples.apps.mlkit.common.CameraImageGraphic;
import com.google.firebase.samples.apps.mlkit.common.FrameMetadata;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.java.VisionProcessorBase;

import java.io.IOException;
import java.util.List;

/**
 * Face Detector Demo.
 */
public class FaceDetectionProcessor extends VisionProcessorBase<List<FirebaseVisionFace>> {

    private static final String TAG = "FaceDetectionProcessor";

    private final FirebaseVisionFaceDetector detector;

    public Bitmap overlayBitmap;
    public Bitmap necklaceOverlayBitmap;
    public Bitmap mHairStyleOverlayBitmap;

    public FaceDetectionProcessor(Resources resources ,int earringPosition,int neckPosition, int mHairStylePosition) {
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .build();

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        getOverlayType( resources,earringPosition,neckPosition,mHairStylePosition);
    }

    public void getOverlayType(Resources resources,int pos, int neckPosition, int mHairStylePosition) {

        if (pos == 0) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.e2);
        }
        if (pos == 1 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.p1);
        }
        if (pos == 2) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.a1);
        }
        if (pos == 3) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.b1);
        }
        if (pos == 4 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.c1);
        }

        if (pos == 5 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering1);
        }
        if (pos == 6 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering2);
        }
        if (pos == 7 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering3);
        }
        if (pos == 8 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering4);
        }
        if (pos == 9 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering5);
        }
        if (pos == 10 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering6);
        }
        if (pos == 11 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering8);
        }
        if (pos == 12 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering9);
        }
        if (pos == 13 ) {
            overlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.ering10);
        }



        // Necklace........

        if (neckPosition == 100) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.necklace);
        }
        if (neckPosition == 101) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck4);
        }
        if (neckPosition == 102) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck1);
        }
        if (neckPosition == 103) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck2);
        }
        if (neckPosition == 104) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck3);
        }
        if (neckPosition == 105) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck5);
        }
        if (neckPosition == 106) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck6);
        }
        if (neckPosition == 107) {
            necklaceOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.neck7);
        }

        // HairStyles.......

        if (mHairStylePosition == 200) {
            mHairStyleOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.hair2);
        }
        if (mHairStylePosition == 201) {
            mHairStyleOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.hair3);
        }
        if (mHairStylePosition == 202) {
            mHairStyleOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.hair4);
        }
        if (mHairStylePosition == 203) {
            mHairStyleOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.hair5);
        }
        if (mHairStylePosition == 204) {
            mHairStyleOverlayBitmap = BitmapFactory.decodeResource(resources, R.drawable.hair6);
        }



    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionFace>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionFace> faces,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        for (int i = 0; i < faces.size(); ++i) {
            FirebaseVisionFace face = faces.get(i);

            int cameraFacing =
                    frameMetadata != null ? frameMetadata.getCameraFacing() :
                            Camera.CameraInfo.CAMERA_FACING_BACK;
            FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay, face, cameraFacing, overlayBitmap,necklaceOverlayBitmap,mHairStyleOverlayBitmap);
            graphicOverlay.add(faceGraphic);
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Face detection failed " + e);
    }
}
