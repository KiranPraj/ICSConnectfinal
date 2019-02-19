package org.icspl.icsconnect.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import org.icspl.icsconnect.R;

public class ProgressBarDialog extends DialogFragment {
    public TextView progress_bar_message;
    private View v;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//use layoutinflater to inflate xml
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_progress, null);
        builder.setView(v);
        return builder.create();
    }

    public void setProgress_bar_message(String msg) {
        progress_bar_message = v.findViewById(R.id.progress_bar_message);
        progress_bar_message.setText(msg);
    }

}