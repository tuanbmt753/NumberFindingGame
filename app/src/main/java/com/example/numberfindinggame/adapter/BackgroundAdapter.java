package com.example.numberfindinggame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.model.BackgroundItem;

import java.util.List;

public class BackgroundAdapter
        extends BaseAdapter {

    private final Context context;

    private final List<BackgroundItem> list;

    public BackgroundAdapter(

            Context context,

            List<BackgroundItem> list
    ) {

        this.context = context;

        this.list = list;

    }

    @Override

    public int getCount() {

        return list.size();

    }

    @Override

    public Object getItem(int position) {

        return list.get(position);

    }

    @Override

    public long getItemId(int position) {

        return position;

    }

    @Override

    public View getView(

            int position,

            View convertView,

            ViewGroup parent
    ) {

        if (convertView == null) {

            convertView =

                    LayoutInflater

                            .from(context)

                            .inflate(

                                    R.layout.item_background,

                                    parent,

                                    false
                            );

        }

        TextView txtName =

                convertView.findViewById(

                        R.id.txtName
                );

        BackgroundItem item =

                list.get(position);

        txtName.setText(

                "▶ " +

                        item.getName()
        );

        return convertView;

    }

}