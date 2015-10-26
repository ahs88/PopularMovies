package com.ahs.udacity.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

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

    private Context mContext;
    private boolean isTwoPane;

    public PopularMoviesAdapter(Cursor cursor, Activity context,boolean is_two_pane)
    {
        super(context, cursor);
        mContext = context;
        isTwoPane = is_two_pane;
    }

    @Override
    public PopularMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder");
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popularmovies_grid_view,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(
        view);
        return viewHolder;
    }


    /*@Override
    public int getItemCount() {
        return MOVIES_COUNT;
    }*/

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        //Log.d(TAG,"onBindViewHolder");
        MovieDetail mDetail  = DbManager.getElementFromCursor(cursor);
        viewHolder.bind(mDetail);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageThumbNail;
        private MovieDetail movieDetail;
        //private TextView movieTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            imageThumbNail = (ImageView)itemView.findViewById(R.id.movieThumbnail);
            CardView cardView  = (CardView)itemView.findViewById(R.id.movieCardView);
            cardView.setPreventCornerOverlap(false);
            imageThumbNail.setOnClickListener(this);
        }


        public void bind(final MovieDetail movieDetail){
            this.movieDetail = movieDetail;
            Log.d(TAG,"MovieDetail:"+movieDetail);
            ViewTreeObserver vto = imageThumbNail.getViewTreeObserver();
            /*vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = imageThumbNail.getMeasuredHeight();
                    int width = imageThumbNail.getMeasuredWidth();
                    Log.d(TAG,"width:"+width+" height:"+height);*/
                    Picasso.with(imageThumbNail.getContext()).load(movieDetail.getPosterLink()).fit().into(imageThumbNail);
            //    }
            //});
            //movieTitle.setText(movieDetail.getMovieTitle());

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
                Log.d(TAG,"movieDetail:"+movieDetail+" intent extra:"+intent.getExtras());
                intent.putExtra(MOVIE_DETAIL,movieDetail);
                mContext.startActivity(intent);
            }
        }
    }

}
