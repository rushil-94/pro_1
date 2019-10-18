package com.rushil.soundrecoder.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.rushil.soundrecoder.R;
import com.rushil.soundrecoder.misc.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class MyPref extends PreferenceFragmentCompat implements ListPreference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "MyPref";
    private Context context;
    private ArrayList<setOnPreferenceUpdateListeners> preferenceUpdateListeners;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.mypref, rootKey);
        final ListPreference fileLocationListPreference = findPreference(Constants.FILE_LOCATION_KEY);
        final ListPreference encoderListPreference = findPreference(Constants.FILE_ENCODER_KEY);
        final ListPreference fileTypeListPreference = findPreference(Constants.FILE_TYPE_KEY);
        EditTextPreference editTextPreference = findPreference(Constants.FILE_LABEL_KEY);

        Log.e(TAG, "onCreatePreferences: " + getPreferenceManager().getSharedPreferencesName());


        if (fileLocationListPreference != null) {
            checkDirectories(fileLocationListPreference);
            if (fileLocationListPreference.getEntry() == null) {
                fileLocationListPreference.setValueIndex(0);
            }
            fileLocationListPreference.setTitle(fileLocationListPreference.getEntry());
            fileLocationListPreference.setOnPreferenceChangeListener(this);
        }

        if (encoderListPreference != null) {
            makeAudioEncoderList(encoderListPreference);
            encoderListPreference.setOnPreferenceChangeListener(this);
            if (encoderListPreference.getEntry() == null) {
                encoderListPreference.setValueIndex(0);

            }
            encoderListPreference.setTitle(encoderListPreference.getEntry());

        }

        if (fileTypeListPreference != null) {
            fileTypeList(fileTypeListPreference);
            fileTypeListPreference.setOnPreferenceChangeListener(this);
            if (fileTypeListPreference.getEntry() == null) {
                fileTypeListPreference.setValueIndex(0);

            }

            fileTypeListPreference.setTitle(fileTypeListPreference.getEntry());

        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }

    private void checkDirectories(ListPreference listPreference) {
        Hashtable<String, String> entries = new Hashtable<>();
        // internal storage path
        entries.put("Path-1", context.getFilesDir().getAbsolutePath());

        // external storage path
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File[] files = context.getExternalFilesDirs(null);
            if (files != null) {
                for (int index = 0; index < files.length; index++) {
                    if (files[index] != null) {
                        if (files[index].getAbsolutePath().contains("emulated")) {
                            entries.put("External path-" + (index + 1), files[index].getAbsolutePath());

                        } else {
                            entries.put("External path-" + (index + 1), files[index].getAbsolutePath());
                        }

                    }

                }
            }
        }

        String[] str = new String[entries.size()];
        entries.values().toArray(str);
        listPreference.setEntryValues(str);

        str = new String[entries.size()];
        entries.keySet().toArray(str);
        listPreference.setEntries(str);
    }

    private void makeAudioEncoderList(ListPreference listPreference) {
        String[] entries = {"DEFAULT", "AMR_NB", "AMR_WB", "AAC", "HE_AAC", "AAC_ELD", "VORBIS", "OPUS"};
        String[] entriesValues = {"0", "1", "2", "3", "4", "5", "6", "7"};

        listPreference.setEntries(entries);
        listPreference.setEntryValues(entriesValues);
    }


    private void fileTypeList(ListPreference listPreference) {
        String[] entries = {"mp3", "mp4", "3gp"};
        String[] entriesValues = {".mp3", ".mp4", ".3gp"};

        listPreference.setEntries(entries);
        listPreference.setEntryValues(entriesValues);

    }

    public void setPreferenceUpdateListeners(setOnPreferenceUpdateListeners preferenceUpdateListeners) {
        if (preferenceUpdateListeners != null) {
            this.preferenceUpdateListeners = new ArrayList<>();
        }
        this.preferenceUpdateListeners.add(preferenceUpdateListeners);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setTitle(((ListPreference) preference).getEntries()[((ListPreference) preference).findIndexOfValue(newValue.toString())]);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (preferenceUpdateListeners != null) {
            for (setOnPreferenceUpdateListeners listeners : preferenceUpdateListeners) {
                listeners.onPreferenceUpdateListeners(sharedPreferences);
            }
        }
    }

    public interface setOnPreferenceUpdateListeners {
        void onPreferenceUpdateListeners(SharedPreferences sharedPreferences);
    }


}
