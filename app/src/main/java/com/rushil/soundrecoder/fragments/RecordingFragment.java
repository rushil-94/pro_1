package com.rushil.soundrecoder.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rushil.soundrecoder.R;
import com.rushil.soundrecoder.misc.Constants;
import com.rushil.soundrecoder.services.SoundRecordService;

import java.io.File;

public class RecordingFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "RecordingFragment";
    // variables
    private Chronometer chronometer;
    private FloatingActionButton playPause;
    private boolean isPlay = false;
    private long duration, startTime, stopTime;
    private Context context;
    private String filePath, folderPath;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e(TAG, "onCreate: " + sharedPreferences.getAll());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_recording, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setListeners();
        createFolder();

    }

    private void initViews(View view) {
        chronometer = view.findViewById(R.id._chronometer);
        playPause = view.findViewById(R.id.playStop);
    }

    private void setListeners() {
        playPause.setOnClickListener(this);
    }

    private void createFolder() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File folder = new File(sharedPreferences
                    .getString(Constants.FILE_LOCATION_KEY, "") + "/RB_records");
            if (!folder.exists()) {
                boolean isCreated = folder.mkdir();
                if (isCreated) {
                    Log.e(TAG, "createFolder: folder is created ");
                    folderPath = folder.getAbsolutePath();
                } else {
                    Log.e(TAG, "createFolder:unable to create a folder. ");

                }
            } else {
                folderPath = folder.getAbsolutePath();
            }
        }
       /* else
        {
            File folder = new File(sharedPreferences.getString("fileLocation", "") + "/RB_records");
            if (!folder.exists()) {
                boolean isCreated = folder.mkdir();
                if (isCreated) {
                    Log.e(TAG, "createFolder: folder is created ");
                    folderPath = folder.getAbsolutePath();
                } else {
                    Log.e(TAG, "createFolder:unable to create a folder. ");

                }
            } else {
                folderPath = folder.getAbsolutePath();
            }
        }*/


    }

    private void createPath() {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            File file = new File(folder.getAbsolutePath(),
                    sharedPreferences.getString(Constants.FILE_LABEL_KEY, "Recording").concat("_") +
                            (files.length + 1) + sharedPreferences.getString("fileType", ".mp3"));
            filePath = file.getAbsolutePath();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.playStop:
                chronometer.setBase(SystemClock.elapsedRealtime());
                if (!isPlay) {
                    createPath();
                    playPause.setImageResource(R.drawable.ic_stop);
                    isPlay = true;
                    chronometer.start();
                    startTime = System.currentTimeMillis();
                    intent = new Intent(context, SoundRecordService.class);
                    intent.putExtra("fileName", filePath);
                    context.startService(intent);

                } else {
                    playPause.setImageResource(R.drawable.ic_play_arrow);
                    isPlay = false;
                    Log.e(TAG, "onClick: " + (SystemClock.elapsedRealtime() - chronometer.getBase()));
                    stopTime = System.currentTimeMillis();
                    duration = stopTime - startTime;
                    Log.e(TAG, "onClick: " + duration);
                    intent = new Intent(context, SoundRecordService.class);
                    context.stopService(intent);
                    chronometer.stop();


                }
                break;

            case R.id.pause:

                break;

            default:
                break;
        }

    }

}
