package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatListItemAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<ChatListItem> items;

    public ChatListItemAdapter(Context context, ArrayList<ChatListItem> chatlists) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = chatlists;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ChatListItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view = inflater.inflate(R.layout.chatroomlist_layout, viewGroup, false);
        }

        ChatListItem item = items.get(i);

        TextView tv1 = (TextView)view.findViewById(R.id.PROJECTNAME);
        TextView tv2 = (TextView)view.findViewById(R.id.FROM);
        TextView tv3 = (TextView)view.findViewById(R.id.TO);

        tv1.setText(item.getPROJECTNAME());
        tv2.setText(item.getFROM());
        tv3.setText(item.getTO());

        return view;
    }
}
