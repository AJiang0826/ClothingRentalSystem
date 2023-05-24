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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class admin_delete_userinfo extends AppCompatActivity {
    private ListView listView;
    private String name,sql;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private Button return_,r_delete_;
    private  int rows;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_userinfo);
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
                ((EditText) findViewById(R.id.username)).setText("用户名： "+rs.getString("username"));
                ((EditText) findViewById(R.id.password)).setText("密 码： "+rs.getString("password"));
                ((EditText) findViewById(R.id.name)).setText("姓 名： "+rs.getString("name"));
                ((EditText) findViewById(R.id.sex)).setText("性 别： "+rs.getString("sex"));
                ((EditText) findViewById(R.id.phone)).setText("手机号： "+rs.getString("phone"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取删除按钮并且添加事件
        r_delete_=(Button) findViewById(R.id.r_delete);
        r_delete_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sql="delete from user where username='"+name+"';";
                System.out.println("sql2="+sql);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rows= DBUtils.getUpdateRows(sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //该线程执行完毕-1
                            countDownLatch.countDown();
                        }
                    }
                }).start();

                //等待线程插入完结果
                try {
                    countDownLatch.await();
                    Toast.makeText(admin_delete_userinfo.this,"用户："+name+"删除成功！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(admin_delete_userinfo.this, admin_delete_user.class);
                startActivity(intent);
                ActivityCollector.finishAll();


            }
        });

        // 获取返回按钮并且添加事件
        return_=(Button) findViewById(R.id.r_return);
        return_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到搜索页面
                Intent intent = new Intent(admin_delete_userinfo.this, admin_delete_user.class);
                startActivity(intent);
            }
        });
    }
}
