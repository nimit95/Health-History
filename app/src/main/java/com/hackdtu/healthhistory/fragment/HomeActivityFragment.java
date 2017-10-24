package com.hackdtu.healthhistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.HeadingView;
import com.hackdtu.healthhistory.model.InfoView;
import com.hackdtu.healthhistory.model.UserHistory;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.ArrayList;

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
        setUpExpandableView();
        return rootView;
    }

    private void initializeViews(View rootView) {
        mExpandableView = (ExpandablePlaceHolderView)rootView.findViewById(R.id.expandableView);
    }

    private void setUpExpandableView() {
        //new ShowList().execute();

        /*for(int i=0;i<10;i++) {
            mExpandableView.addView(new HeadingView(getApplicationContext(), "Heading" + i));
            for(int j=0;j<4;j++){
                UserHistory userHistory = new UserHistory();
                userHistory.setTitle("title"+j);
                userHistory.setDescription("description"+j);
                mExpandableView.addView(new InfoView(getApplicationContext(), userHistory));
            }
        }*/
        for(int i=0;i<4;i++) {
            if (i == 0)
                mExpandableView.addView(new HeadingView(getActivity(), "June, 2017"));
            if (i == 1)
                mExpandableView.addView(new HeadingView(getActivity(), "May, 2017"));
            if (i == 2)
                mExpandableView.addView(new HeadingView(getActivity(), "March, 2017"));
            if (i == 3)
                mExpandableView.addView(new HeadingView(getActivity(), "January, 2017"));
        }
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
        mExpandableView.addChildView(0, new InfoView(getActivity(), userHistoryList.get(1)));
    }


}
