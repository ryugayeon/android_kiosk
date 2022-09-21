package org.tensorflow.lite.examples.facerecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.examples.facerecognition.tflite.FaceEmbedding;
import org.tensorflow.lite.examples.facerecognition.tflite.FaceEncoder;
import org.tensorflow.lite.examples.facerecognition.tflite.FaceEncoderAPIModel;

import java.io.IOException;

public class FaceEncoderHelper {
    public static final int INPUT_IMAGE_SIZE = 112;
    private static final String TAG = "FaceEncoderHelper";
    private final Context context;
    private FaceEncoder faceEncoder;
    private long startTime;

    public FaceEncoderHelper(Context context) {
        this.context = context;
        setupFaceEncoder();
    }

    public static FaceEncoderHelper create(Context context) {
        return new FaceEncoderHelper(context);
    }

    private void setupFaceEncoder() {
        try {
            faceEncoder = FaceEncoderAPIModel.create(
                    context.getAssets(),
                    "mobilefacenet.tflite",
                    INPUT_IMAGE_SIZE,
                    false);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    public FaceEmbedding encode(Bitmap image) {
        if (faceEncoder == null) {
            setupFaceEncoder();
        }

        //        Log.i(TAG, "Image Rotation = " + imageRotation);
        //        Log.i(TAG, "Bitmap image: height=" + image.getHeight() + ", width=" + image.getWidth());

        startTime = SystemClock.uptimeMillis();
        FaceEmbedding embedding = faceEncoder.encode(image);
        Log.i(TAG, "Encode time: " + (SystemClock.uptimeMillis() - startTime) + "ms");

        return embedding;
    }

    public void clearFaceEncoder() {
        faceEncoder = null;
    }
}
