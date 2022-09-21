package org.tensorflow.lite.examples.facerecognition;

import com.google.mlkit.vision.face.Face;

public class RecognizedFace {
    public String id;
    public String name;
    public Face face;

    public RecognizedFace(String id, String name, Face face) {
        this.id = id;
        this.name = name;
        this.face = face;
    }
}
