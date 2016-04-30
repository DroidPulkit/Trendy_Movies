package com.pulkitkumar.trendymovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class gridAdapter extends BaseAdapter {

    Context mContext;
    List<String> list = new ArrayList<String>();

    public gridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ImageView imageView;

        if(convertView == null){

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 750));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        }else {

            imageView = (ImageView) convertView;
        }
        //Loading image from below url into imageView
        Picasso.with(mContext)
                .load(MainActivity.IMG_BASE_URL + MainActivity.SMALL_IMG_SIZE + list.get(i))
                .into(imageView);

        return imageView;
    }

    public void add(String s) {
        list.add(s);
    }

    public void clean() {
        list.clear();
    }
}