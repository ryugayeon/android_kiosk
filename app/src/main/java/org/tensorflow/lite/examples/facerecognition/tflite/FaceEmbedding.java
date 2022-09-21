package org.tensorflow.lite.examples.facerecognition.tflite;

public class FaceEmbedding {
    public static final int EMBEDDING_SIZE = 192;
    private float[] embedding;

    public float[] getEmbedding() {
        return embedding;
    }

    public float get(int i) {
        return embedding[i];
    }

    public FaceEmbedding(float[] embedding) {
        this.embedding = new float[EMBEDDING_SIZE];
        System.arraycopy(embedding, 0, this.embedding, 0, EMBEDDING_SIZE);
    }
}
