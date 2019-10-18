package com.rushil.soundrecoder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rushil.soundrecoder.R;
import com.rushil.soundrecoder.adapters.RecordFileAdapter;
import com.rushil.soundrecoder.misc.Constants;

import java.io.File;

public class RecordsFragment extends Fragment {

    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvSoundRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        RecordFileAdapter recordFileAdapter = new RecordFileAdapter(context, getRecordingFiles());
        recyclerView.setAdapter(recordFileAdapter);
    }

    private File[] getRecordingFiles() {
        File file = new File(sharedPreferences
                .getString(Constants.FILE_LOCATION_KEY, "")
                .concat("/RB_records"));
        return file.listFiles();

    }

}
