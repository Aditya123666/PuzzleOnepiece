package com.marco.beautypuzzle;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * User: KdMobiB
 * Date: 2016/7/4
 * Time: 11:02
 */
public class ImageSpliterUtil {

    /**
     * 传入bitmap切成piece*piece块
     *
     * @param bitmap
     * @param piece
     * @return
     */
    public static List<ImagePiece> spiteImage(Bitmap bitmap, int piece) {
        List<ImagePiece> items = new ArrayList<>();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width, height) / piece;
        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(i * piece + j);

                int x = j * pieceWidth;
                int y = i * pieceWidth;

                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceWidth));

                items.add(imagePiece);
            }
        }
        return items;
    }
}
