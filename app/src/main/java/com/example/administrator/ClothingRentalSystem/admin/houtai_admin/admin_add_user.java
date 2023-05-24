package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员添加用户
 * 功能：1.添加用户信息（对输入的信息进行验证，用户名不重复且其他全部符合要求才能通过）
 *      2.重置文本框内容
 *      3.返回上一界面
 */
public class admin_add_user extends BaseActivity {
    private EditText user_ed, username ,pwd_ed, phone, sex;
    private Button adduser,cz;//确认添加按钮、重置按钮
    private String struser,strpwd,uname,usersex,phonenum,sql;
    private CountDownLatch countDownLatch;//创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private ResultSet rs;
    private ImageButton back_bt;//返回图片按钮
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        init();//初始化界面
    }

    private void init() {

        //返回--图片按钮监听
        back_bt = (ImageButton) findViewById(R.id.edituser_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_add_user.this, admin_manager_user.class);
                startActivity(intent);
            }
        });
        //获取文本框输入的信息
        user_ed = (EditText) findViewById(R.id.r_name);
        pwd_ed = (EditText) findViewById(R.id.r_password);
        username = (EditText)findViewById(R.id.user_name);
        phone = (EditText)findViewById(R.id.r_phone);
        sex = (EditText)findViewById(R.id.r_sex);
        adduser = (Button) findViewById(R.id.r_register);

        //添加用户按钮--事件监听
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                struser = user_ed.getText().toString();
                strpwd = pwd_ed.getText().toString();
                uname = username.getText().toString();
                usersex = sex.getText().toString();
                phonenum = phone.getText().toString();

                sql="insert into user(username,password,name,sex,phone,identity) values('" +
                        struser+"','"+strpwd+"','"+uname+"','"+usersex+"','"+phonenum+"',0);";

                //以下开始数据库操作，使用线程，查询用户是否已经存在
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rs = DBUtils.getSelectResultSet("Select * from user where username='"+struser+"'");
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
                    //判断用户名是否存在
                    if(rs.getRow()!=0){
                        System.out.println("rs.getRow="+rs.getRow());
                        Toast.makeText(admin_add_user.this, "该用户名已存在！", Toast.LENGTH_LONG).show();
                        ((EditText) findViewById(R.id.RegisterUserName)).setText("");
                        ((EditText) findViewById(R.id.RegisterUserName)).requestFocus();
                        return;
                    }
                    //对输入的信息进行验证，全部符合要求才能通过
                    if (user_ed.getText().length()<3) {
                        Toast.makeText(admin_add_user.this,"请输入账号不可小于3位",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(phone.getText().length()!=11){
                        Toast.makeText(admin_add_user.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(usersex.equals("")||struser.equals("")){
                        Toast.makeText(admin_add_user.this,"请输入完整内容",Toast.LENGTH_SHORT).show();
                        return;
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
                        Toast.makeText(admin_add_user.this,"用户："+struser+"添加成功！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ActivityCollector.finishAll();
            }

        });

        // 获取重置按钮并且添加事件
        cz=(Button) findViewById(R.id.r_register_resetting);
        cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_ed.setText("");
                username.setText("");
                pwd_ed.setText("");
                sex.setText("");
                phone.setText("");
            }
        });

    }
}
