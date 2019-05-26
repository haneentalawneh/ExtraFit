package com.example.haneenalawneh.extrafit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.haneenalawneh.extrafit.Data.Api;
import com.example.haneenalawneh.extrafit.Data.Exercise;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkoutsActivity extends AppCompatActivity implements WorkoutFragment.OnListFragmentInteractionListener {
    int index = 0;
    ArrayList<Exercise> exerciseArrayList;
    Exercise[] exercises;

    @BindView(R.id.pb)
    ProgressBar progressBar;

    @BindView(R.id.main)
    CoordinatorLayout coordinatorLayout;
    Bundle savedIS;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        savedIS = savedInstanceState;

        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(84, 180, 220), PorterDuff.Mode.MULTIPLY); // rgb values of the primaryColor
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.workoutskey))) {
            exerciseArrayList = intent.getParcelableArrayListExtra("workouts");
            WorkoutFragment workoutFragment = new WorkoutFragment();
            workoutFragment.setData(exerciseArrayList);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.workoutContainer, workoutFragment)
                    .commitAllowingStateLoss();
            progressBar.setVisibility(View.INVISIBLE);

        } else if (savedInstanceState == null) {
            new RetroFitAsyncTask().execute();
        } else {
            exerciseArrayList = savedInstanceState.getParcelableArrayList("workouts");
            WorkoutFragment workoutFragment = new WorkoutFragment();
            workoutFragment.setData(exerciseArrayList);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.workoutContainer, workoutFragment)
                    .commitAllowingStateLoss();
            progressBar.setVisibility(View.INVISIBLE);

        }
    }


    private void showSnackBar() {
        Snackbar.make(coordinatorLayout, getString(R.string.network_problem), Snackbar.LENGTH_LONG)
                .show();
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onListFragmentInteraction(int index) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putParcelableArrayListExtra("exercises", exerciseArrayList);
        intent.putExtra("index", index);
        startActivity(intent);

    }

    public class RetroFitAsyncTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            Api api = retrofit.create(Api.class);

            Call<List<Exercise>> call = api.getExercises();

            call.enqueue(new Callback<List<Exercise>>() {
                @Override
                public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                    progressBar.setVisibility(View.INVISIBLE);

                    List<Exercise> list = response.body();
                    exerciseArrayList = new ArrayList<>();
                    exercises = new Exercise[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        exercises[i] = list.get(i);
                        exerciseArrayList.add(list.get(i));

                    }
                    if (savedIS == null) {

                        createFragment();
                    }
                }

                @Override
                public void onFailure(Call<List<Exercise>> call, Throwable t) {
                    showSnackBar();

                }


            });
            return null;

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createFragment() {
        if (exerciseArrayList != null) {
            WorkoutFragment workoutFragment = new WorkoutFragment();
            workoutFragment.setData(exerciseArrayList);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.workoutContainer, workoutFragment)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.workoutskey), exerciseArrayList);

    }
}

