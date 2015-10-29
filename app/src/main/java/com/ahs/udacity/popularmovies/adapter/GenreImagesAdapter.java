package com.ahs.udacity.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ahs.udacity.popularmovies.Movies;
import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.activity.MovieDetailActivity;
import com.ahs.udacity.popularmovies.activity.PopularMoviesGrid;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.provider.DbManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shetty on 03/10/15.
 */
public class GenreImagesAdapter extends RecyclerView.Adapter<GenreImagesAdapter.ViewHolder>{

    private static final int MOVIES_COUNT = 15;
    private static final String TAG = GenreImagesAdapter.class.getCanonicalName();
    public static final String MOVIE_DETAIL = "MOVIE_DETAIL";

    private Context mContext;
    private boolean isTwoPane;
    private List<String> genreIds;

    public GenreImagesAdapter(List<String> genre_id,Context context)
    {
        super();
        genreIds = genre_id;
        mContext = context;
    }

    @Override
    public GenreImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder genreIds size:"+genreIds.size());
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.genre_image_layout,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(
        view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(genreIds.get(position));
    }


    @Override
    public int getItemCount() {
        return genreIds.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageThumbNail;


        public ViewHolder(View itemView) {
            super(itemView);
            imageThumbNail = (ImageView)itemView.findViewById(R.id.genre_image);
        }


        public void bind(String id){
            String imageDrawableName = Movies.getInstance().getImageName(Integer.parseInt(id));
            Log.d(TAG,"id:"+id+"drawable name:"+imageDrawableName);
            Resources res = mContext.getResources();
            if(imageDrawableName !=null) {
                int resID = res.getIdentifier(imageDrawableName, "drawable", mContext.getPackageName());
                Drawable drawable = res.getDrawable(resID);
                imageThumbNail.setImageDrawable(drawable);
            }
        }


    }

}
