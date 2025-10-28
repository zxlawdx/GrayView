package com.example.grayview.process;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.util.Log;


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
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();
        Bitmap bmpBlurred = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int radius = 5;  // menor para teste
        int kernelSize = 2*radius + 1;

        int[] pixels = new int[width * height];
        bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] output = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sumR = 0, sumG = 0, sumB = 0;
                int count = 0;

                for (int ky = -radius; ky <= radius; ky++) {
                    int py = y + ky;
                    if (py < 0 || py >= height) continue;

                    for (int kx = -radius; kx <= radius; kx++) {
                        int px = x + kx;
                        if (px < 0 || px >= width) continue;

                        int pixel = pixels[py * width + px];
                        sumR += (pixel >> 16) & 0xff;
                        sumG += (pixel >> 8) & 0xff;
                        sumB += pixel & 0xff;
                        count++;
                    }
                }

                int r = sumR / count;
                int g = sumG / count;
                int b = sumB / count;
                output[y * width + x] = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
        }

        bmpBlurred.setPixels(output, 0, width, 0, 0, width, height);
        return bmpBlurred;
    }


    public Bitmap rotate(Bitmap bmpOriginal, double degrees) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();

        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        // Calcula dimensões do novo bitmap
        int newWidth = (int) (width * Math.abs(cos) + height * Math.abs(sin));
        int newHeight = (int) (width * Math.abs(sin) + height * Math.abs(cos));

        Bitmap bmpRotated = Bitmap.createBitmap(newWidth, newHeight, bmpOriginal.getConfig());

        int cx = width / 2;
        int cy = height / 2;
        int ncx = newWidth / 2;
        int ncy = newHeight / 2;

        for (int yPrime = 0; yPrime < newHeight; yPrime++) {
            for (int xPrime = 0; xPrime < newWidth; xPrime++) {

                // Coordenadas relativas ao centro da nova imagem
                double xRel = xPrime - ncx;
                double yRel = yPrime - ncy;

                // Mapeamento inverso: calcula (x, y) originais
                double x = xRel * cos + yRel * sin + cx;
                double y = -xRel * sin + yRel * cos + cy;

                // Verifica se (x, y) estão dentro da imagem original
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    int pixel = bmpOriginal.getPixel((int) x, (int) y);
                    bmpRotated.setPixel(xPrime, yPrime, pixel);
                }
            }
        }

        return bmpRotated;
    }




    public ColorMatrix contrast(float contrast) {
        float scale = contrast;
        float translate = (-0.5f * scale + 0.5f) * 255f * (1f - scale);

        ColorMatrix matrix = new ColorMatrix(new float[]{
                scale, 0, 0, 0, translate,
                0, scale, 0 , 0, translate,
                0, 0,scale,0, translate,
                0, 0, 0, 1, 0
        });

        return  matrix;
    }

    public ColorMatrix brightness(float brightness) {
        float translate = (brightness - 1f) * 255f;

        ColorMatrix matrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, translate,
                0, 1, 0, 0, translate,
                0, 0, 1, 0, translate,
                0, 0, 0, 1, 0
        });

        return matrix;
    }


    public Bitmap stretchPerChannel(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int rMin = 255, rMax = 0;
        int gMin = 255, gMax = 0;
        int bMin = 255, bMax = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = bitmap.getPixel(x, y);
                int R = Color.red(p);
                int G = Color.green(p);
                int B = Color.blue(p);
                if (R < rMin) rMin = R;
                if (R > rMax) rMax = R;
                if (G < gMin) gMin = G;
                if (G > gMax) gMax = G;
                if (B < bMin) bMin = B;
                if (B > bMax) bMax = B;
            }
        }

        // Função auxiliar inline para stretching com proteção
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = bitmap.getPixel(x, y);
                int R = Color.red(p);
                int G = Color.green(p);
                int B = Color.blue(p);

                int rNew = (rMax == rMin) ? R : (int)(((R - rMin) / (float)(rMax - rMin)) * 255.0f);
                int gNew = (gMax == gMin) ? G : (int)(((G - gMin) / (float)(gMax - gMin)) * 255.0f);
                int bNew = (bMax == bMin) ? B : (int)(((B - bMin) / (float)(bMax - bMin)) * 255.0f);

                rNew = Math.max(0, Math.min(255, rNew));
                gNew = Math.max(0, Math.min(255, gNew));
                bNew = Math.max(0, Math.min(255, bNew));

                out.setPixel(x, y, Color.rgb(rNew, gNew, bNew));
            }
        }

        Log.d("ImageProcessor", "StretchPerChannel: r[" + rMin + "," + rMax + "] g[" + gMin + "," + gMax + "] b[" + bMin + "," + bMax + "]");
        return out;
    }

    public Bitmap histogramStretch(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int rMin = 255, rMax = 0;
        int gMin = 255, gMax = 0;
        int bMin = 255, bMax = 0;

        // Encontra min/max por canal
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = bitmap.getPixel(x, y);
                int R = Color.red(p);
                int G = Color.green(p);
                int B = Color.blue(p);
                if (R < rMin) rMin = R;
                if (R > rMax) rMax = R;
                if (G < gMin) gMin = G;
                if (G > gMax) gMax = G;
                if (B < bMin) bMin = B;
                if (B > bMax) bMax = B;
            }
        }

        // Aplica stretching
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = bitmap.getPixel(x, y);
                int R = Color.red(p);
                int G = Color.green(p);
                int B = Color.blue(p);

                int rNew = (rMax == rMin) ? R : (R - rMin) * 255 / (rMax - rMin);
                int gNew = (gMax == gMin) ? G : (G - gMin) * 255 / (gMax - gMin);
                int bNew = (bMax == bMin) ? B : (B - bMin) * 255 / (bMax - bMin);

                out.setPixel(x, y, Color.rgb(rNew, gNew, bNew));
            }
        }

        return out;
    }

    public Bitmap edgeDetection(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();
        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);


        int[][] Gx = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] Gy = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                int sumRx = 0, sumGx = 0, sumBx = 0;
                int sumRy = 0, sumGy = 0, sumBy = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = bmpOriginal.getPixel(x + kx, y + ky);
                        int R = Color.red(pixel);
                        int G = Color.green(pixel);
                        int B = Color.blue(pixel);

                        sumRx += R * Gx[ky + 1][kx + 1];
                        sumGx += G * Gx[ky + 1][kx + 1];
                        sumBx += B * Gx[ky + 1][kx + 1];

                        sumRy += R * Gy[ky + 1][kx + 1];
                        sumGy += G * Gy[ky + 1][kx + 1];
                        sumBy += B * Gy[ky + 1][kx + 1];
                    }
                }

                int r = (int) Math.min(255, Math.sqrt(sumRx * sumRx + sumRy * sumRy));
                int g = (int) Math.min(255, Math.sqrt(sumGx * sumGx + sumGy * sumGy));
                int b = (int) Math.min(255, Math.sqrt(sumBx * sumBx + sumBy * sumBy));

                out.setPixel(x, y, Color.rgb(r, g, b));
                Log.e("ImageProcessor", "EdgeDetection: [" + r + "," + g + "," + b + "]");
            }
        }

        return out;
    }








}
