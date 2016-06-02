package com.ahs.udacity.popularmovies.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahs.udacity.popularmovies.Movies;
import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.adapter.MovieDetailPagerAdapter;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.provider.DbManager;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesDetailFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MOVIE_DETAIL = "movie_detail";

    public static final String TAG = MoviesDetailFragment.class.getCanonicalName();
    private static final String TMDB_URL = "http://image.tmdb.org/t/p/w500/";
    private static final int OVERVIEW = 0;
    private static final int RATING = 1;
    private static final int GENRE = 2;
    private static final float SELECTED_SIZE = 22;
    private static final float DESELECTED_SIZE = 16;
    private static final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View convert_view;
    private MovieDetail movie_detail;
    private ViewPager viewPager;
    private FloatingActionButton favourite_fab;
    private FloatingActionButton share_fab;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieDetail Parameter 1.

     * @return A new instance of fragment MoviesDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesDetailFragment newInstance(MovieDetail movieDetail) {
        MoviesDetailFragment fragment = new MoviesDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_DETAIL, movieDetail);
        fragment.setArguments(args);
        return fragment;
    }

    public MoviesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie_detail = (MovieDetail)getArguments().getParcelable(MOVIE_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convert_view =  inflater.inflate(R.layout.fragment_movies_detail, container, false);
        viewPager = (ViewPager)convert_view.findViewById(R.id.detailedContentPager);
        if( viewPager !=null ){
            setupMovieDetails();
        }
        youTubePlayerSupportFragment = (YouTubePlayerSupportFragment)getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
        YouTubeListeners youTubeListeners = new YouTubeListeners();
        youTubePlayerSupportFragment.initialize(Movies.DEVELOPER_KEY, youTubeListeners);
        return convert_view;
    }

    private void setupMovieDetails() {

        MovieDetailPagerAdapter movieDetailPagerAdapter = new MovieDetailPagerAdapter(getActivity(),getChildFragmentManager(),movie_detail);
        viewPager.setAdapter(movieDetailPagerAdapter);
        setupPageChangeListener();
    }


    private void setupPageChangeListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int id = -1;
                switch (position) {
                    case 0:
                        id = R.id.overview;
                        break;
                    case 1:
                        id = R.id.rating;
                        break;
                    case 2:
                        id = R.id.genre;
                        break;
                }
                setTabTextSize(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView)convert_view.findViewById(R.id.movie_title)).setText(movie_detail.getMovieTitle());
        favourite_fab = (FloatingActionButton)convert_view.findViewById(R.id.fab_favourite);
        if(!movie_detail.isFavourite()) {
            favourite_fab.setLabelText(getString(R.string.mark_favourite));
        }
        else
        {
            favourite_fab.setLabelText(getString(R.string.unmark_as_favourite));
        }
        share_fab = (FloatingActionButton)convert_view.findViewById(R.id.fab_share);
        share_fab.setLabelText(getString(R.string.share_friends));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onresume");
        final ImageView imageView = (ImageView) convert_view.findViewById(R.id.moviePoster);
        Picasso.with(getActivity()).load(TMDB_URL + movie_detail.getThumbNailLink()).fit().into(imageView);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void mDetailTab(View v)
    {
        setTabTextSize(v.getId());
        switch (v.getId())
        {

            case R.id.overview:
                viewPager.setCurrentItem(OVERVIEW);
                break;
            case R.id.rating:
                viewPager.setCurrentItem(RATING);
                break;
            case R.id.genre:
                viewPager.setCurrentItem(GENRE);
                break;
        }
    }

    private void setTabTextSize(int id) {
        int arr[] = new int[]{R.id.overview,R.id.rating,R.id.genre};
        for(int i=0;i<arr.length;i++){
            if(arr[i]==id)
            {
                ((TextView)convert_view.findViewById(arr[i])).setTextSize(SELECTED_SIZE);
            }
            else
            {
                ((TextView)convert_view.findViewById(arr[i])).setTextSize(DESELECTED_SIZE);
            }
        }

    }

    public void playTrailer(View v)
    {
        //player initialized
        if(youTubePlayer!=null) {
            Log.d(TAG,"playTrailer key:"+movie_detail.getYoutubeKey());

            MyPlayBackListener myPlayBackListener = new MyPlayBackListener();
            youTubePlayer.setPlaybackEventListener(myPlayBackListener);
            if(movie_detail.getYoutubeKey()!=null) {
                youTubePlayer.loadVideo(movie_detail.getYoutubeKey());
            }
            else
            {
                //TODO display dialog saying trailer not available
            }
            convert_view.findViewById(R.id.moviePoster_container).setVisibility(View.GONE);
            //youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
        else //not initialized
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_LINK + movie_detail.getYoutubeKey())));
        }
    }

    public void markAsFavourite(View v,int current_id) {
        String toDisplay = "";
        if(!movie_detail.isFavourite()) {
            if(DbManager.markAsFavourite(getActivity(), movie_detail.getMovieId())) {
                movie_detail.setIsFavourite(true);
                favourite_fab.setLabelText(getString(R.string.unmark_as_favourite));
                toDisplay = getString(R.string.movie_added_favourite);
            }
        }
        else
        {
            if (DbManager.unmarkAsFavourite(getActivity(), movie_detail.getMovieId())) {
                movie_detail.setIsFavourite(false);
                favourite_fab.setLabelText(getString(R.string.mark_favourite));
                toDisplay = getString(R.string.movie_remove_favourite);

            }
        }

        Toast.makeText(getActivity(), movie_detail.getMovieTitle() + " " + toDisplay, Toast.LENGTH_LONG).show();
    }

    public void share(View viewById) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_string) + "\"" + movie_detail.getMovieTitle() + "\" -" + "\n"+YOUTUBE_LINK + movie_detail.getYoutubeKey());
        startActivity(Intent.createChooser(share, "Share with Friends"));
    }

    public class YouTubeListeners implements YouTubePlayer.OnInitializedListener{

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider,YouTubePlayer you_tube_player, boolean b) {
            youTubePlayer = you_tube_player;


            Log.d(TAG,"onInitializationSuccess youtube player:"+you_tube_player);

        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            Log.d(TAG, "onInitializationFailure youtube player :"+youTubeInitializationResult.toString());

        }
    }


    class MyPlayBackListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            Log.d(TAG," video playing");
            convert_view.findViewById(R.id.moviePoster_container).setVisibility(View.GONE);
            youTubePlayer.loadVideo(movie_detail.getYoutubeKey());
        }

        @Override
        public void onPaused() {
            Log.d(TAG," video paused");

        }

        @Override
        public void onStopped() {
            Log.d(TAG," video stopped");
            //youTubePlayer.loadVideo(movie_detail.getYoutubeKey());
            convert_view.findViewById(R.id.moviePoster_container).setVisibility(View.VISIBLE);

        }

        @Override
        public void onBuffering(boolean b) {
            convert_view.findViewById(R.id.moviePoster_container).setVisibility(View.GONE);
            Log.d(TAG,"onBuffering");
        }

        @Override
        public void onSeekTo(int i) {
            Log.d(TAG,"onSeekTo:"+i);
        }
    }

}
