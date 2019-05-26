package com.example.haneenalawneh.extrafit;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haneenalawneh.extrafit.Data.Exercise;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class WorkoutFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;

    private static final String ARG_COLUMN_COUNT = "column-count";
    LinearLayoutManager manager;
    Parcelable state;
    // boolean isTablet= getResources().getBoolean(R.bool.isTablet);
    String WORKOUTS_KEY = "workouts";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    ArrayList<Exercise> exerciseArrayList;

    public WorkoutFragment() {
    }

    public void setData(ArrayList<Exercise> exercises) {
        exerciseArrayList = new ArrayList<>();
        exerciseArrayList = exercises;

    }

    @SuppressWarnings("unused")
    public static WorkoutFragment newInstance(int columnCount) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises_list, container, false);
        manager = new LinearLayoutManager(getContext());

        if (savedInstanceState != null) {
            exerciseArrayList = savedInstanceState.getParcelableArrayList(WORKOUTS_KEY);

            manager.onRestoreInstanceState(savedInstanceState);

        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            //recyclerView.addItemDecoration(new DividerItemDecoration(context,0));

            if (!getResources().getBoolean(R.bool.isTablet)) {
                //manager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(manager);

            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

            }

            recyclerView.setAdapter(new MyWorkoutRecyclerViewAdapter(exerciseArrayList, this.getActivity(), mListener));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(int index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(WORKOUTS_KEY, exerciseArrayList);
        state = manager.onSaveInstanceState();

    }

    @Override
    public void onPause() {
        super.onPause();
        state = manager.onSaveInstanceState();

    }

    @Override
    public void onResume() {
        super.onResume();

        manager.onRestoreInstanceState(state);
    }
}
