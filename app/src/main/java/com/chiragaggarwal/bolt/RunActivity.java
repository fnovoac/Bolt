package com.chiragaggarwal.bolt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.chiragaggarwal.bolt.databinding.ActivityMainBinding;
import com.chiragaggarwal.bolt.timer.ElapsedTime;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunActivity extends AppCompatActivity {
    private RunServiceBroadcastReceiver runServiceBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private RunServiceViewModel runServiceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        runServiceViewModel = new RunServiceViewModel();
        activityMainBinding.setRunServiceViewModel(runServiceViewModel);
        ButterKnife.bind(this);
        runServiceBroadcastReceiver = new RunServiceBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter runServiceIntentFilter = new IntentFilter();
        runServiceIntentFilter.addAction(RunService.ACTION_TIME_TICK);
        localBroadcastManager.registerReceiver(runServiceBroadcastReceiver, runServiceIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(runServiceBroadcastReceiver);
    }

    @OnClick(R.id.button_start_activity)
    public void onTextStartActivityClick() {
        Intent runServiceIntent = new Intent(this, RunService.class);
        startService(runServiceIntent);
    }

    private class RunServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RunService.ACTION_TIME_TICK)) {
                ElapsedTime elapsedTime = intent.getParcelableExtra(ElapsedTime.TAG);
                runServiceViewModel.setElapsedTime(elapsedTime);
            } else if (action.equals(RunService.ACTION_FETCH_ACCURATE_LOCATION)) {
                Location location = intent.getParcelableExtra(Location.TAG);
                runServiceViewModel.setLocation(location);
            }
        }
    }
}
