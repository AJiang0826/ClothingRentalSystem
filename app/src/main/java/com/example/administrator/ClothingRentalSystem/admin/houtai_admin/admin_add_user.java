package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.MD5Utils;

/**
 * 管理员添加用户
 * 功能：1.添加用户信息
 *      2.验证用户名是否存在，若存在则提示用户名存在
 *      3.重置文本框内容
 * 变量：1. user_ed, username ,pwd_ed, phone, sex分别对应表单信息
 *      2. 确认添加按钮adduser
 */
public class admin_add_user extends BaseActivity {
    private EditText user_ed, username ,pwd_ed, phone, sex;
    private Button adduser,cz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);
        init();//初始化界面
    }

    private void init() {
        //获取文本框信息
        user_ed = (EditText) findViewById(R.id.r_name);
        pwd_ed = (EditText) findViewById(R.id.r_password);
        username = (EditText)findViewById(R.id.user_name);
        phone = (EditText)findViewById(R.id.r_phone);
        sex = (EditText)findViewById(R.id.r_sex);
        adduser = (Button) findViewById(R.id.r_register);
        //添加用户--按钮的事件监听
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                String uname = username.getText().toString();
                String phonenum = phone.getText().toString();
                String usersex = sex.getText().toString();
                String md5Psw = MD5Utils.md5(strpwd);//把密码用MD5加密---MD5信息摘要算法（是不可逆的，只能加密，不能解密）
                //验证用户名是否存在
                databaseHelp help = new databaseHelp(getApplicationContext());
                SQLiteDatabase db = help.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user", struser);
                values.put("name", uname);
                values.put("password", md5Psw);
                values.put("sex", usersex);
                values.put("phone", phonenum);
                Cursor cursor = db.query("admin", null, null, null, null, null, null);

                //判断用户名是否存在
                if (cursor.moveToFirst()) {
                    do {
                        String username = cursor.getString(cursor.getColumnIndex("user"));
                        if (username.equals(user_ed.getText().toString())) {
                            Toast.makeText(admin_add_user.this, "用户名已存在", Toast.LENGTH_LONG).show();
                            ((EditText) findViewById(R.id.r_name)).setText("");
                            return;
                        }

                    } while (cursor.moveToNext());

                }
                cursor.close();
                help.insert(values);
                Toast.makeText(admin_add_user.this, "用户添加成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(admin_add_user.this, admin_manager_user.class);
                startActivity(intent);
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
