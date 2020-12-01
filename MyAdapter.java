package com.erickson.saranote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;

    private ArrayList<String> adaData;
    private ArrayList<String> fileDateList;

    public MyAdapter(ArrayList<String> data, ArrayList<String> dates) {
        this.adaData = data;
        this.fileDateList = dates;
    }

    @Override
    public int getCount() {
        return adaData == null ? 0 : adaData.size();
    }

    @Override
    public Object getItem(int position) {
        return adaData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (context == null)
            context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_item,null);
            viewHolder = new ViewHolder();
            viewHolder.fnTV = convertView.findViewById(R.id.txtShowFileName);
            viewHolder.dateTV = convertView.findViewById(R.id.txtShowCreDate);
            viewHolder.openBtn = convertView.findViewById(R.id.btnOpen);
            viewHolder.delBtn = convertView.findViewById(R.id.btnDel);
            convertView.setTag(viewHolder);
        }

        // get viewHolder data case
        viewHolder = (ViewHolder) convertView.getTag();

        // set data
        viewHolder.fnTV.setText(adaData.get(position));

        viewHolder.dateTV.setText(fileDateList.get(position));

        // set open file listener
        viewHolder.openBtn.setTag(R.id.openBtnId, position);
        viewHolder.openBtn.setOnClickListener(this);

        // set delete file listener
        viewHolder.delBtn.setTag(R.id.delBtnId, position);
        viewHolder.delBtn.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {

        Boolean isSuccessful;

        switch (v.getId()) {
            case R.id.btnOpen:
                int b = (int) v.getTag(R.id.openBtnId);

                Intent intent = new Intent();
                intent.setClass(context, note_page.class);
                intent.putExtra("FN", adaData.get(b));
                context.startActivity(intent);

                break;
            case R.id.btnDel:
                int t = (int) v.getTag(R.id.delBtnId);
                String fileName = adaData.get(t);

                String filePath = context.getFilesDir().getPath() + '/' + fileName;

                File file = new File(filePath);
                if (file.exists()){
                    isSuccessful = file.delete();

                    if (isSuccessful) {
                        Toast.makeText(context,
                                "檔案已刪除",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context,
                                "刪除失敗",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,
                            "檔案不存在",
                            Toast.LENGTH_SHORT).show();
                }

                Intent intentRefresh = new Intent();
                intentRefresh.setClass(context, MainActivity.class);
                context.startActivity(intentRefresh);

                break;
        }
    }

    static class ViewHolder{
        TextView fnTV;
        TextView dateTV;
        Button openBtn;
        Button delBtn;
    }
}
