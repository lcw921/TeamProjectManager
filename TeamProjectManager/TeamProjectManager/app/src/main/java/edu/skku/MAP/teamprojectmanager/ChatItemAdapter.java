package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatItemAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<ChatItem> items;

    public ChatItemAdapter(Context context, ArrayList<ChatItem> chats) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = chats;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ChatItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view = inflater.inflate(R.layout.chat_layout, viewGroup, false);
        }

        ChatItem item = items.get(i);

        TextView tv1 = (TextView)view.findViewById(R.id.FROM);
        TextView tv2 = (TextView)view.findViewById(R.id.CONTENTS);
        TextView tv3 = (TextView)view.findViewById(R.id.TIME);

        tv1.setText(item.getFROM());
        tv2.setText(item.getCONTENTS());
        tv3.setText(item.getTIME());

        return view;
    }
}
