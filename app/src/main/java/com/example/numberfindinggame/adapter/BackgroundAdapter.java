package com.example.numberfindinggame.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.dialog.ConfirmDialogThongTinHinhNenDong;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.model.BackgroundItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class BackgroundAdapter extends ArrayAdapter<BackgroundItem> {

    private final Context context;

    private final List<BackgroundItem> list;
    private BackgroundItem item = null;

    public BackgroundAdapter(
            Context context,
            List<BackgroundItem> list) {
        super(
                context,
                R.layout.item_background,
                list
        );
        this.context = context;
        this.list = list;

    }

    @Override

    public int getCount() {

        return list.size();

    }

    public void onSelectItem(BackgroundItem item) {
        this.item = item;
    }

    @Override

    public BackgroundItem getItem(int position) {

        return list.get(position);

    }

    @Override

    public long getItemId(int position) {

        return position;

    }


    @NonNull
    @Override
    public View getView(

            int position,

            View convertView,

            @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_background, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.txtName);
        MaterialCardView cardNenDong = convertView.findViewById(R.id.cardNenDong);
        MaterialCardView cardThongTin = convertView.findViewById(R.id.cardThongTin);

        BackgroundItem item = list.get(position);
        txtName.setText("▶ " + item.getName());

        if (this.item != null) {
            if (this.item.getResId() == item.getResId()) {
                cardNenDong.setStrokeWidth(dpToPx(1));
            } else {
                cardNenDong.setStrokeWidth(dpToPx(0));
            }
        } else {
            cardNenDong.setStrokeWidth(dpToPx(0));
        }

        cardThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfirmDialogThongTinHinhNenDong(context, item.getName(), item.getDsLinkHinhNenDong()).show();
            }
        });

        return convertView;

    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}