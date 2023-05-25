package com.example.administrator.ClothingRentalSystem.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.houtai_admin.admin_content;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 本类实现管理员登录内容
 * 完成了登录按钮监听和重置按钮监听事件
 **/
public class AdminActivity extends BaseActivity {
    private EditText user, pwd;
    private Button register, resetting;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        countDownLatch = new CountDownLatch(1);
        init();//界面初始化
    }

    private void init() {

        //重置按钮的事件监听
        register = (Button) findViewById(R.id.AdminResetting);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resettingContent();
            }
        });

        //登录按钮的事件监听
        register = (Button) findViewById(R.id.AdminRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得输入的相关内容
                user = (EditText) findViewById(R.id.AdminName);
                pwd = (EditText) findViewById(R.id.AdminPassword);
                final String struser = user.getText().toString();
                final String strpwd = pwd.getText().toString();
                //在数据库中进行判断
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //查询
                            rs = DBUtils.getSelectResultSet("Select password,identity from user where username='"+struser+"';");
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
                    //判断是否有查询结果，若为1则是有结果，若为0则无该用户，若>1则有多个重名用户，请修改注册算法
                    if (rs.getRow()==1)
                    {
                        //如果管理员密码和身份认证正确就登录
                        if (strpwd.equals(rs.getString("password"))&& rs.getInt("identity")==1) {
                            Intent intent = new Intent(AdminActivity.this, admin_content.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AdminActivity.this, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
                            resettingContent();
                        }
                    }
                    else
                    {
                        Toast.makeText(AdminActivity.this, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
                        resettingContent();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 本方法是重置输入内容
     **/
    public void resettingContent()
    {
        ((EditText) findViewById(R.id.AdminName)).setText("");
        ((EditText) findViewById(R.id.AdminPassword)).setText("");
        ((EditText) findViewById(R.id.AdminName)).requestFocus();
    }

}
