
package org.tensorflow.lite.examples.facerecognition.tflite;

import android.graphics.Bitmap;

public interface FaceEncoder {
    FaceEmbedding encode(Bitmap bitmap);

    void enableStatLogging(final boolean debug);

    String getStatString();

    void close();

    void setNumThreads(int num_threads);

    void setUseNNAPI(boolean isChecked);
}
