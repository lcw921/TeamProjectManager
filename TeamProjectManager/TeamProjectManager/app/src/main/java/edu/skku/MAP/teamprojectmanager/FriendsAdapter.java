package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendsAdapter extends BaseAdapter{
    LayoutInflater inflater;
    private ArrayList<FriendsItem> items;

    public FriendsAdapter (Context context, ArrayList<FriendsItem> friends) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = friends;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public FriendsItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view = inflater.inflate(R.layout.frient_list, viewGroup, false);
        }

        FriendsItem item = items.get(i);

        TextView tv1 = (TextView)view.findViewById(R.id.FRIEND_NAME_TV);

        tv1.setText(item.getNAME());

        return view;
    }
}