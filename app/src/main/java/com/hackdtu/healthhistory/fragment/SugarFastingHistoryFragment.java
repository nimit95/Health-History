package com.hackdtu.healthhistory.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.SugarLevel;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SugarFastingHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SugarFastingHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private EditText etSugarFasting;
    private Button btnSugarFasting;
    protected LineChart mChart;
    private SuperPrefs superPrefs;
    private DatabaseReference dataBaseReference;
    private ArrayList<SugarLevel> sugarLevelArrayList;
    private DatabaseReference temp;

    private LineChartView lineCHartView;
    public SugarFastingHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SugarFastingHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SugarFastingHistoryFragment newInstance(String type) {
        SugarFastingHistoryFragment fragment = new SugarFastingHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sugar_history, container, false);

        initializeViews(rootView);
        setUpFirebaseListener();
        return rootView;
    }

    private void initializeViews(View rootView) {
        etSugarFasting = (EditText) rootView.findViewById(R.id.et_sugar_fasting);
        btnSugarFasting = (Button) rootView.findViewById(R.id.btn_sugar_fasting);

        //lineCHartView = (LineChartView)rootView.findViewById(R.id.chart1);
        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        //mChart = new LineChart(getActivity());

        superPrefs = new SuperPrefs(getActivity());

        dataBaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USERS_FB).child(superPrefs.getString(Constants.USER_ID))
                .child(mParam1);

        //makeGraph();
    }


    private void setUpFirebaseListener() {

        dataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sugarLevelArrayList = null;
                sugarLevelArrayList = new ArrayList<SugarLevel>();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    SugarLevel sugarLevel = data.getValue(SugarLevel.class);
                    sugarLevelArrayList.add(sugarLevel);
                }
                if(sugarLevelArrayList.size() > 0)
                    makeGraph();
                else
                    Log.e("piyush","Null list of sugar level" );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        temp = dataBaseReference.push();
        //temp = temp.push();
        btnSugarFasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etSugarFasting.getText().toString().trim();

                if(value.length() > 0){
                    temp.setValue(new SugarLevel(String.valueOf(System.currentTimeMillis()),value));
                    temp = dataBaseReference.push();
                }
            }
        });
    }



    private void makeGraph() {
        List<Entry> entryList = new ArrayList<>();

        Log.e("piyush", "list obtained");
        String[] labels = new String[sugarLevelArrayList.size()];
        for(int i=0; i<sugarLevelArrayList.size();i++){
            Entry entry = new Entry(Float.parseFloat(""+(i+1)),
                    Float.parseFloat(sugarLevelArrayList.get(i).getValue()));

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(
                    Long.parseLong(sugarLevelArrayList.get(i).getTime())));
            labels[i] = dateString;
            entryList.add(entry);
        }
        LineDataSet lineDataSet = new LineDataSet(entryList,"Label");
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setValueTextColor(Color.BLUE);

        LineData lineData = new LineData(lineDataSet);

        mChart.setData(lineData);
        mChart.invalidate();
        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        mChart.getXAxis().setLabelRotationAngle(-45);
        mChart.invalidate();
    }
}
