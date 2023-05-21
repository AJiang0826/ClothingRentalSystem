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
 * 管理员添加读者
 */
public class admin_add_reader extends BaseActivity {
    private EditText user_ed, username ,pwd_ed,  birthday, phone, sex;
    private Button addreader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_reader);
        init();//出实话界面
    }

    private void init() {
        user_ed = (EditText) findViewById(R.id.r_name);
        pwd_ed = (EditText) findViewById(R.id.r_password);
        username = findViewById(R.id.user_name);
        birthday = findViewById(R.id.r_birthday);
        phone = findViewById(R.id.r_phone);
        sex = findViewById(R.id.r_sex);
        addreader = (Button) findViewById(R.id.r_register);
        //添加读者按钮的事件监听
        addreader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                String uname = username.getText().toString();
                String birth = birthday.getText().toString();
                String phonenum = phone.getText().toString();
                String usersex = sex.getText().toString();
                String md5Psw = MD5Utils.md5(strpwd);//把密码用MD5加密
                //验证用户名是否存在
                databaseHelp help = new databaseHelp(getApplicationContext());
                SQLiteDatabase db = help.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user", struser);
                values.put("name", uname);
                values.put("password", md5Psw);
                values.put("sex", usersex);
                values.put("phone", phonenum);
                values.put("birthday", birth);
                Cursor cursor = db.query("admin", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String username = cursor.getString(cursor.getColumnIndex("user"));
                        if (username.equals(user_ed.getText().toString())) {
                            Toast.makeText(admin_add_reader.this, "用户名已存在", Toast.LENGTH_LONG).show();
                            ((EditText) findViewById(R.id.r_name)).setText("");
                            return;
                        }

                    } while (cursor.moveToNext());

                }
                cursor.close();
                help.insert(values);
                Toast.makeText(admin_add_reader.this, "用户添加成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(admin_add_reader.this, admin_manager_reader.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }
        });
    }
}
