package com.example.haneenalawneh.extrafit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.haneenalawneh.extrafit.Data.Exercise;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {
    int index;
    ArrayList<Exercise> exercises;
    String imageUrl;
    int duration;
    int exercisesNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("exercises")) {
            exercises = new ArrayList<>();
            exercises = intent.getParcelableArrayListExtra("exercises");
            exercisesNum = exercises.size();
            index = intent.getIntExtra("index", 0);

            imageUrl = exercises.get(index).getImageURL();
            duration = exercises.get(index).getDuration() * 1000;

        }
        if (savedInstanceState == null) {
            ExerciseFragment exerciseFragment = new ExerciseFragment();
            // workoutFragment.setData(exercises);
            setFragmentData(exerciseFragment);
            FragmentManager fragmentManager = getSupportFragmentManager();

            //workoutFragment.setData(exercises);
            fragmentManager.beginTransaction()
                    .add(R.id.exerciseContainer, exerciseFragment)
                    .commitAllowingStateLoss();


        }
    }

    public void setFragmentData(ExerciseFragment fragment) {
        fragment.setImageUrl(imageUrl);
        fragment.setIndex(index);
        fragment.setDuration(duration);
        fragment.setMaxSize(exercises.size());
        fragment.maxSize = exercisesNum - 1;
    }


    public void moveToPrevious(View view) {
        index--;
        imageUrl = exercises.get(index).getImageURL();
        duration = exercises.get(index).getDuration() * 1000;

        ExerciseFragment exerciseFragment = new ExerciseFragment();
        setFragmentData(exerciseFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.exerciseContainer, exerciseFragment)
                .commitAllowingStateLoss();


    }

    public void moveToNext(View view) {
        index++;

        if (index < exercisesNum) {

            imageUrl = exercises.get(index).getImageURL();
            duration = exercises.get(index).getDuration() * 1000;

            ExerciseFragment exerciseFragment = new ExerciseFragment();
            setFragmentData(exerciseFragment);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.exerciseContainer, exerciseFragment)
                    .commitAllowingStateLoss();


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, WorkoutsActivity.class);
                intent.putParcelableArrayListExtra("workouts", exercises);
                startActivity(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}