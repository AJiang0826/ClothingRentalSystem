package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

public class select_user_admininfo extends AppCompatActivity {
    private ListView listView;
    private String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reader_admininfo);
        init();//界面初始化

    }
    private void init() {
        listView = (ListView) findViewById(R.id.select_admin_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        Cursor cursor = help.queryname(name);
        String from[]={"user","password","name", "sex", "phone"};
        int to[]={R.id.user_user,R.id.user_pwd,R.id.user_name, R.id.user_sex, R.id.user_phone};
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this, R.layout.select_user_item,cursor,from,to);
        listView.setAdapter(adapter);
    }


}

