package com.example.pa2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context c;
    int[] arrayList;
    //Bitmap puzzles[];

    MyAdapter(Context c, int[] list)
    {
        this.c = c;
        arrayList = list;
    }

    @Override
    public int getCount()
    {
        return arrayList.length;
    }

    @Override
    public Object getItem(int i)
    {
        return arrayList[i];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.boardpiece, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.numbering);
        if(arrayList[i] == 0 || arrayList[i] == 99) {
            textView.setText("");
        }
        else textView.setText(Integer.toString(arrayList[i]));

        if(i<300) ;
        else if(i%30 <10) ;
        else if(arrayList[i] == 99) textView.setBackgroundColor(Color.parseColor("#000000"));
        else textView.setBackgroundColor(Color.parseColor("#fafafa"));

        //ImageView imageView = view.findViewById(R.id.imageView);
        //imageView.setImageBitmap(puzzles[i]);
        return view;
    }
    public void drawing(int[] arr) { this.arrayList = arr;}
}

