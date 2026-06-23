package com.example.numberfindinggame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.GridView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.adapter.BackgroundAdapter;
import com.example.numberfindinggame.model.BackgroundItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BackgroundDialog {

    private final Dialog dialog;

    public BackgroundDialog(

            Context context,

            BackgroundDialogCallback callback

    ) {

        dialog = new Dialog(context);

        dialog.requestWindowFeature(

                Window.FEATURE_NO_TITLE

        );

        dialog.setContentView(

                R.layout.layout_dialog_background

        );

        GridView gridBackground =

                dialog.findViewById(

                        R.id.gridBackground

                );

        List<BackgroundItem> list =

                new ArrayList<>();

        Field[] fields =

                R.raw.class.getFields();

        for (Field field : fields) {

            String tenFile =

                    field.getName();

            if (tenFile.startsWith("bg")) {

                try {

                    int resId =

                            field.getInt(null);

                    list.add(

                            new BackgroundItem(

                                    resId,

                                    tenFile

                            )

                    );

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

        BackgroundAdapter adapter =

                new BackgroundAdapter(

                        context,

                        list

                );

        gridBackground.setAdapter(

                adapter

        );

        gridBackground

                .setOnItemClickListener(

                        (parent,

                         view,

                         position,

                         id) -> {

                            BackgroundItem item =

                                    list.get(position);

                            dialog.dismiss();

                            if (callback != null) {

                                callback.onSelect(

                                        item

                                );

                            }

                        });

    }

    public void show() {

        dialog.show();

    }

    public void dismiss() {

        dialog.dismiss();

    }

}