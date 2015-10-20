package com.ahs.udacity.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.activity.MovieDetailActivity;
import com.ahs.udacity.popularmovies.activity.PopularMoviesGrid;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.provider.DbManager;
import com.squareup.picasso.Picasso;

/**
 * Created by shetty on 03/10/15.
 */
public class PopularMoviesAdapter extends CursorRecyclerViewAdapter<PopularMoviesAdapter.ViewHolder>{

    private static final int MOVIES_COUNT = 15;
    private static final String TAG = PopularMoviesAdapter.class.getCanonicalName();
    public static final String MOVIE_DETAIL = "MOVIE_DETAIL";
    private static final String TMDB_IMAGE_LINK = "http://i.imgur.com/";
    private Context mContext;
    private boolean isTwoPane;

    public PopularMoviesAdapter(Cursor cursor, Activity context,boolean is_two_pane)
    {
        super(context,cursor);
        mContext = context;
        isTwoPane = is_two_pane;
    }

    @Override
    public PopularMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder(
        inflater.inflate(R.layout.popularmovies_grid_view,viewGroup,false));
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return MOVIES_COUNT;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        MovieDetail mDetail  = DbManager.getElementFromCursor(cursor);
        viewHolder.bind(mDetail);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private MovieDetail movieDetail;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.movieThumbnail);
            imageView.setOnClickListener(this);
        }


        public void bind(final MovieDetail movieDetail){
            this.movieDetail = movieDetail;
            Log.d(TAG,"MovieDetail:"+movieDetail);
            ViewTreeObserver vto = imageView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = imageView.getMeasuredHeight();
                    int width = imageView.getMeasuredWidth();
                    Log.d(TAG,"width:"+width+" height:"+height);
                    Picasso.with(imageView.getContext()).load(TMDB_IMAGE_LINK+movieDetail.getThumbNailLink()).resize(width, height).into(imageView);
                }
            });

        }

        @Override
        public void onClick(View v) {
            if(isTwoPane)
            {
                ((PopularMoviesGrid)mContext).addDetailFragment(movieDetail);

            }
            else
            {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.getExtras().putParcelable(MOVIE_DETAIL,movieDetail);
                mContext.startActivity(intent);
            }
        }
    }

}
