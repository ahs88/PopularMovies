package com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.adapter.GenreImagesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Genre.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Genre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Genre extends android.support.v4.app.Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GENREIDS = "GENREIDS";
    private static final String TAG = Genre.class.getCanonicalName();


    // TODO: Rename and change types of parameters
    private List<String> genreIds;


    private OnFragmentInteractionListener mListener;
    private View convert_view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param genre_ids Parameter 1.
     *
     * @return A new instance of fragment Genre.
     */
    // TODO: Rename and change types and number of parameters
    public static Genre newInstance(ArrayList<String> genre_ids) {
        Genre fragment = new Genre();
        Bundle args = new Bundle();
        args.putStringArrayList(GENREIDS, genre_ids);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Genre() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genreIds = getArguments().getStringArrayList(GENREIDS);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView genreIds size:"+genreIds.size());
        convert_view =  inflater.inflate(R.layout.fragment_genre, container, false);
        RecyclerView recyclerView = (RecyclerView)convert_view.findViewById(R.id.grid_genre_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new GenreImagesAdapter(genreIds,getActivity()));
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
