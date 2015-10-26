package com.ahs.udacity.popularmovies.activity.fragment;

import android.app.Activity;
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

import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.adapter.MovieDetailPagerAdapter;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View convert_view;
    private MovieDetail movie_detail;
    private ViewPager viewPager;

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
        MovieDetailPagerAdapter movieDetailPagerAdapter = new MovieDetailPagerAdapter(getActivity(),getChildFragmentManager(),movie_detail);
        viewPager.setAdapter(movieDetailPagerAdapter);

        return convert_view;
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

}
