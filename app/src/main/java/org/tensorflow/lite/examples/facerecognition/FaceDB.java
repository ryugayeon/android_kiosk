package org.tensorflow.lite.examples.facerecognition;

import android.util.Log;
import android.util.Pair;

import org.tensorflow.lite.examples.facerecognition.tflite.FaceEmbedding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FaceDB {
    private static final String TAG = "FaceDB";
    // In-memory database
    private Map<String, Pair<String, FaceEmbedding>> registeredFaces; // id -> (name, embedding), assuming unique names
    private boolean isRegistering = false;

    public FaceDB() {
        registeredFaces = new HashMap<>();
    }

    public boolean getIsRegistering() {
        return isRegistering;
    }

    public synchronized void setIsRegistering(boolean b) {
        isRegistering = b;
    }

    public void register(String name, FaceEmbedding embedding) {
        registeredFaces.put(UUID.randomUUID().toString(), new Pair<>(name, embedding));
    }

    public Pair<String, String> findFace(FaceEmbedding embedding) {
        Pair<String, String> p = null;
        Float minDistance = null;

        for (Map.Entry<String, Pair<String, FaceEmbedding>> entry : registeredFaces.entrySet()) {
            String id = entry.getKey();
            String name = entry.getValue().first;
            FaceEmbedding other = entry.getValue().second;

            // Calculate distance
            float distance = 0f;
            for (int i = 0; i < FaceEmbedding.EMBEDDING_SIZE; i++) {
                float d = embedding.get(i) - other.get(i);
                distance += d * d;
            }
            distance = (float) Math.sqrt(distance);

            if (minDistance == null || distance < minDistance) {
                minDistance = distance;
                if (minDistance < 1.0f) {
                    p = new Pair<>(id, name);
                }
            }
        }
//        Log.i(TAG, "Min Distance: " + minDistance);

        return p;
    }
}
