package com.example.haneenalawneh.extrafit;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param3";


    int index;
    SimpleDraweeView draweeView;
    TextView textView;
    TextView secondTextView;
    String imageUrl;
    LinearLayout timeLinearLayout;
    LinearLayout controlLinearLayout;
    ImageButton next;
    ImageButton prev;
    ImageButton pause;
    static long duration;
    CountDownTimer timer;
    static boolean isPaused;
    int maxSize;
    ProgressBar progressBar;
    Animatable animatable;
    CoordinatorLayout coordinatorLayout;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExerciseFragment.
     */
    public static ExerciseFragment newInstance(String imageUrl, int index) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, imageUrl);
        args.putInt(ARG_PARAM2, index);
        args.putBoolean(ARG_PARAM3, isPaused);
        args.putLong(ARG_PARAM4, duration);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imageUrl = getArguments().getString(ARG_PARAM1);
            index = getArguments().getInt(ARG_PARAM2);
            isPaused = getArguments().getBoolean(ARG_PARAM3);
            duration = getArguments().getLong(ARG_PARAM4);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_exercise2, container, false);
        draweeView = view.findViewById(R.id.exerciseImage);
        textView = view.findViewById(R.id.minutes);
        secondTextView = view.findViewById(R.id.Seconds);
        progressBar = view.findViewById(R.id.pb);
        timeLinearLayout = view.findViewById(R.id.timeLayout);
        controlLinearLayout = view.findViewById(R.id.controlersLayout);
        coordinatorLayout = view.findViewById(R.id.main_fragment_layout);
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        pause = view.findViewById(R.id.play);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPaused) {
                    pauseImage();
                } else {
                    startImage();
                }
            }
        });
        timeLinearLayout.setVisibility(View.INVISIBLE);
        controlLinearLayout.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index");
            isPaused = savedInstanceState.getBoolean("paused");
            imageUrl = savedInstanceState.getString("image");
            duration = savedInstanceState.getLong("duration");
            maxSize = savedInstanceState.getInt("maxSize", 0);
        }
        Log.e("h", index + "");
        if (index == 0) {
            prev.setVisibility(View.INVISIBLE);

        }
        if (index == maxSize) {

            next.setVisibility(View.INVISIBLE);
        }
        Long minutes = (duration / 1000) / 60;
        Long seconds = (duration / 1000) % 60;
        if (minutes > 9) {
            textView.setText(minutes + getString(R.string.colon));
        } else {
            textView.setText(getString(R.string.zero) + minutes + getString(R.string.colon));

        }
        if (seconds > 9) {
            secondTextView.setText(seconds + "");
        } else {
            secondTextView.setText(getString(R.string.zero) + seconds + "");

        }

        if (isPaused) {
            pause.setBackground(getResources().getDrawable(R.drawable.play));
        }

        loadImage();

        return view;


    }

    private void loadImage() {

        if (isNetworkAvailable()) {
            Uri uri = Uri.parse(imageUrl);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)

                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(
                                String id,
                                @Nullable ImageInfo imageInfo,
                                @Nullable Animatable anim) {
                            if (anim != null) {
                                // app-specific logic to enable animation starting
                                animatable = anim;
                                progressBar.setVisibility(View.INVISIBLE);
                                timeLinearLayout.setVisibility(View.VISIBLE);
                                controlLinearLayout.setVisibility(View.VISIBLE);
                                if (!isPaused) {
                                    anim.start();
                                    startCounter();


                                }
                            }
                        }
                    })

                    .build();
            draweeView.setController(controller);
//        CircularProgressDrawable c= new CircularProgressDrawable(draweeView.getContext());
//        c.setTint(getResources().getColor(R.color.colorPrimaryLight));
//        c.setStyle(8);
//        draweeView.getHierarchy().setProgressBarImage(c);
//    }
        } else {
            progressBar.setVisibility(View.INVISIBLE);

            Snackbar.make(coordinatorLayout, getString(R.string.network_problem), Snackbar.LENGTH_INDEFINITE).setAction("TRY AGAIN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    loadImage();

                }
            })
                    .show();

        }
    }

    private void startImage() {
        if (animatable != null) {
            animatable.start();
            pause.setBackground(getResources().getDrawable(R.drawable.pause));

            isPaused = false;
            startCounter();
        }
    }

    private void pauseImage() {
        if (animatable != null) {

            animatable.stop();
            pause.setBackground(getResources().getDrawable(R.drawable.play));

            isPaused = true;
            timer.cancel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (timer != null) {
            timer.cancel();
            isPaused = false;

        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIndex(int index) {
        this.index = index;

    }

    public void startCounter() {
        timer = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                duration = millisUntilFinished;

                Long minutes = (millisUntilFinished / 1000) / 60;
                Long seconds = (millisUntilFinished / 1000) % 60;
                if (minutes > 9) {
                    textView.setText(minutes + getString(R.string.colon));
                } else {
                    textView.setText(getString(R.string.zero) + minutes + getString(R.string.colon));

                }
                if (seconds > 9) {
                    secondTextView.setText(seconds + "");
                } else {
                    secondTextView.setText(getString(R.string.zero) + seconds + "");

                }
            }

            public void onFinish() {
                textView.setText(getString(R.string.done_msg));
                secondTextView.setText("");

                if (animatable != null) {
                    animatable.stop();
                }

            }

        }.start();


    }

    public void setDuration(int duration1) {
        duration = (long) duration1;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putBoolean("paused", isPaused);
        outState.putString("image", imageUrl);
        outState.putLong("duration", duration);
        outState.putInt("maxSize", maxSize);


    }

    public void onPause() {

        super.onPause();
        if (timer != null) {
            timer.cancel();
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


    }

    public void setMaxSize(int size) {
        maxSize = size;
    }


}


