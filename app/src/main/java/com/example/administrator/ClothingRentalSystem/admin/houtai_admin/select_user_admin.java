package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

/**
 * 查找读者的界面
 * 功能有：1.通过用户名查询用户
 *       2.显示所有用户
 *
 * 变量含义：1.listView
 *         2.search_btn
 *         3.search_name
 *
 */
public class select_user_admin extends AppCompatActivity {
    private ListView listView;
    private Button search_btn;
    private EditText search_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_admin);
        listView=(ListView)findViewById(R.id.sel_reader_list);
        databaseHelp help=new databaseHelp(getApplicationContext());
        Cursor cursor=help.query();
        String from[]={"user","password","name", "sex", "phone"};
        int to[]={R.id.user_user,R.id.user_pwd,R.id.user_name, R.id.user_sex, R.id.user_phone};
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.select_user_item,cursor,from,to);
        listView.setAdapter(adapter);

        //搜索按钮监听
        search_btn=findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(select_user_admin.this, select_user_admininfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",search_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
