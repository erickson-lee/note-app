package com.erickson.saranote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> fileNameArr = new ArrayList<>();
    ArrayList<String> fileCreDate = new ArrayList<>();

    ListView searchListView;
    ArrayAdapter searchAdapter;
    ArrayList<String> searchArrList = new ArrayList<>();
    boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File folder = new File(getFilesDir().getPath());
        File [] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                Date fDate = new Date(file.lastModified());
                String strDate = DateFormat.getDateInstance().format(fDate);
                fileNameArr.add(file.getName());
                fileCreDate.add(strDate);

                //--------------------------------------------------------------

                String result="";
                String filePath = getFilesDir().getPath() + '/' + file.getName();

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

                String [] str = result.split("\n");
                StringBuilder sb = new StringBuilder();
                String singleLine;

                for (String element: str) {
                    if (!element.equals("")) {
                        String s = element + " ";
                        sb.append(s);
                    }
                }

                singleLine = sb.toString();
                searchArrList.add(singleLine);

                //--------------------------------------------------------------
            }
        }

        // set create file button
        Button j_createNewFile = findViewById(R.id.btnCreateNew);
        j_createNewFile.setOnClickListener(crtNewListener);

        // set listView dataSet

        ListView listView = findViewById(R.id.fileListView);

        MyAdapter adapter = new MyAdapter(fileNameArr, fileCreDate);

        listView.setAdapter(adapter);

        searchListView = findViewById(R.id.searchList);
        searchAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, searchArrList);

        searchListView.setOnItemClickListener(itemClickListener);


        SearchView fileSearchView;

        fileSearchView = findViewById(R.id.mySearchView);

        fileSearchView.setIconifiedByDefault(false);
        fileSearchView.setFocusable(false);
        fileSearchView.requestFocusFromTouch();
        fileSearchView.setSubmitButtonEnabled(true);
        fileSearchView.setQueryHint("請輸入關鍵字");
        fileSearchView.setOnQueryTextListener(search_Listener);

    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // use getItem catch position from ArrayAdapter
            String itemStr = (String) parent.getAdapter().getItem(position);
            Pattern pattern = Pattern.compile("^(\\S+) (.*)$");
            Matcher matcher = pattern.matcher(itemStr);
            String fileName="";
            if (matcher.find()) {
                fileName = matcher.group(1);
                //fileContext = matcher.group(2);
            }

            Intent intent = new Intent(MainActivity.this, note_page.class);
            intent.putExtra("FN", fileName);
            startActivity(intent);
        }
    };

    Button.OnClickListener crtNewListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, note_page.class);
            startActivity(intent);
        }
    };

    SearchView.OnQueryTextListener search_Listener = new SearchView.OnQueryTextListener() {

        private String TAG = getClass().getSimpleName();

        @Override
        public boolean onQueryTextSubmit(String query) {

            Log.d(TAG, "onQueryTextSubmit = " + query);

            Toast.makeText(MainActivity.this, "輸入了:" + query, Toast.LENGTH_SHORT).show();

            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d(TAG, "onQueryTextChange = " + newText);
            if (!isSearch && newText.length() != 0) { // when search bar has str
                searchListView.setAdapter(searchAdapter);
                isSearch = true;
            } else if (isSearch && newText.length() == 0) { // when search bar is empty
                searchListView.setAdapter(null);
                isSearch = false;
            }
            if (isSearch) {
                Filter filter = searchAdapter.getFilter();
                filter.filter(newText);
            }
            return true;
        }
    };



}



