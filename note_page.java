package com.erickson.saranote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class note_page extends AppCompatActivity {
    //private final static String TAG="MainActivity";

    private EditText j_editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);

        // save file button
        Button j_btnSaveFile = findViewById(R.id.act_btnSave);

        j_btnSaveFile.setOnClickListener(btnListener_save);

        // set return button
        Button j_return2fileList = findViewById(R.id.act_btnReturn);

        j_return2fileList.setOnClickListener(btnListener_return);

        // set EditText View not Single Line
        j_editText = findViewById(R.id.act_edit);

        j_editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        j_editText.setGravity(Gravity.TOP);

        // Set no Single Line
        j_editText.setSingleLine(false);

        // Set no Horizontal scrolling
        j_editText.setHorizontallyScrolling(false);

        Intent intent = this.getIntent(); // get MyAdapter File Name
        String fileName = intent.getStringExtra("FN");

        if (fileName != null && !fileName.isEmpty()) {

            String result="";
            String filePath = getFilesDir().getPath() + '/' + fileName;

            try {

                FileInputStream f_in =
                        new FileInputStream(filePath);
                int length = f_in.available();
                byte[] buffer = new byte[length];
                f_in.read(buffer);
                f_in.close();
                result = new String(buffer, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            j_editText.setText(result);

        }

    }

    Button.OnClickListener btnListener_return = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(note_page.this, MainActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener btnListener_save = new Button.OnClickListener() {

        String filename, str;

        @Override
        public void onClick(View v) {

            str = j_editText.getText().toString();
            String [] result = str.split("\n");
            String first_line="";

            //final AlertDialog.Builder editDialog = new AlertDialog.Builder(MainActivity.this);
            AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(note_page.this);
            editDialogBuilder.setTitle("確定儲存為以下檔名嗎?");

            //LayoutInflater li = LayoutInflater.from(MainActivity.this);
            //View dialogView = li.inflate(R.layout.alert_dialog,null);


            final EditText j_dialogEditText = new EditText(note_page.this);
            //final EditText j_dialogEditText = dialogView.findViewById(R.id.edtInputFN);

            for (String element: result) {
                if (!element.equals("")) {
                    first_line = element;
                    break;
                }
            }

            if (!first_line.equals("")) {

                j_dialogEditText.setText(first_line);
                editDialogBuilder.setView(j_dialogEditText);

                editDialogBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        filename = j_dialogEditText.getText().toString();

                        try {
                            FileOutputStream f_out = openFileOutput(filename, MODE_APPEND);
                            BufferedOutputStream buff = new BufferedOutputStream(f_out);
                            byte[] bytes = str.getBytes();
                            buff.write(bytes);
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent();
                        intent.setClass(note_page.this, MainActivity.class);
                        startActivity(intent);

                    }
                });

                editDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog editDialog = editDialogBuilder.create();
                editDialog.show();
            } else {
                Toast toast = Toast.makeText(note_page.this,
                        "檔案不能為空", Toast.LENGTH_LONG);
                toast.show();
            }

        }
    };
}
