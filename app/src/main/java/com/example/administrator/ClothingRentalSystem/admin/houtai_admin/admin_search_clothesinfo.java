package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.registerActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员查询衣物详细信息【搜索结果】
 */

public class admin_search_clothesinfo extends AppCompatActivity {
    private ImageButton back_bt;
    private ListView listView;
    private String name;

    private String strUserName,strPwd,strConfirmPwd,strName,strPhone,strSex,sql;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_clothesinfo);
        init();//界面初始化
    }

    private void init() {
        listView = (ListView) findViewById(R.id.Select_Clothes_List);//列表

        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        Cursor cursor = ps.query(Table_Name2, null, "name like ?", new String[]{"%"+name+"%"}, null, null, null, null);

        //Cursor cursor = help.querybookinfoname(name);
        String from[] = {"img", "name", "writer", "type","price","rank","publicer"};
        int to[] = {R.id.Clothes_Info_Img, R.id.Clothes_Name, R.id.Clothes_Author,
                    R.id.Clothes_Type, R.id.Clothes_Price, R.id.Clothes_Rank, R.id.Clothes_Publish};
        //调用admin_book_item.xml，上边一行id来自该xml
        adapter = new SimpleCursorAdapter(this, R.layout.admin_book_item, cursor, from, to);

        //以下开始数据库操作，使用线程，查询用户是否已经存在
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rs = DBUtils.getSelectResultSet("Select clothes_img,name,designer,type,price,rank,size from clothes_information");

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

                    rs.last();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    //该线程执行完毕-1
                    countDownLatch.countDown();
                }
            }
        }).start();
        //等待线程查询完结果
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();

        /*final databaseHelp help = new databaseHelp(getApplicationContext());
        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        Cursor cursor = help.querybookinfoname(name);
        String from[] = {"img", "name", "writer", "type","price","rank","publicer"};
        int to[] = {R.id.Clothes_Info_Img, R.id.Clothes_Name, R.id.Clothes_Author, R.id.Clothes_Type, R.id.Clothes_Price, R.id.Clothes_Rank, R.id.Clothes_Publish};
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
        listView.setAdapter(adapter);*/
    }
}
