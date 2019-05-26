package com.example.haneenalawneh.extrafit.Widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.haneenalawneh.extrafit.Data.Api;
import com.example.haneenalawneh.extrafit.Data.Exercise;
import com.example.haneenalawneh.extrafit.Data.Tip;
import com.example.haneenalawneh.extrafit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService extends Service {
    String tipText;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildUpdate();

        return super.onStartCommand(intent, flags, startId);
    }

    private void buildUpdate() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Exercise>> call = api.getExercises();
        Call<List<Tip>> call2 = api.getTips();
        call2.enqueue(new Callback<List<Tip>>() {
            @Override
            public void onResponse(Call<List<Tip>> call, Response<List<Tip>> response) {
                List<Tip> list = response.body();
                ArrayList<Tip> tips = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    tips.add(list.get(i));

                }
                Random random = new Random();
                int r = random.nextInt(list.size());
                Log.i("t", tips.get(r).getTip());
                tipText = tips.get(r).getTip();

            }

            @Override
            public void onFailure(Call<List<Tip>> call, Throwable t) {

            }
        });

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.health_tip_widget);
        if (!TextUtils.isEmpty(tipText)) {
            view.setTextViewText(R.id.appwidget_text, tipText);
        }
        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, HealthTipWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
