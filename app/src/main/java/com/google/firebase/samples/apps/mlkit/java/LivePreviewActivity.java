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
package com.google.firebase.samples.apps.mlkit.java;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.annotation.KeepName;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.google.firebase.samples.apps.mlkit.R;
import com.google.firebase.samples.apps.mlkit.common.CameraSource;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.databinding.ActivityLivePreviewBinding;
import com.google.firebase.samples.apps.mlkit.java.automl.AutoMLImageLabelerProcessor;
import com.google.firebase.samples.apps.mlkit.java.automl.AutoMLImageLabelerProcessor.Mode;
import com.google.firebase.samples.apps.mlkit.java.barcodescanning.BarcodeScanningProcessor;
import com.google.firebase.samples.apps.mlkit.java.custommodel.CustomImageClassifierProcessor;
import com.google.firebase.samples.apps.mlkit.java.facedetection.Coordinates;
import com.google.firebase.samples.apps.mlkit.java.facedetection.FaceContourDetectorProcessor;
import com.google.firebase.samples.apps.mlkit.java.facedetection.FaceDetectionProcessor;
import com.google.firebase.samples.apps.mlkit.java.imagelabeling.ImageLabelingProcessor;
import com.google.firebase.samples.apps.mlkit.java.objectdetection.ObjectDetectorProcessor;
import com.google.firebase.samples.apps.mlkit.common.preference.SettingsActivity;
import com.google.firebase.samples.apps.mlkit.common.preference.SettingsActivity.LaunchSource;
import com.google.firebase.samples.apps.mlkit.java.textrecognition.TextRecognitionProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo app showing the various features of ML Kit for Firebase. This class is used to
 * set up continuous frame processing on frames from a camera source.
 */
@KeepName
public final class LivePreviewActivity extends AppCompatActivity
        implements OnRequestPermissionsResultCallback,
        OnItemSelectedListener,
        EarringDataAdapter.ListAdapterListener,
        CompoundButton.OnCheckedChangeListener {
    private static final String FACE_DETECTION = "Face Detection";
    private static final String OBJECT_DETECTION = "Object Detection";
    private static final String AUTOML_IMAGE_LABELING = "AutoML Vision Edge";
    private static final String TEXT_DETECTION = "Text Detection";
    private static final String BARCODE_DETECTION = "Barcode Detection";
    private static final String IMAGE_LABEL_DETECTION = "Label Detection";
    private static final String CLASSIFICATION_QUANT = "Classification (quantized)";
    private static final String CLASSIFICATION_FLOAT = "Classification (float)";
    private static final String FACE_CONTOUR = "Face Contour";
    private static final String TAG = "LivePreviewActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private String selectedModel = FACE_DETECTION;
    private ActivityLivePreviewBinding binding;


    public RecyclerView mEarringRecyclerVw,mNecklaceRecyclerVw,mHairStyleRecyclerVw;
    EarringDataAdapter mEarringDataAdapter;
//    NecklaceDataAdapter mNecklaceDataAdapter;
//    HairStyleDataAdapter mHairStyleDataAdapter;
    ArrayList<EarringData> mEarringDataArrayList;

    public static float mMainVal = 0;
    public static float lx = 0;
    public static float lx_temp = 0;
    public static float lx_temp2 = 0;

    public static float mMainValY = 0;
    public static float ly = 0;
    public static float ly_temp = 0;
    public static float ly_temp2 = 0;



    public static float mRMainVal = 0;
    public static float rx = 0;
    public static float rx_temp = 0;
    public static float rx_temp2 = 0;

    public static float mRMainValY = 0;
    public static float ry = 0;
    public static float ry_temp = 0;
    public static float ry_temp2 = 0;

    public static float a = 0 ,b = 0,c = 0,d = 0,e = 0;

    public static float mRightx1 = 0, mRightx2 = 0, mRightx3 = 0, mRightx4 = 0, mRightx5 = 0;
    public static float mAvgRightX = 0;
    public static float mRighty1 = 0, mRighty2 = 0, mRighty3 = 0, mRighty4 = 0, mRighty5 = 0;
    public static float mAvgRightY = 0;

    public static float mLeftx1 = 0, mLeftx2 = 0, mLeftx3 = 0, mLeftx4 = 0, mLeftx5 = 0;
    public static float mLefty1 = 0, mLefty2 = 0, mLefty3 = 0, mLefty4 = 0, mLefty5 = 0;
    public static float mAvgLeftX = 0;
    public static float mAvgLeftY = 0;


    public static float mNeckx1 = 0, mNeckx2 = 0, mNeckx3 = 0, mNeckx4 = 0, mNeckx5 = 0;
    public static float mNecky1 = 0, mNecky2 = 0, mNecky3 = 0, mNecky4 = 0, mNecky5 = 0;
    public static float mAvgNeckX = 0;
    public static float mAvgNeckY = 0;

    public static ArrayList<Coordinates> mCoordinatesArrayList ;
    public static ArrayList<Coordinates> mMainCoordinatesArrayList ;

    public static int mEarringId = 30;
    public static int mNecklaceId = 30;
    public static int mHairStyleId = 30;

    FloatingActionButton mFabClearAll,mFabEarrings,mFabNecklace,mFabHairStyle;
    public static ImageView mImgOval;
    private GraphicOverlay graphicOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        binding = ActivityLivePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mCoordinatesArrayList = new ArrayList<>();
        mMainCoordinatesArrayList = new ArrayList<>();

        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        mEarringRecyclerVw = findViewById(R.id.earingRecyclerVw);
        mNecklaceRecyclerVw = findViewById(R.id.necklaceRecyclerVw);
        mHairStyleRecyclerVw = findViewById(R.id.hairStyleRecyclerVw);

        mFabClearAll = findViewById(R.id.fabCamSwitch);
        mFabEarrings = findViewById(R.id.fabEarring);
        mFabNecklace = findViewById(R.id.fabNecklace);
        mFabHairStyle = findViewById(R.id.fabHairStyle);
        mImgOval = findViewById(R.id.imgOval);


        mFabClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEarringRecyclerVw.setVisibility(View.GONE);
                mNecklaceRecyclerVw.setVisibility(View.GONE);
                mHairStyleRecyclerVw.setVisibility(View.GONE);
                mEarringId = 30;
                mNecklaceId = 30;
                mHairStyleId = 30;
                cameraSource.setMachineLearningFrameProcessor(new FaceDetectionProcessor(getResources(),mEarringId,mNecklaceId,mHairStyleId));
            }
        });

        mFabEarrings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mEarringDataArrayList.clear();
                loadData();
                showEarringViewData();
                mEarringRecyclerVw.setVisibility(View.VISIBLE);
                mNecklaceRecyclerVw.setVisibility(View.GONE);
                mHairStyleRecyclerVw.setVisibility(View.GONE);
            }
        });

        List<String> options = new ArrayList<>();
        options.add(FACE_CONTOUR);
        options.add(FACE_DETECTION);
        options.add(AUTOML_IMAGE_LABELING);
        options.add(OBJECT_DETECTION);
        options.add(TEXT_DETECTION);
        options.add(BARCODE_DETECTION);
        options.add(IMAGE_LABEL_DETECTION);
        options.add(CLASSIFICATION_QUANT);
        options.add(CLASSIFICATION_FLOAT);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_style,
                options);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        binding.spinner.setAdapter(dataAdapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.facingSwitch.setVisibility(View.VISIBLE);

        binding.facingSwitch.setOnCheckedChangeListener(this);
        // Hide the toggle button if there is only 1 camera
//        if (Camera.getNumberOfCameras() == 1) {
//            binding.facingSwitch.setVisibility(View.VISIBLE);
//        }

        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        } else {
            getRuntimePermissions();
        }
    }



    private void loadData() {
        mEarringDataArrayList =new ArrayList<>();
        mEarringDataArrayList.add(new EarringData(R.drawable.e1));
        mEarringDataArrayList.add(new EarringData(R.drawable.p));
        mEarringDataArrayList.add(new EarringData(R.drawable.a));
        mEarringDataArrayList.add(new EarringData(R.drawable.b));
        mEarringDataArrayList.add(new EarringData(R.drawable.c));

        mEarringDataArrayList.add(new EarringData(R.drawable.er1));
        mEarringDataArrayList.add(new EarringData(R.drawable.er2));
        mEarringDataArrayList.add(new EarringData(R.drawable.er3));
        mEarringDataArrayList.add(new EarringData(R.drawable.er4));
        mEarringDataArrayList.add(new EarringData(R.drawable.er5));
        mEarringDataArrayList.add(new EarringData(R.drawable.er6));
        mEarringDataArrayList.add(new EarringData(R.drawable.er8));
        mEarringDataArrayList.add(new EarringData(R.drawable.er9));
        mEarringDataArrayList.add(new EarringData(R.drawable.er10));


    }
    private void showEarringViewData() {
        mEarringDataAdapter = new EarringDataAdapter(LivePreviewActivity.this, mEarringDataArrayList,this);
        mEarringRecyclerVw.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mEarringRecyclerVw.setAdapter(mEarringDataAdapter);
        mEarringDataAdapter.notifyDataSetChanged();
    }
    @Override
    public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedModel = parent.getItemAtPosition(pos).toString();
        Log.d(TAG, "Selected model: " + selectedModel);
        binding.firePreview.stop();
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
            startCameraSource();
        } else {
            getRuntimePermissions();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Set facing");
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
                Toast.makeText(
                        getApplicationContext(),
                        "Front Cam selected",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
                Toast.makeText(
                        getApplicationContext(),
                        "Back Cam selected",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
        binding.firePreview.stop();
        startCameraSource();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_preview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, binding.fireFaceOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);

        }

        try {
            switch (model) {
                case CLASSIFICATION_QUANT:
                    Log.i(TAG, "Using Custom Image Classifier (quant) Processor");
                    cameraSource.setMachineLearningFrameProcessor(new CustomImageClassifierProcessor(this, true));
                    break;
                case CLASSIFICATION_FLOAT:
                    Log.i(TAG, "Using Custom Image Classifier (float) Processor");
                    cameraSource.setMachineLearningFrameProcessor(new CustomImageClassifierProcessor(this, false));
                    break;
                case TEXT_DETECTION:
                    Log.i(TAG, "Using Text Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor());
                    break;
                case FACE_DETECTION:
                    Log.i(TAG, "Using Face Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new FaceDetectionProcessor(getResources(),mEarringId,mNecklaceId,mHairStyleId));
                    break;
                case AUTOML_IMAGE_LABELING:
                    cameraSource.setMachineLearningFrameProcessor(new AutoMLImageLabelerProcessor(this, Mode.LIVE_PREVIEW));
                    break;
                case OBJECT_DETECTION:
                    Log.i(TAG, "Using Object Detector Processor");
                    FirebaseVisionObjectDetectorOptions objectDetectorOptions =
                            new FirebaseVisionObjectDetectorOptions.Builder()
                                    .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
                                    .enableClassification().build();
                    cameraSource.setMachineLearningFrameProcessor(
                            new ObjectDetectorProcessor(objectDetectorOptions));
                    break;
                case BARCODE_DETECTION:
                    Log.i(TAG, "Using Barcode Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor());
                    break;
                case IMAGE_LABEL_DETECTION:
                    Log.i(TAG, "Using Image Label Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new ImageLabelingProcessor());
                    break;
                case FACE_CONTOUR:
                    Log.i(TAG, "Using Face Contour Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new FaceContourDetectorProcessor());
                    break;
                default:
                    Log.e(TAG, "Unknown model: " + model);
            }
        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " + model, e);
            Toast.makeText(
                    getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (binding.firePreview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (binding.fireFaceOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                binding.firePreview.start(cameraSource, binding.fireFaceOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        binding.firePreview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }


    @Override
    public void onClickAtOKButton(int position) {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        Toast.makeText(getApplicationContext(),"Clicked...",Toast.LENGTH_SHORT).show();

//        System.out.println("Positions "+mEarringId+" mNecklaceId "+mNecklaceId+" mHairStyleId "+mHairStyleId);
        cameraSource.setMachineLearningFrameProcessor(new FaceDetectionProcessor(getResources(),mEarringId,mNecklaceId,mHairStyleId));
    }
}