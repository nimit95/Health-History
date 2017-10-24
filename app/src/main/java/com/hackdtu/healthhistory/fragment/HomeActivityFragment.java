package com.hackdtu.healthhistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.HeadingView;
import com.hackdtu.healthhistory.model.ImagePojo;
import com.hackdtu.healthhistory.model.InfoView;
import com.hackdtu.healthhistory.model.UserHistory;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class HomeActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ExpandablePlaceHolderView mExpandableView;
    private DatabaseReference databaseReference;
    private HashSet<String> typesOfImagePresent;
    private SuperPrefs superPrefs;
    private ArrayList<ImagePojo> xRay,mRI,doctPresciption,ultrasound,testReport,others;
    public HomeActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeActivityFragment newInstance(String param1, String param2) {
        HomeActivityFragment fragment = new HomeActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_activity, container, false);

        initializeViews(rootView);
        //setUpExpandableView();
        return rootView;
    }

    private void initializeViews(View rootView) {
        typesOfImagePresent = new HashSet<>();
        xRay = new ArrayList<>();
        mRI = new ArrayList<>();
        doctPresciption = new ArrayList<>();
        ultrasound = new ArrayList<>();
        testReport = new ArrayList<>();
        others = new ArrayList<>();

        mExpandableView = (ExpandablePlaceHolderView)rootView.findViewById(R.id.expandableView);
        superPrefs = new SuperPrefs(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference(
                superPrefs.getString(Constants.USER_ID)
        ).child(Constants.USER_IMG_FB);

        setUpExpandableView();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    ImagePojo image = data.getValue(ImagePojo.class);

                    typesOfImagePresent.add(image.getImgType());
                    int imgType = Integer.parseInt(image.getImgType());
                    if(imgType == Integer.parseInt(Constants.DOCTOR_PRESCRIPTION_TYPE))
                        doctPresciption.add(image);
                    else if(imgType == Integer.parseInt(Constants.MRI_TYPE))
                        mRI.add(image);
                    else if(imgType == Integer.parseInt(Constants.OTHERS_REPORT_TYPE))
                        others.add(image);
                    else if(imgType == Integer.parseInt(Constants.TEST_REPORT_TYPE))
                        testReport.add(image);
                    else if (imgType == Integer.parseInt(Constants.XRAY_TYPE))
                        xRay.add(image);
                    else if (imgType == Integer.parseInt(Constants.ULTRASOUND_TYPE))
                        ultrasound.add(image);
                }

                addChildViewsToView(0,xRay);
                addChildViewsToView(1,mRI);
                addChildViewsToView(2,doctPresciption);
                addChildViewsToView(3,ultrasound);
                addChildViewsToView(4,testReport);
                addChildViewsToView(4,others);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        typesOfImagePresent = new HashSet<>();


    }

    private void addChildViewsToView(int pos,ArrayList<ImagePojo> arr) {

        for(int i=0; i<arr.size();i++){
            mExpandableView.addChildView(pos,
                    new InfoView(getActivity(),arr.get(i)));
        }
    }

    private void setUpExpandableView() {


         //   if (typesOfImagePresent.contains(Constants.XRAY_TYPE))
                mExpandableView.addView(new HeadingView(getActivity(), "X Rays"));

         //   if (typesOfImagePresent.contains(Constants.MRI_TYPE))
                mExpandableView.addView(new HeadingView(getActivity(), "MRI Report"));

         //   if (typesOfImagePresent.contains(Constants.DOCTOR_PRESCRIPTION_TYPE))
                mExpandableView.addView(new HeadingView(getActivity(), "Doctor Prescription"));


        //    if (typesOfImagePresent.contains(Constants.ULTRASOUND_TYPE))
                mExpandableView.addView(new HeadingView(getActivity(), "Ultrasound"));

         //   if (typesOfImagePresent.contains(Constants.TEST_REPORT_TYPE))
                mExpandableView.addView(new HeadingView(getActivity(), "Test Reports"));

        //    if (typesOfImagePresent.contains(Constants.OTHERS_REPORT_TYPE))
                mExpandableView.addView(new HeadingView(getActivity(), "Others"));

        /*
        ArrayList<UserHistory> userHistoryList = new ArrayList<>();

        for(int j=0;j<4;j++){
            userHistoryList.add(new UserHistory("https://firebasestorage.googleapis.com/v0/b/healthhistory-459fe.appspot.com/o/WZvBhbeWmuMB6gTEt4149PUbN4t1images%2Ftesting?alt=media&token=52f4c92d-f14a-4cca-a201-ba85f489fbd5"
                    , "lalu", "21 june 2017", "Blood test Report", "Detailed report awaited","102"));
            UserHistory userHistory = userHistoryList.get(j);
            // mExpandableView.addView();
        }
        userHistoryList.add(1,new UserHistory("https://firebasestorage.googleapis.com/v0/b/healthhistory-459fe.appspot.com/o/WZvBhbeWmuMB6gTEt4149PUbN4t1images%2Ftesting?alt=media&token=52f4c92d-f14a-4cca-a201-ba85f489fbd5"
                , "Piyush", "20 june 2017", "Chest X Ray", "Little Congestion in chest","102"));
        mExpandableView.addChildView(0,new InfoView(getActivity(), userHistoryList.get(0)));
        mExpandableView.addChildView(0, new InfoView(getActivity(), userHistoryList.get(1)));*/
    }


}
