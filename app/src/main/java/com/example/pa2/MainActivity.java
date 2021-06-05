package com.example.pa2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button search_btn, gallery_btn;
    naverapi searching;
    GridView gridView;
    //Bitmap bitmap[] = new Bitmap[400];
    int tag[][] = new int[30][30];
    Handler handler = new Handler();
    int gameboard[][] = new int[30][30];
    int game[] = new int[900];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.grid);
        search_btn = (Button)findViewById(R.id.search_btn);
        MyAdapter myadapter = new MyAdapter(this, game);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    public void run(){
                        //search using naverapi
                        editText = (EditText)findViewById(R.id.searchtext);
                        String keyword = editText.getText().toString();
                        String temp = searching.search(keyword);

                        //get with gson format
                        Gson gson = new GsonBuilder().create();
                        image imges = gson.fromJson(temp, image.class);
                        List<Map> list = imges.getItems();
                        Map map = list.get(0);
                        String pictureUrl = map.get("thumbnail").toString();
                        try{
                            URL url = new URL(pictureUrl);
                            InputStream is = url.openStream();
                            Bitmap bm = BitmapFactory.decodeStream(is);
                            Context context = getApplicationContext();
                            nonogame board = new nonogame(context, bm);
                            tag = board.checkImg;
                            game = makegameboard(board);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    gridView.setNumColumns(30);
                                    gridView.setAdapter(myadapter);
                                }
                            });

                        } catch (Exception e){
                            e.printStackTrace();
                        };


                    }
                }.start();
            }
        });

        gallery_btn = (Button)findViewById(R.id.gallery_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        piecelistener listener = new piecelistener();
        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) listener);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = getApplicationContext();
        super.onActivityResult(requestCode, resultCode, data);
        MyAdapter myadapter = new MyAdapter(this, game);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    nonogame board = new nonogame(context, img);
                    tag = board.checkImg;
                    game = makegameboard(board);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            gridView.setNumColumns(30);
                            gridView.setAdapter(myadapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int[] makegameboard(nonogame board){
        //init
        for(int i=0; i<900; i++) game[i] = 0;
        for(int i=0; i<30; i++)
            for(int j=0; j<30; j++)
                gameboard[i][j] = 0;

        int cnt1 = 0, cnt2 = 0;
        //가로방향 숫자세기

        for(int y=10; y<30; ++y){
            int start_x_pos = 9;
            cnt1 = 0;
            for(int x=29; x>=10; --x){
                if(board.checkImg[y][x]==1){
                    cnt1++;
                }
                else if(cnt1 != 0){
                    gameboard[y][start_x_pos--] = cnt1;
                    cnt1 = 0;
                }

                if(x==10 && cnt1 != 0){
                    gameboard[y][start_x_pos] = cnt1;
                }
            }

        }

        for(int x=10; x<30; ++x){
            int start_y_pos = 9;
            cnt2 = 0;
            for(int y=29; y>=10; --y){
                if(board.checkImg[y][x]==1){
                    cnt2++;
                }
                else if(cnt2 !=0){
                    gameboard[start_y_pos--][x] = cnt2;
                    cnt2 =0;
                }
                if(y==10 && cnt2 != 0){
                    gameboard[start_y_pos--][x] = cnt2;
                }
            }
        }
        for(int i=0;i<30; ++i){
            for(int j=0;j<30; ++j){
                int index = i*30 + j;
                game[index] = gameboard[i][j];
                //game[index] = board.checkImg[i][j];
            }
        }
        return game;
    }

    private class piecelistener implements AdapterView.OnItemClickListener{
        GridView gridView = (GridView)findViewById(R.id.grid);
        Context context = getApplicationContext();
        MyAdapter myAdapter = new MyAdapter(context, game);

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int flag = 0;
            int index = position;
            int y_pos=0, x_pos=0;
            //x 좌표 , y좌표 구하기
            for(int y=0; y<30; ++y){
                for(int x=0; x<30; ++x){
                    if(index == (y*30 + x)){
                        y_pos=y;
                        x_pos=x;
                    }
                }
            }

            if(tag[y_pos][x_pos] == 1){
                game[index] = 99; //99는 색칠을 뜻
                myAdapter.drawing(game);
                gridView.setAdapter(myAdapter);
                //정답 check
                for(int y=10; y<30; y++){
                    for(int x=10; x<30; x++){
                        if(tag[y][x] == 1 && game[y*30 + x]==99) flag++;
                        else if(tag[y][x] == 0 && game[y*30 + x]==0) flag++;
                        else{
                            break;
                        }
                    }
                }
                if(flag == 400){
                    String msg = "FINISH!";
                    Toast msg_show = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    msg_show.show();
                }
            }
            else{
                String msg = "Wrong!";
                Toast msg_show = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                msg_show.show();
                for(int y=10; y<30; y++){
                    for(int x=10; x<30; x++){
                        game[y*30 + x] = 0;
                        myAdapter.drawing(game);
                        gridView.setAdapter(myAdapter);
                    }
                }
            }

        }
    }


}