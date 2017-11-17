package com.example.jiahang.pvrm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;

import static com.example.jiahang.pvrm.MyFileWriter.filepath;

/**
 * Created by zifeifeng on 10/23/17.
 */

public class ShareLastFileFragment extends DialogFragment {
    private String subjectID;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_file_not_shared_alert, null);
        subjectID = Shared.getString(getActivity().getApplicationContext(), Shared.SUBJECT_ID);

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Share the last file?")
                .setPositiveButton(R.string.shared_last_file, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shareByEmail();
                        Shared.putBoolean(getActivity(), Shared.HAS_BEEN_SAHRED, true);
                    }
                })
                .setNegativeButton(R.string.discard_last_file, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteFile();
                        Shared.putBoolean(getActivity(), Shared.HAS_BEEN_SAHRED, true);
                    }
                })
                .setCancelable(false)
                .create();
    }

    private void shareByEmail(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822").putExtra(Intent.EXTRA_EMAIL, new String[]{"zifeif2@illinois.edu"}).putExtra(android.content.Intent.EXTRA_SUBJECT, "Patient Data");
        String targetFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Android/data/com.example.jiahang.pvrm/files/bluetoothclass4" + File.separator + subjectID+".txt";
        Log.e("share File", targetFilePath);
        Uri attachmentUri = Uri.parse(targetFilePath);
        emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
        startActivity(emailIntent);
    }
    private void deleteFile(){
        File file = new File(getActivity().getExternalFilesDir(filepath), subjectID+".txt");
        file.delete();
    }

}
