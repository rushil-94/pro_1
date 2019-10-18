package com.rushil.soundrecoder.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MediaOperationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Intent.ACTION_MEDIA_REMOVED:
                    case Intent.ACTION_MEDIA_MOUNTED:
                    case Intent.ACTION_MEDIA_EJECT:
                        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().
//                                beginTransaction()
//                                .replace(R.id.frg_container, settingFragment).commit();
                        break;
                }
            }
        }
    }
}
