package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

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
 * 管理员查询图书信息
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
        listView = (ListView) findViewById(R.id.select_book_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        Cursor cursor = help.querybookinfoname(name);
        String from[] = {"name", "type", "writer","publicer","rank","img"};
        int to[] = {R.id.book_name, R.id.book_type, R.id.book_author, R.id.book_publish, R.id.book_rank, R.id.book_info_img};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.admin_book_item, cursor, from, to);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.book_info_img) {
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
