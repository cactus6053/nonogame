package com.example.pa2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class nonogame extends View {
    int width, height;
    int orgW, orgH;
    int picW, picH;
    Bitmap img;
    Bitmap imgPic[][] = new Bitmap[20][20];
    int checkImg[][] = new int[30][30];

    public nonogame(Context context, Bitmap bm){
        super(context);
        int pixel;
        int black = 0, white = 255;
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        width = display.getWidth()-80;
        height = width;

        img = Bitmap.createScaledBitmap(bm, width, height, true);

        orgW = img.getWidth()/20 * 20;
        orgH = img.getHeight()/20 * 20;

        picW = orgW/20;
        picH = orgH/20;

        Bitmap bitmapgray = Bitmap.createBitmap(orgW, orgH, Bitmap.Config.ARGB_4444);
        Bitmap finalbitmap = Bitmap.createBitmap(orgW, orgH, Bitmap.Config.ARGB_4444);

        //ARGB 4 colors
        int A, R, G, B;

        for(int i=0; i<orgW; ++i){
            for(int j=0; j<orgH; ++j){
                pixel = img.getPixel(i,j);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int)((R+G+B)/3);

                if (gray > 128) gray = 255;
                else gray = 0;

                bitmapgray.setPixel(i,j,Color.argb(A,gray,gray,gray));
            }
        }

        for(int i=0; i<20; ++i) {
            for (int j = 0; j < 20; ++j) {
                int black_cnt = 0;
                int white_cnt = 0;
                for (int x = picW * i; x < picW * (i + 1); ++x) {
                    for (int y = picH * j; y < picH * (j + 1); ++y) {
                        pixel = bitmapgray.getPixel(x, y);
                        int color = Color.red(pixel);
                        if (color == 0) {
                            black_cnt++;
                        } else {
                            white_cnt++;
                        }
                    }
                }
                if (black_cnt >= white_cnt) {
                    for (int x = picW * i; x < picW * (i + 1); ++x) {
                        for (int y = picH * j; y < picH * (j + 1); ++y) {
                            //finalbitmap.setPixel(x, y, Color.argb(111, black, black, black));
                            checkImg[j+10][i+10] = 1;
                        }
                    }
                } else {
                    for (int x = picW * i; x < picW * (i + 1); ++x) {
                        for (int y = picH * j; y < picH * (j + 1); ++y) {
                            //finalbitmap.setPixel(x, y, Color.argb(111, white, white, white));
                            checkImg[j+10][i+10] = 0;
                        }
                    }
                }
            }
        }
        /*for(int i=0; i<20; ++i) {
            for (int j = 0; j < 20; ++j) {
                imgPic[i][j] = Bitmap.createBitmap(finalbitmap, j*picW, i*picH, picW, picH );
            }
        }*/
    }
}
