package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class admin_select_clothesinfo extends AppCompatActivity {
    private ImageButton back_bt;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_bookinfo);
        init();//界面初始化

    }

    private void init() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        back_bt = (ImageButton) findViewById(R.id.sel_clothes_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_select_clothesinfo .this, admin_select_message.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.select_clothes_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.querybookinfo();
        String from[] = {"name", "type", "writer","publicer","rank","img", "price"};
        int to[] = {R.id.Clothes_Name, R.id.Clothes_Type, R.id.Clothes_Author, R.id.Clothes_Publish, R.id.Clothes_Rank, R.id.Clothes_Info_Img, R.id.Clothes_Price};
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
        //长时间停留则跳出来是否删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long temp = l;
                builder.setMessage("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        help.delbookinfo((int) temp);
                        //删除后重新显示
                        Cursor cursor = help.querybookinfo();
                        String from[] = {"name", "type", "writer","publicer","rank","img"};
                        int to[] = {R.id.book_name, R.id.Clothes_Type, R.id.Clothes_Author, R.id.Clothes_Publish, R.id.Clothes_Rank, R.id.Clothes_Info_Img};
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(admin_select_clothesinfo .this, R.layout.admin_book_item, cursor, from, to);
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
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


        //listview的单击事件,修改图书信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //传值到修改界面
                int i = position;
                Intent intent = new Intent(admin_select_clothesinfo .this, admin_update_clothes.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", i);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
