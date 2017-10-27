package com.hackdtu.healthhistory.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import info.hoang8f.widget.FButton;

/**
 * Created by piyush on 25/10/17.
 */

public class MainActionDialog extends DialogFragment implements View.OnClickListener{
    private SuperPrefs superPrefs;
    private FButton fbtnCataract,fbtnSkinCancer,fbtnUpload;
    private int REQ_CAMERA_IMAGE=10;
    private int REQ_CHECK_CATARACT = 100;
    private int REQ_CHECK_SKIN = 200;
    public static final String TAG = MainActionDialog.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getDialog().setTitle("Add New Contractor");

        View view= inflater.inflate(R.layout.fragment_main_action_dialog, container);
        superPrefs = new SuperPrefs(getActivity());

        initializer(view);
        return view;
    }

    private void initializer(View view) {
        fbtnCataract = (FButton) view.findViewById(R.id.fbtn_cataract);
        fbtnSkinCancer = (FButton) view.findViewById(R.id.fbtn_skincancer);
        fbtnUpload = (FButton) view.findViewById(R.id.fbtn_upload);
        fbtnCataract.setOnClickListener(this);
        fbtnSkinCancer.setOnClickListener(this);
        fbtnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fbtn_cataract:
                cataractDetect();
                break;
            case R.id.fbtn_skincancer:
                skinCancerDetect();
                break;
            case R.id.fbtn_upload:
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        Log.e(TAG, "uploadImage: " );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent, REQ_CAMERA_IMAGE);
        this.dismiss();
    }

    private void skinCancerDetect() {
        Log.e(TAG, "skinCancerDetect: ");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent, REQ_CHECK_SKIN);
        this.dismiss();
    }

    private void cataractDetect() {
        Log.e(TAG, "cataractDetect: " );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent, REQ_CHECK_CATARACT);
        this.dismiss();
    }
}
