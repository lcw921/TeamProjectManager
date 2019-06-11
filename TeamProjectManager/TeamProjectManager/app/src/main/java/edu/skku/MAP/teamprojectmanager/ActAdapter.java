package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActAdapter extends BaseAdapter {

    LayoutInflater inflater;
    private ArrayList<Act> items;

    public ActAdapter (Context context, ArrayList<Act> acts) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = acts;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Act getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view = inflater.inflate(R.layout.act_layout, viewGroup, false);
        }

        Act item = items.get(i);

        TextView tv1 = (TextView)view.findViewById(R.id.name);
        TextView tv2 = (TextView)view.findViewById(R.id.descriptor);
        TextView tv3 = (TextView)view.findViewById(R.id.worker);

        tv1.setText("할일 :" + item.getNAME());
        tv2.setText("설명 :" + item.getDescription());
        tv3.setText("수행자 :" + item.getWorker());

        return view;
    }
}
