package com.example.jiahang.pvrm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by zifeifeng on 11/10/17.
 */

public class PositiveResultFragment extends DialogFragment {
    private TextView mText;
    private String mResultText;

    public static final String ARG_RESULT = "result_text";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_succeed, null);
        mText = (TextView) v.findViewById(R.id.tv_positive_fragment);
        mResultText = getArguments().getString(ARG_RESULT);
        mText.setText(mResultText);
        return new AlertDialog.Builder(getActivity()).setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setCancelable(true).create();
    }

}
