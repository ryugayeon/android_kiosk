package org.tensorflow.lite.examples.facerecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.ArrayList;
import java.util.List;

public class FaceDetectorHelper {
    private static final String TAG = "FaceDetectorHelper";
    private final Context context;
    private FaceDetector faceDetector;
    private long startTime;

    public FaceDetectorHelper(Context context) {
        this.context = context;
        setupFaceDetector();
    }

    public static FaceDetectorHelper create(Context context) {
        return new FaceDetectorHelper(context);
    }

    private void setupFaceDetector() {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .build();

        faceDetector = FaceDetection.getClient(options);
    }

    public List<Face> detect(Bitmap image, int imageRotation) {
        if (faceDetector == null) {
            setupFaceDetector();
        }

        //        Log.i(TAG, "Image Rotation = " + imageRotation);
        //        Log.i(TAG, "Bitmap image: height=" + image.getHeight() + ", width=" + image.getWidth());

        startTime = SystemClock.uptimeMillis();

//        InputImage inputImage = InputImage.fromBitmap(image, imageRotation);
        //        Log.i(TAG, "Input image: height=" + inputImage.getHeight() + ", width=" + inputImage.getWidth());

        Task<List<Face>> task = faceDetector.process(image, imageRotation)
                .addOnSuccessListener(
                        faces -> {
                            Log.i(TAG, "Detect time: " + (SystemClock.uptimeMillis() - startTime) + "ms");
                        }
                )
                .addOnFailureListener(
                        e -> {
                            Log.e(TAG, e.toString());
                        }
                );

        try {
            return Tasks.await(task);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return new ArrayList<>();
        }
    }

    public void clearFaceDetector() {
        faceDetector = null;
    }
}
