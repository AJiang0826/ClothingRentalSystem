package com.example.administrator.ClothingRentalSystem.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 本类是用注册普通用户算法类
 * 通过调用DBUtils完成数据库的增删改查
 **/
public class registerActivity extends BaseActivity {
    private EditText username, password,confirmPassword ,name, sex, phone, identity;
    private Button resetting, confirm;
    private String strUserName,strPwd,strConfirmPwd,strName,strPhone,strSex,sql;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        countDownLatch = new CountDownLatch(1);
        init();//界面初始化
    }

    private void init() {
        username = (EditText) findViewById(R.id.RegisterUserName);
        password = (EditText) findViewById(R.id.RegisterPassword);
        confirmPassword = (EditText) findViewById(R.id.RegisterPasswordConfirm);
        name = (EditText)findViewById(R.id.RegisterName);
        phone = (EditText)findViewById(R.id.RegisterPhone);
        sex = (EditText)findViewById(R.id.RegisterSex);
        //重置按钮的事件监听
        resetting = (Button) findViewById(R.id.RegisterResetting);
        resetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) findViewById(R.id.RegisterUserName)).setText("");
                ((EditText) findViewById(R.id.RegisterPasswordConfirm)).setText("");
                ((EditText) findViewById(R.id.RegisterPassword)).setText("");
                ((EditText) findViewById(R.id.RegisterSex)).setText("");
                ((EditText) findViewById(R.id.RegisterPhone)).setText("");
                ((EditText) findViewById(R.id.RegisterName)).setText("");
            }
        });
        //注册按钮de事件监听
        confirm = (Button) findViewById(R.id.RegisterConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUserName = username.getText().toString();
                strPwd = password.getText().toString();
                strConfirmPwd = confirmPassword.getText().toString();
                strName = name.getText().toString();
                strPhone = phone.getText().toString();
                strSex = sex.getText().toString();
                //首先验证两次密码输入是否正确，若不正确则密码重新输入
                if (!strPwd.equals(strConfirmPwd))
                {
                    Toast.makeText(registerActivity.this, "密码两次输入不相同！", Toast.LENGTH_LONG).show();
                    ((EditText) findViewById(R.id.RegisterPassword)).setText("");
                    ((EditText) findViewById(R.id.RegisterPasswordConfirm)).setText("");
                    ((EditText) findViewById(R.id.RegisterPassword)).requestFocus();
                    return;
                }
                sql="insert into user(username,password,name,phone,sex,identity) values('" +
                        strUserName+"','"+strPwd+"','"+strName+"','"+strPhone+"','"+strSex+"',0);";
                //以下开始数据库操作，使用线程，查询用户是否已经存在
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rs = DBUtils.getSelectResultSet("Select * from user where username='"+strUserName+"'");
                            System.out.println("strUserName="+strUserName);
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
//                        System.out.println("rs.getRow="+rs.getRow());
                        Toast.makeText(registerActivity.this, "该用户名已存在！", Toast.LENGTH_LONG).show();
                        ((EditText) findViewById(R.id.RegisterUserName)).setText("");
                        ((EditText) findViewById(R.id.RegisterUserName)).requestFocus();
                        return;
                    }
                    //对用户注册输入的信息进行验证，全部符合要求才能通过
                    if (username.getText().length()<3) {
                        Toast.makeText(registerActivity.this,"请输入超过3位帐号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(phone.getText().length()!=11){
                        Toast.makeText(registerActivity.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(strSex.equals("")||strName.equals("")){
                        Toast.makeText(registerActivity.this,"请输入完整内容",Toast.LENGTH_SHORT).show();
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
                    if (rows>0){
                        Toast.makeText(registerActivity.this,"用户："+strUserName+"注册成功！",Toast.LENGTH_SHORT).show();
                        //用户注册成功，就跳转到登录页面
                        Intent intent = new Intent(registerActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(registerActivity.this,"注册失败，请联系管理员！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
