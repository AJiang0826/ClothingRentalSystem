package com.example.administrator.ClothingRentalSystem.admin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.contentActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.MD5Utils;

/**
 * 这是主类，打开初始页面
 * 并且开启统一全局的数据库连接
 **/
public class MainActivity extends BaseActivity {
    private EditText user_ed, pwd_ed;
    private Button login_bt, register_bt;
    private Button im_bt;
    private CheckBox rember, auto_login;
    private String md5Psw;

    /**
     * 以下方法是静态代码块，用来初始化数据库，并载入连接进入内存
     * 调用的是DBUtils工具类
     **/
    static{
        new Thread(new Runnable() {
            @Override
            public void run() {
                new DBUtils("192.168.43.149:3306","test","AJiang","QAQby!!!");
                System.out.println("查看数据库连接是否成立："+ (DBUtils.conn!=null));
            }
        }
        ).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                if ( choseRemember==false) {
                    //如果没有勾选记住密码，对当前用户输入的密码进行MD5加密再进行比对判断, MD5Utils.md5( ) 进行加密
                    md5Psw= MD5Utils.md5(strpwd);
                }else {
                    //勾选了记住密码
                    md5Psw= strpwd;
                }

                databaseHelp help = new databaseHelp(getApplicationContext());
                SQLiteDatabase db = help.getWritableDatabase();
                boolean login_succ = false;
                Cursor cursor = db.query("admin", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String username = cursor.getString(cursor.getColumnIndex("user"));
                        String password = cursor.getString(cursor.getColumnIndex("password"));

                        if (username.equals(struser) && password.equals(md5Psw)) {
                            login_succ=true;
                            Intent intent = new Intent(MainActivity.this, contentActivity.class);
                            startActivity(intent);
                            //增加一个Notification通知信息。当用户名、密码正确时，不但做界面跳转，还要发出一条状态栏消息提
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                            String channelId = createNotificationChannel("my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH);
                            NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this, channelId)
                                    .setContentTitle("通知")
                                    .setContentText("hello，"+"欢迎"+username+"来到服装租借系统~")
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
                            editor.putString("users", username);
                            editor.putString("passwords", password);
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

                        }


                    } while (cursor.moveToNext());

                }
                if(!login_succ){
                    Toast.makeText(MainActivity.this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                }


                cursor.close();


            }

        });

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
