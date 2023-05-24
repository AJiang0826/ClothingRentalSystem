package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class select_user_admininfo extends AppCompatActivity {
    private ListView listView;
    private String name;
    private CountDownLatch countDownLatch;
    private String sql,nametxt;
    private ResultSet rs;
    private int rows;
    private EditText searchname;
    private Button return_;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_admininfo);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        init();//界面初始化

    }
    private void init() {

        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        System.out.println("strSearch_Name="+name);

        sql="select username,password,name,sex,phone from user where username='"+name+"';";
        System.out.println("sql="+sql);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结
                     rs= DBUtils.getSelectResultSet(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //该线程执行完毕-1
                    countDownLatch.countDown();
                }
            }
        }).start();

       //等待线程插入完结果，把结果集转换成一定格式，并呈现在页面上
        try {
            countDownLatch.await();//阻塞等待线程执行完毕

            while (rs.next()) {
                ((EditText) findViewById(R.id.username)).setText(rs.getString("username"));
                ((EditText) findViewById(R.id.password)).setText(rs.getString("password"));
                ((EditText) findViewById(R.id.name)).setText(rs.getString("name"));
                ((EditText) findViewById(R.id.sex)).setText(rs.getString("sex"));
                ((EditText) findViewById(R.id.phone)).setText(rs.getString("phone"));


                //设置不可编辑
                ((EditText) findViewById(R.id.username)).setFocusable(false);
                ((EditText) findViewById(R.id.password)).setFocusable(false);
                ((EditText) findViewById(R.id.name)).setFocusable(false);
                ((EditText) findViewById(R.id.sex)).setFocusable(false);
                ((EditText) findViewById(R.id.phone)).setFocusable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取重置按钮并且添加事件
        return_=(Button) findViewById(R.id.r_return);
        return_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到搜索页面
                Intent intent = new Intent(select_user_admininfo.this, select_user_admin.class);
                startActivity(intent);
            }
        });


    }


}

