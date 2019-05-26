package com.example.haneenalawneh.extrafit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.haneenalawneh.extrafit.Authentication.LoginActivity;
import com.example.haneenalawneh.extrafit.Data.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference mUserDatabse;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView welcomingTV;
    LinearLayout linearLayout;
    CoordinatorLayout coordinatorLayout;
    ProgressBar pb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomingTV = findViewById(R.id.welcoming_msg);
        pb = findViewById(R.id.pb);
        coordinatorLayout = findViewById(R.id.main_layout);
        linearLayout = findViewById(R.id.second_layout);
        linearLayout.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //CHECKING USER PRESENCE
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }

        };
        checkNetwork();

    }


    public void moveToWorkouts(View view) {
        Intent intent = new Intent(this, WorkoutsActivity.class);
        startActivity(intent);

    }


    public void signOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void checkNetwork() {

        if (isNetworkAvailable()) {
            mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String name = user.getFirstName();
                    welcomingTV.setText(getString(R.string.welcome) + name + getString(R.string.comma) + getString(R.string.to_app));
                    pb.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting data failed

                }
            };
            mUserDatabse.addValueEventListener(userListener);


            Fresco.initialize(this);

            AdView mAdView = findViewById(R.id.adView);
            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);

        } else {
            pb.setVisibility(View.INVISIBLE);

            Snackbar.make(coordinatorLayout, getString(R.string.network_problem), Snackbar.LENGTH_INDEFINITE).setAction("TRY AGAIN", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    pb.setVisibility(View.VISIBLE);

                    checkNetwork();
                }
            }).show();

        }
    }
}
