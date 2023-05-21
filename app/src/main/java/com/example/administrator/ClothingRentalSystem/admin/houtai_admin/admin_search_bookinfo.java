package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

/**
 * 管理员查询衣物详细信息【搜索结果】
 */

public class admin_search_bookinfo extends AppCompatActivity {
    private ImageButton back_bt;
    private ListView listView;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_bookinfo);
        init();//界面初始化
    }

    private void init() {
        //回退--初始后台管理
        back_bt = (ImageButton) findViewById(R.id.Sel_Clothes_Back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_search_bookinfo.this, admin_search_book.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.Select_Clothes_List);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        Cursor cursor = help.querybookinfoname(name);
        String from[] = {"name", "type", "writer","publicer","rank","img"};
        int to[] = {R.id.book_name, R.id.Clothes_Type, R.id.Clothes_Author, R.id.Clothes_Publish, R.id.Clothes_Rank, R.id.Clothes_Info_Img};
        //调用admin_book_item.xml，上边一行id来自该xml
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.admin_book_item, cursor, from, to);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.Clothes_Info_Img) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageURI(Uri.parse(cursor.getString(columnIndex)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        listView.setAdapter(adapter);
    }
}
