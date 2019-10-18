package com.rushil.soundrecoder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rushil.soundrecoder.R;
import com.rushil.soundrecoder.fragments.PlaybackFragment;

import java.io.File;

public class RecordFileAdapter extends RecyclerView.Adapter<RecordFileAdapter.Holder> {
    private Context context;
    private File[] files;

    public RecordFileAdapter(Context context, File[] files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.layout_sound_record_files, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvFileName.setText(files[position].getName().substring(0, files[position].getName().lastIndexOf('.')));
    }

    @Override
    public int getItemCount() {
        return (files != null && files.length > 0) ? files.length : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView tvFileName;

        Holder(@NonNull View itemView) {
            super(itemView);
            this.tvFileName = itemView.findViewById(R.id.tvFileName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaybackFragment playbackFragment = new PlaybackFragment();
                    playbackFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
                }
            });
        }
    }
}
