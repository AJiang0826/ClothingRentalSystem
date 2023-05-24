package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

/**
 * 修改读者信息的页面
 */

public class UserUpdateInfo extends BaseActivity {
    private EditText user_ed, pwd_ed, username , sex, phone, register_identify,birthday;
    private Button modify_bt;
    String uname2;
    private String strUserName,strPwd,strConfirmPwd,strName,strPhone,strSex,sql,strregister_identify;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;

    private String md5Psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update_info);
        countDownLatch = new CountDownLatch(1);
        inut();//初始化界面
    }

    private void inut() {


        //将id返回的记录查询在edittext
        user_ed = (EditText) findViewById(R.id.u_name);
        pwd_ed = (EditText) findViewById(R.id.u_password);
        username = (EditText) findViewById(R.id.user_name);
        sex = (EditText) findViewById(R.id.u_sex);
        phone = (EditText) findViewById(R.id.u_phone);
        modify_bt = (Button) findViewById(R.id.r_modify);

        sql="select username,password,name,sex,phone from user where username='"+MainActivity.getStrUserName()+"';";
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
                ((EditText) findViewById(R.id.u_name)).setText(rs.getString("username"));
                ((EditText) findViewById(R.id.u_password)).setText(rs.getString("password"));
                ((EditText) findViewById(R.id.user_name)).setText(rs.getString("name"));
                ((EditText) findViewById(R.id.u_sex)).setText(rs.getString("sex"));
                ((EditText) findViewById(R.id.u_phone)).setText(rs.getString("phone"));

            }



        } catch (Exception e) {
            e.printStackTrace();
        }



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

/////
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
                    //对用户注册输入的信息进行验证，全部符合要求才能通过
                    if (username.getText().length()<3) {
                        Toast.makeText(UserUpdateInfo.this,"请输入超过3位帐号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(rs.getRow()==0)
                    {
                        Toast.makeText(UserUpdateInfo.this,"用户不存在",Toast.LENGTH_SHORT).show();
                    }
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
                Toast.makeText(UserUpdateInfo.this, "除用户名外信息修改成功", Toast.LENGTH_LONG).show();
                ActivityCollector.finishAll();
            }
        });

    }
}
