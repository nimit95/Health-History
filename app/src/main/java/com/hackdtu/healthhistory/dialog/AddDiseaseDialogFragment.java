package com.hackdtu.healthhistory.dialog;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.Disease;
import com.hackdtu.healthhistory.model.GlobalDisease;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dell on 2/25/2017.
 */

public class AddDiseaseDialogFragment extends DialogFragment {
    private Button done,cancel,delete;
    private DatePicker datePicker;
    private EditText todoSymptoms,todoDescription;
    private Spinner spnDiseaseTitle;
    private SuperPrefs superPrefs;
    private DatabaseReference databaseReference,diseaseDatabseReference;
    private ArrayList<GlobalDisease> globalDiseaseList;

    public AddDiseaseDialogFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_disease_details, container);

        initializer(view);

        getDialog().setTitle("Your Suffering");

        setUpSpinnerTitleListener();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if(todoTitle.getText().toString().trim().length() > 0) {
                    pushToFirebaseDatabase();
                    getDialog().dismiss();
                //}
            }
        });
        //setUpRecyclerView();
        return view;
    }

    private void pushToFirebaseDatabase() {
        Disease disease = new Disease(
                spnDiseaseTitle.getSelectedItem().toString(),
                String.valueOf(System.currentTimeMillis()),
                todoSymptoms.getText().toString().trim(),
                todoDescription.getText().toString().trim()
        );
        databaseReference.setValue(disease);
        GlobalDisease globalDisease = globalDiseaseList.get(spnDiseaseTitle.getSelectedItemPosition());
        globalDisease.setNumCount(globalDisease.getNumCount()+1);

        diseaseDatabseReference.child(String.valueOf(spnDiseaseTitle.getSelectedItemPosition()))
                .setValue(globalDisease);
    }


    private void initializer(View view) {
        superPrefs = new SuperPrefs(getActivity());
        done=(Button)view.findViewById(R.id.done_button);
        datePicker=(DatePicker) view.findViewById(R.id.date_picker);
        todoDescription = (EditText)view.findViewById(R.id.todo_description);
        todoSymptoms = (EditText)view.findViewById(R.id.todo_symptoms);

        spnDiseaseTitle = (Spinner)view.findViewById(R.id.spn_disease_title);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_FB)
                .child(superPrefs.getString(Constants.USER_ID))
                .child(Constants.DISEASE_LIST_FB).push();
        //rvDiseaseList.setLayoutManager(new LinearLayoutManager(getActivity()));


        diseaseDatabseReference = FirebaseDatabase.getInstance().getReference(Constants.DISEASES_FB);
    }
    private void setUpSpinnerTitleListener() {
        diseaseDatabseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                globalDiseaseList=null;

                 globalDiseaseList = new ArrayList<GlobalDisease>();
                ArrayList<String> nameOfDiseases = new ArrayList<String>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    GlobalDisease globalDisease = data.getValue(GlobalDisease.class);
                    globalDiseaseList.add(globalDisease);
                    nameOfDiseases.add(globalDisease.getDiseaseName());
                }
                setUpSpinner(nameOfDiseases);
                diseaseDatabseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpSpinner(ArrayList<String> globalDiseaseList) {

        Log.e("setUpSpinner: ", globalDiseaseList.toString());
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item_global_disease,globalDiseaseList);*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,globalDiseaseList);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDiseaseTitle.setAdapter(adapter);
    }
}
