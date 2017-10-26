package com.hackdtu.healthhistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.Disease;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiseaseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiseaseListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvDiseaseList;
    private SuperPrefs superPrefs;
    public DiseaseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiseaseListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiseaseListFragment newInstance(String param1, String param2) {
        DiseaseListFragment fragment = new DiseaseListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_disease_list, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView) {
        rvDiseaseList = (RecyclerView)rootView.findViewById(R.id.rv_disease_list);
        superPrefs = new SuperPrefs(getActivity());
    }

/*
    private void setUpRecyclerView() {
        DatabaseReference drDiseaseRV = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USERS_FB)
                .child(superPrefs.getString(Constants.USER_ID))
                .child(Constants.DISEASE_LIST_FB);
        drDiseaseRV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Disease> diseaseArrayList = new ArrayList<Disease>();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Disease disease = data.getValue(Disease.class);
                    diseaseArrayList.add(disease);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
