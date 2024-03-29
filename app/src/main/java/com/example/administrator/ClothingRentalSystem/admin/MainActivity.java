package com.example.administrator.ClothingRentalSystem.admin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.contentActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.DonwloadSaveImg;
import com.example.administrator.ClothingRentalSystem.admin.utils.MD5Utils;

import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

import java.sql.SQLException;

/**
 * 这是主类，打开初始页面
 * 并且开启统一全局的数据库连接
 * 初始界面功能：用户输入账户密码进行登录 可勾选记住密码 播放背景音乐等辅助功能
 * 用户和管理员初次登录可进行用户信息的注册，向数据库存储个人信息
 **/
public class MainActivity extends AppCompatActivity {

    private EditText user_ed, pwd_ed;
    private Button login_bt, register_bt;
    private Button im_bt;
    private CheckBox rember, auto_login;
    private static String strUserName;
    private String sql;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;

//11

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
                new DBUtils("192.168.43.149:3306","clothes_rental_system","Android","123456");
                System.out.println("查看数据库连接是否成立："+ (DBUtils.conn!=null));
                for (int i=1;i<=9;i++){
                    String Path="http://192.168.43.149/ClothesRentalSystem/"+i+".png";
                    new DonwloadSaveImg().donwloadImg(Path,i);
                }
            }
        }
        ).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countDownLatch = new CountDownLatch(1);
        init();//界面初始化
        final Switch aSwitch = (Switch) findViewById(R.id.musicswitch);
        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制播放背景音乐
                if (b) {
                    Intent intent=new Intent(MainActivity.this,MyService.class);
                    startService(intent);
                }
                else
                {
                    Intent intent=new Intent(MainActivity.this,MyService.class);
                    stopService(intent);
                }

            }
        });
    }

    private void init() {

        user_ed = (EditText) findViewById(R.id.name);
        pwd_ed = (EditText) findViewById(R.id.password);
        //复选框的监听事件
        rember = (CheckBox) findViewById(R.id.rmber_pwd);//记住密码
        auto_login = (CheckBox) findViewById(R.id.auto_login);//自动登录
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String Rusername = sp.getString("users", "");
        final String Rpassword = sp.getString("passwords", "");
        final boolean choseRemember = sp.getBoolean("remember", false);
        boolean choseAutoLogin = sp.getBoolean("autologin", false);
        //      Toast.makeText(this, name, Toast.LENGTH_SHORT).show();


        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {
            user_ed.setText(Rusername);
            pwd_ed.setText(Rpassword);
            rember.setChecked(true);
        }
        //如果上次登录选了自动登录，那进入登录页面也自动勾选自动登录
        if (choseAutoLogin) {
            auto_login.setChecked(true);
        }

        //注册按钮的事件监听
        register_bt = (Button) findViewById(R.id.register);
        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });
        //切换按钮的事件监听
        im_bt = (Button) findViewById(R.id.admin);
        im_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
        //登录按钮的事件监听
        login_bt = (Button) findViewById(R.id.login);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                strUserName=struser;
                String strpwd = pwd_ed.getText().toString();
                if ( choseRemember==false) {
                    //如果没有勾选记住密码，对当前用户输入的密码进行MD5加密再进行比对判断, MD5Utils.md5( ) 进行加密
                    md5Psw= MD5Utils.md5(strpwd);
                }else {
                    //勾选了记住密码
                    md5Psw= strpwd;
                }

                sql="select * from user where  username='"+struser+"' and password="+strpwd;
                //sql="Select * from user where username='"+struser+"'";
                //以下开始数据库操作，使用线程，查询用户是否存在
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rs = DBUtils.getSelectResultSet(sql);
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

                        Toast.makeText(MainActivity.this,"账号密码输入正确！",Toast.LENGTH_SHORT).show();
                        //用户密码输入正确，就跳转到显示服装列表页面
                        Intent intent = new Intent(MainActivity.this, contentActivity.class);
                        startActivity(intent);
                        //增加一个Notification通知信息。当用户名、密码正确时，不但做界面跳转，还要发出一条状态栏消息提
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                        String channelId = createNotificationChannel("my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH);
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this, channelId)
                                .setContentTitle("通知")
                                .setContentText("hello，"+"欢迎"+struser+"来到服装租借系统~")
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                        notificationManager.notify(100, notification.build());

                         /*
                            将用户名存储到sharedpreferences中
                            获取用户名和密码，方便在记住密码时使用
                             */
                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                        editor.putString("users", struser);
                        editor.putString("passwords", strpwd);
                        //是否记住密码
                        if (rember.isChecked()) {
                            editor.putBoolean("remember", true);
                        } else {
                            editor.putBoolean("remember", false);
                        }

                        //是否自动登录
                        if (auto_login.isChecked()) {
                            editor.putBoolean("autologin", true);
                            Intent intent1 = new Intent(MainActivity.this, contentActivity.class);
                            startActivity(intent1);
                        } else {
                            editor.putBoolean("autologin", false);
                        }
                        editor.apply();
                        return;
                    }
                    else
                        Toast.makeText(MainActivity.this,"账号密码输入不正确",Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });//登录监听完毕

    }
    public static String getStrUserName()
    {
        return strUserName;
    }
 private String createNotificationChannel(String channelID, String channelNAME, int level) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }

}
