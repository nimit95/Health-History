package com.hackdtu.healthhistory.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.Diseases;

import java.util.List;

/**
 * Created by dell on 2/11/2017.
 */

public class CustomList extends ArrayAdapter<Diseases> {
    private List<Diseases> diseases;
    private Context context;
    private LayoutInflater inflater;
    public CustomList(Context context, List<Diseases> diseases) {
        super(context, R.layout.list_item,diseases);
        this.context=context;
        this.diseases=diseases;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Diseases element=diseases.get(position);
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        }
        TextView dis=(TextView)convertView.findViewById(R.id.disease_name);
        TextView dat=(TextView)convertView.findViewById(R.id.disease_date);

        dis.setText(element.getDiseaseName());
        dat.setText(element.getTime());
        return convertView;
    }
}
