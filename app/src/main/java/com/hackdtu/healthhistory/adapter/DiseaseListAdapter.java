package com.hackdtu.healthhistory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.Disease;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush on 26/10/17.
 */

public class DiseaseListAdapter extends RecyclerView.Adapter<DiseaseListAdapter.DiseaseViewHolder> {

    private ArrayList<Disease> diseaseArrayList;
    private Context context;

    public DiseaseListAdapter(ArrayList<Disease> diseaseArrayList, Context context) {
        this.diseaseArrayList = diseaseArrayList;
        this.context = context;
    }

    @Override
    public DiseaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.disease_list_item,parent,false);
        return new DiseaseViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DiseaseViewHolder holder, int position) {
        holder.name.setText(diseaseArrayList.get(position).getTitle());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(
                Long.parseLong(diseaseArrayList.get(position).getTime())));
        holder.date.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return diseaseArrayList.size();
    }

    class DiseaseViewHolder extends RecyclerView.ViewHolder{

        public TextView name,date;
        public DiseaseViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_rv_item_disease_name);
            date = (TextView) itemView.findViewById(R.id.tv_rv_item_disease_date);
        }
    }
}
