package com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahs.udacity.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Ratings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Ratings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ratings extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RATING = "RATING";
    private static final String VOTE_COUNT = "VOTE_COUNT";
    private static final String RELEASE_DATE = "RELEASE_DATE";


    private OnFragmentInteractionListener mListener;
    private View convert_view;
    private TextView rating_view;
    private String rating;
    private String vote_count;
    private TextView vote_count_view;
    private String release_date;
    private TextView release_date_view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ratings.
     */
    // TODO: Rename and change types and number of parameters
    public static Ratings newInstance(String param1, String param2,String release_date) {
        Ratings fragment = new Ratings();
        Bundle args = new Bundle();
        args.putString(RATING, param1);
        args.putString(VOTE_COUNT, param2);
        args.putString(RELEASE_DATE, release_date);

        fragment.setArguments(args);
        return fragment;
    }

    public Ratings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rating = getArguments().getString(RATING);
            vote_count = getArguments().getString(VOTE_COUNT);
            release_date = getArguments().getString(RELEASE_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convert_view =  inflater.inflate(R.layout.fragment_ratings, container, false);

        rating_view = (TextView)convert_view.findViewById(R.id.rating);
        vote_count_view = (TextView)convert_view.findViewById(R.id.vote_count);
        release_date_view = (TextView)convert_view.findViewById(R.id.release_date);
        rating_view.setText(getString(R.string.out_of_10_rating,rating));

        release_date_view.setText(getString(R.string.released_on_date,release_date));
        vote_count_view.setText(vote_count);
        return convert_view;
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
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

}
