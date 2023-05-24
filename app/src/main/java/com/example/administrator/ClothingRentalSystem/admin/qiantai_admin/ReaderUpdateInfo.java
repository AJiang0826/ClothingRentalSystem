package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.registerActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.MD5Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
/**
 * 修改读者信息的页面
 */

public class ReaderUpdateInfo extends BaseActivity {
    private EditText user_ed, pwd_ed, username , sex, phone, register_identify,birthday;
    private Button modify_bt;
    String uname2;
    private String strUserName,strPwd,strConfirmPwd,strName,strPhone,strSex,sql,strregister_identify;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;

    private String md5Psw;

    /**
     * 以下方法是静态代码块，用来初始化数据库，并载入连接进入内存
     * 调用的是DBUtils工具类
     * 必须使用多线程才可使得网络通信
     **/
    static{
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始连接数据库……");
                // new DBUtils("192.168.43.149:3306","clothes_rental_system","Android","123456");
                new DBUtils("192.168.43.71:3306","clothes_rental_system","root","123456");
                System.out.println("查看数据库连接是否成立："+ (DBUtils.conn!=null));
            }
        }
        ).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_update_info);
        countDownLatch = new CountDownLatch(1);
        inut();//初始化界面
    }

    private void inut() {












        //将id返回的记录查询在edittext
        user_ed = (EditText) findViewById(R.id.u_name);
        pwd_ed = (EditText) findViewById(R.id.u_password);
        username = findViewById(R.id.user_name);
        sex = findViewById(R.id.u_sex);
        phone = findViewById(R.id.u_phone);

        modify_bt = (Button) findViewById(R.id.r_modify);

        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
        uname2 = perf.getString("users", "");//获得当前用户名称
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.queryname(uname2);
       /* if (cursor.getCount() > 0) {
            cursor.moveToFirst();
//            不能修改用户名
            user_ed.setText(cursor.getString(cursor.getColumnIndex("user")));
            pwd_ed.setText(cursor.getString(cursor.getColumnIndex("password")));
            username.setText(cursor.getString(cursor.getColumnIndex("name")));
            birthday.setText(cursor.getString(cursor.getColumnIndex("birthday")));
            phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
            sex.setText(cursor.getString(cursor.getColumnIndex("sex")));
        }*/





        //修改按钮的事件监听
        modify_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUserName = user_ed.getText().toString();
               // strName = username.getText().toString();
                strPwd = pwd_ed.getText().toString();
                strName = username.getText().toString();
                strSex = sex.getText().toString();
                strPhone = phone.getText().toString();
             //   strregister_identify= register_identify.getText().toString();


            //  sql="Select * from user where username='"+struser+"'";
                sql="UPDATE user SET name = '"+strName +"',password= '"+strPwd +"',sex= '"+strPwd +"',sex= '"+strPhone +"'where username='"+MainActivity.getStrUserName()+"'";
                //以下开始数据库操作，使用线程，查询用户是否存在
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rs = DBUtils.getSelectResultSet("Select * from user where username='"+strUserName+"'");
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
                    if(rs.getRow()!=0){
                        System.out.println("rs.getRow="+rs.getRow());
                        //return;
                    }
                    else
                        Toast.makeText(ReaderUpdateInfo.this,"用户不存在",Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                countDownLatch = new CountDownLatch(1);
                //以下开始数据库操作，使用线程，插入用户
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //成功修改用户信息
                            rows=DBUtils.getUpdateRows(sql);
                            System.out.println("成功修改用户信息");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //该线程执行完毕-1
                            countDownLatch.countDown();
                        }
                    }
                }).start();
                //等待线程修改完结果
                Toast.makeText(ReaderUpdateInfo.this, "除用户名外信息修改成功", Toast.LENGTH_LONG).show();
                ActivityCollector.finishAll();
            }
        });

    }
}
