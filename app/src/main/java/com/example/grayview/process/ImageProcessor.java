package com.example.grayview.process;

import android.graphics.Bitmap;

public class ImageProcessor {

    public Bitmap toGray(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();
        Bitmap bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bmpOriginal.getPixel(x, y);

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                int gray = (int)(0.299*r + 0.587*g + 0.114*b);

                int newPixel = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
                bmpGray.setPixel(x, y, newPixel);
            }
        }
        return bmpGray;
    }

    public Bitmap invertColors(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();
        Bitmap bmpInverted = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bmpOriginal.getPixel(x, y);

                int r = 255 - ((pixel >> 16) & 0xff);
                int g = 255 - ((pixel >> 8) & 0xff);
                int b = 255 - (pixel & 0xff);

                int newPixel = 0xFF000000 | (r << 16) | (g << 8) | b;
                bmpInverted.setPixel(x, y, newPixel);
            }
        }
        return bmpInverted;
    }

    public Bitmap simpleBlur(Bitmap bmpOriginal) {
        Bitmap bmpBlurred = bmpOriginal.copy(bmpOriginal.getConfig(), true);
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();

        int radius = 1; // tamanho do kernel 3x3
        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                int sumR = 0, sumG = 0, sumB = 0;
                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int pixel = bmpOriginal.getPixel(x + kx, y + ky);
                        sumR += (pixel >> 16) & 0xff;
                        sumG += (pixel >> 8) & 0xff;
                        sumB += pixel & 0xff;
                    }
                }
                int numPixels = (2*radius+1)*(2*radius+1);
                int r = sumR / numPixels;
                int g = sumG / numPixels;
                int b = sumB / numPixels;

                int newPixel = 0xFF000000 | (r << 16) | (g << 8) | b;
                bmpBlurred.setPixel(x, y, newPixel);
            }
        }
        return bmpBlurred;
    }



}
