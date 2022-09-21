package org.tensorflow.lite.examples.facerecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.examples.facerecognition.tflite.FaceEmbedding;

import java.util.ArrayList;
import java.util.List;

public class FaceRecognitionPipeline {
    private static final String TAG = "FaceRecognitionPipeline";
    private FaceDetectorHelper faceDetectorHelper;
    private FaceEncoderHelper faceEncoderHelper;
    private FaceDB faceDB;
    private final Context context;
    private final Listener listener;

    public FaceRecognitionPipeline(Context context,
                                   Listener listener) {
        this.context = context;
        this.listener = listener;
        this.faceDB = new FaceDB();
        setupFaceRecognizer();
    }

    public static FaceRecognitionPipeline create(Context context,
                                                 Listener listener) {
        return new FaceRecognitionPipeline(context, listener);
    }

    private void setupFaceRecognizer() {
        faceDetectorHelper = FaceDetectorHelper.create(context);
        faceEncoderHelper = FaceEncoderHelper.create(context);
    }

    public void clear() {
        faceDetectorHelper.clearFaceDetector();
        faceEncoderHelper.clearFaceEncoder();
    }

    public void process(Bitmap image, int imageRotation) {
        // Detection
        long startTime = SystemClock.uptimeMillis();
        List<Face> faces = faceDetectorHelper.detect(image, imageRotation);
        Log.i(TAG, faces.toString());

        // Recognition for each detected face
        List<RecognizedFace> recognizedFaces = new ArrayList<>();
        List<Pair<Bitmap, FaceEmbedding>> unknownFaces = new ArrayList<>();
        for (Face face : faces) {
            // Crop the image
            // Notice: boundingBox is in screen orientation
            Rect boundingBox = face.getBoundingBox();
            //            Log.i(TAG, "BoundingBox: " + boundingBox);
            //            Log.i(TAG, "BoundingBox: w=" + boundingBox.width() + ", h=" + boundingBox.height());
            Log.i(TAG, "Original: w=" + image.getWidth() + ", h=" + image.getHeight());

            // Too small, discard
//            if (boundingBox.width() < FaceEncoderHelper.INPUT_IMAGE_SIZE || boundingBox.height() < FaceEncoderHelper.INPUT_IMAGE_SIZE) {
//                continue;
//            }

            // Rotation image (always w=640, h=480) to sensor frame (depends on screen orientation), boundingBox is in this frame
            Matrix m = new Matrix();
            m.postRotate(imageRotation);
            Bitmap rotatedBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, false);
            //            Log.i(TAG, "Rotated: w=" + rotatedBitmap.getWidth() + ", h=" + rotatedBitmap.getHeight());

            // Make sure bounding box is within the borders
            int l = Math.max(0, boundingBox.left);
            int t = Math.max(0, boundingBox.top);
            int r = Math.min(rotatedBitmap.getWidth(), boundingBox.right);
            int b = Math.min(rotatedBitmap.getHeight(), boundingBox.bottom);
            Bitmap faceBitmap = Bitmap.createBitmap(rotatedBitmap, l, t, r - l, b - t);
            //            Log.i(TAG, "Cropped: w=" + faceBitmap.getWidth() + ", h=" + faceBitmap.getHeight());

            // Scale the image to (112, 112)
            Bitmap scaledFaceBitmap = Bitmap.createScaledBitmap(faceBitmap,
                    FaceEncoderHelper.INPUT_IMAGE_SIZE,
                    FaceEncoderHelper.INPUT_IMAGE_SIZE,
                    false);
            Log.i(TAG, "Scaled: w=" + scaledFaceBitmap.getWidth() + ", h=" + scaledFaceBitmap.getHeight());

            // TODO: align face

            // Recognition
            FaceEmbedding embedding = faceEncoderHelper.encode(scaledFaceBitmap);

            // Find closest embedding in the database
            Pair<String, String> p = faceDB.findFace(embedding);

            if (p != null) {
                recognizedFaces.add(new RecognizedFace(p.first, p.second, face));
            } else {
                unknownFaces.add(new Pair<>(scaledFaceBitmap, embedding));
            }
        }

        long inferenceTime = SystemClock.uptimeMillis() - startTime;
        //        Log.i(TAG, "unknown faces: " + unknownFaces.size());
        if (unknownFaces.size() == 0) {
            listener.onFaceRecognitionPipelineResults(recognizedFaces, inferenceTime, imageRotation);
        } else {
            if (!faceDB.getIsRegistering()) {
                faceDB.setIsRegistering(true);
                listener.onFaceRecognitionPipelineFoundUnknownFaces(unknownFaces, faceDB);
            }
        }
    }

    public interface Listener {
        void onFaceRecognitionPipelineError(String error);

        void onFaceRecognitionPipelineResults(List<RecognizedFace> results, long inferenceTime, int rotationDegree);

        void onFaceRecognitionPipelineFoundUnknownFaces(List<Pair<Bitmap, FaceEmbedding>> unknownFaces, FaceDB db);
    }
}
