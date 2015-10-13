package com.ahs.udacity.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ahs.udacity.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by shetty on 03/10/15.
 */
public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder>{

    private static final int MOVIES_COUNT = 15;
    private static final String TAG = PopularMoviesAdapter.class.getCanonicalName();
    private Context mContext;

    public PopularMoviesAdapter(Activity context)
    {
        super();
        mContext = context;

    }

    @Override
    public PopularMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder(
        inflater.inflate(R.layout.popularmovies_grid_view,viewGroup,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PopularMoviesAdapter.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder");
        viewHolder.bind();

    }

    @Override
    public int getItemCount() {
        return MOVIES_COUNT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.movieThumbnail);
        }
        public void bind(){
            ViewTreeObserver vto = imageView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = imageView.getMeasuredHeight();
                    int width = imageView.getMeasuredWidth();
                    Log.d(TAG,"width:"+width+" height:"+height);
                    Picasso.with(imageView.getContext()).load("http://i.imgur.com/DvpvklR.png").resize(width, height).into(imageView);
                }
            });

        }
    }

}
