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
 *
 */
public class select_reader_admin extends AppCompatActivity {
private ListView listView;

    private String name;
    private Button search_btn;
    private EditText search_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reader_admin);
        listView=(ListView)findViewById(R.id.sel_reader_list);
        databaseHelp help=new databaseHelp(getApplicationContext());
        Cursor cursor=help.query();
        String from[]={"user","password","name", "sex", "birthday", "phone"};
        int to[]={R.id.read_user,R.id.read_pwd,R.id.read_name, R.id.read_sex, R.id.read_birth, R.id.read_phone};
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.select_reader_item,cursor,from,to);
        listView.setAdapter(adapter);


        search_btn=findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(select_reader_admin.this, select_reader_admininfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",search_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
