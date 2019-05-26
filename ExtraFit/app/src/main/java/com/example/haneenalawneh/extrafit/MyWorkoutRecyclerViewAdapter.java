package com.example.haneenalawneh.extrafit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haneenalawneh.extrafit.Data.Exercise;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWorkoutRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkoutRecyclerViewAdapter.ViewHolder> {
    private final WorkoutFragment.OnListFragmentInteractionListener mListener;

    private final ArrayList<Exercise> mExercises;
    Context mContext;

    public MyWorkoutRecyclerViewAdapter(ArrayList<Exercise> exercises, Context context, WorkoutFragment.OnListFragmentInteractionListener listener) {
        mExercises = exercises;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.mNameView.setText(mExercises.get(position).getName());
        holder.mDurationView.setText(mExercises.get(position).getDurationDescription());
        Picasso.with(mContext).load(mExercises.get(position).getImageURL()).into(holder.mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDurationView;
        // public final View mDivider;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mImageView = view.findViewById(R.id.exerciseImage);
            //mDivider=view.findViewById(R.id.divider);
            mNameView = view.findViewById(R.id.name);
            mDurationView = view.findViewById(R.id.duration);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
