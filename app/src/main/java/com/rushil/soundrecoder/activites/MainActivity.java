package com.rushil.soundrecoder.activites;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rushil.soundrecoder.R;
import com.rushil.soundrecoder.broadcasters.MediaOperationReceiver;
import com.rushil.soundrecoder.fragments.RecordingFragment;
import com.rushil.soundrecoder.fragments.RecordsFragment;
import com.rushil.soundrecoder.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static int STORAGE_PERMISSION = 1;
    private static int AUDIO_PERMISSION = STORAGE_PERMISSION << 1;
    private static int BOTH_PERMISSION = STORAGE_PERMISSION << 2;
    private MediaOperationReceiver mediaOperationReceiver;

    /*private void checkPermissions() {
        int request_permission = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            request_permission |= STORAGE_PERMISSION;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            request_permission =
        }
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.recording:
                fragmentTransaction
                        .replace(R.id.frg_container, recordingFragment);
                break;
            case R.id.records:
                fragmentTransaction
                        .replace(R.id.frg_container, recordsFragment);
                break;
            case R.id.setting:
                fragmentTransaction
                        .replace(R.id.frg_container, settingFragment);
                break;
        }
        fragmentTransaction.commit();
        return true;
    }


    // variables
    private BottomNavigationView bottomNavigationView;
    private RelativeLayout container;
    private SettingFragment settingFragment;
    private RecordingFragment recordingFragment;
    private RecordsFragment recordsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(mediaOperationReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(null);
        }
        if (mediaOperationReceiver != null) {
            unregisterReceiver(mediaOperationReceiver);
        }
    }

    private void initViews() {
        container = findViewById(R.id.frg_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        settingFragment = new SettingFragment();
        recordsFragment = new RecordsFragment();
        recordingFragment = new RecordingFragment();
        mediaOperationReceiver = new MediaOperationReceiver();

    }

    private void setListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.recording);


    }

}
