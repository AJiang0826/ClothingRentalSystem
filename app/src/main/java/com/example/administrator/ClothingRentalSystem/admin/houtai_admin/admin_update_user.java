package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.registerActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.MD5Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 修改用户信息的页面
 * 功能：修改用户信息
 * 变量：
 */

public class admin_update_user extends BaseActivity {

    private EditText username, password ,name, sex, phone;

    private Button update_bt,cz;
    int id;
    private String struser,strpwd,strname,strsex,strphone,sql,names;
    private ResultSet rs;
    private int rows;
    private CountDownLatch countDownLatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_user);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        inut();//初始化界面
    }

    private void inut() {

        username = (EditText) findViewById(R.id.r_name);
        password = (EditText) findViewById(R.id.r_password);
        name = (EditText) findViewById(R.id.user_name);
        sex = (EditText)findViewById(R.id.r_sex);
        phone = (EditText)findViewById(R.id.r_phone);

        Bundle bundle=this.getIntent().getExtras();
        names=bundle.getString("name");
        System.out.println("nnnnname="+names);

        sql="select username,password,name,sex,phone from user where username='"+names+"';";
        System.out.println("sql="+sql);
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
                ((EditText) findViewById(R.id.r_name)).setText(rs.getString("username"));
                ((EditText) findViewById(R.id.r_password)).setText(rs.getString("password"));
                ((EditText) findViewById(R.id.user_name)).setText(rs.getString("name"));
                ((EditText) findViewById(R.id.r_phone)).setText(rs.getString("phone"));
                ((EditText) findViewById(R.id.r_sex)).setText(rs.getString("sex"));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        update_bt = (Button) findViewById(R.id.r_register);
        //修改按钮的事件监听
        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                struser = username.getText().toString();
                strpwd = password.getText().toString();
                strname = name.getText().toString();
                strsex = sex.getText().toString();
                strphone = phone.getText().toString();

                //对用户注册输入的信息进行验证，全部符合要求才能通过
                if (username.getText().length()!=6) {
                    Toast.makeText(admin_update_user.this,"请输入6位帐号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.getText().length()!=11){
                    Toast.makeText(admin_update_user.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sex.equals("")||struser.equals("")){
                    Toast.makeText(admin_update_user.this,"请输入完整内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                sql="update user set username='"+struser+"',password='"+strpwd+"',name='"
                        +strname+"',sex='"+strsex+"',phone='"+strphone+"' where username='"+names+"'";
                System.out.println("update_sql"+sql);
                countDownLatch = new CountDownLatch(1);
                //以下开始数据库操作，使用线程，插入用户
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //成功创建普通用户
                            rows=DBUtils.getUpdateRows(sql);
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
                    if (rows>0){
                        Toast.makeText(admin_update_user.this,"用户："+names+"修改成功！",Toast.LENGTH_SHORT).show();
                        //用户添加成功，就跳转到选择页面
                        // Intent intent = new Intent(admin_add_user.this, MainActivity.class);
                        //startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ActivityCollector.finishAll();

                Intent intent = new Intent(admin_update_user.this, admin_editer_user.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });

        // 获取重置按钮并且添加事件
        cz=(Button) findViewById(R.id.r_register_resetting);
        cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                password.setText("");
                name.setText("");
                sex.setText("");
                phone.setText("");

            }
        });

    }
}
