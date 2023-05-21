package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

/**
 * 编辑读者页面
 * 功能：
 */

public class admin_editer_user extends AppCompatActivity {
    private ListView listView;
    private ImageButton back_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editer_user);
        init();//初始化界面

    }

    private void init() {
        back_bt = (ImageButton) findViewById(R.id.deleteuser_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editer_user.this, admin_manager_reader.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.delete_user_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.query();
        String from[]={"user","password","name", "sex", "birthday", "phone"};
        int to[]={R.id.user_user,R.id.user_pwd,R.id.user_name, R.id.user_sex, R.id.user_birth, R.id.user_phone};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.select_user_item, cursor, from, to);
        listView.setAdapter(adapter);
        //listview的单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //传值到修改界面
                int i = position + 1;
                Intent intent = new Intent(admin_editer_user.this, admin_update_reader.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
