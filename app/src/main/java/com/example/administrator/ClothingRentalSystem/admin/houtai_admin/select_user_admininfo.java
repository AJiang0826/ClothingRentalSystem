package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

/**
 * 查找用户详情的界面
 * 功能有：1.通过搜索界面输入的用户名进行用户信息的查询
 *       2.显示所查用户信息到EditText，并设置不可修改
 *       3.点击返回按钮回到select_user_admin搜素界面
 *
 */
public class select_user_admininfo extends AppCompatActivity {

    private String name;//user_admin搜索框中的值
    private CountDownLatch countDownLatch;//创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private String sql;
    private ResultSet rs;
    private Button return_;//返回按钮
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_admininfo);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        init();//界面初始化

    }
    private void init() {

        //获取上个界面搜索框中的值
        Bundle bundle=this.getIntent().getExtras();
        name=bundle.getString("name");
        System.out.println("strSearch_Name="+name);

        //查询username为name的用户信息
        sql="select username,password,name,sex,phone from user where username='"+name+"';";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结果
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
                //搜索到的用户信息显示到文本框中
                ((EditText) findViewById(R.id.username)).setText("用户名： "+rs.getString("username"));
                ((EditText) findViewById(R.id.password)).setText("密 码： "+rs.getString("password"));
                ((EditText) findViewById(R.id.name)).setText("姓 名： "+rs.getString("name"));
                ((EditText) findViewById(R.id.sex)).setText("性 别： "+rs.getString("sex"));
                ((EditText) findViewById(R.id.phone)).setText("手机号： "+rs.getString("phone"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取返回按钮并且添加事件
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

