package com.marco.beautypuzzle;

import android.graphics.Bitmap;

/**
 * User: KdMobiB
 * Date: 2016/7/4
 * Time: 11:01
 */
public class ImagePiece {
    private int index;
    private Bitmap bitmap;

    public ImagePiece() {
    }

    public ImagePiece(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
