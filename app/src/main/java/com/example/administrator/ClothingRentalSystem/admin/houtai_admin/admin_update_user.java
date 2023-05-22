package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.MD5Utils;

/**
 * 修改用户信息的页面
 * 功能：修改用户信息
 * 变量：
 */

public class admin_update_user extends BaseActivity {
    private EditText user_ed, username ,pwd_ed, phone, sex;
    private Button update_bt,cz;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_user);
        inut();//初始化界面
    }

    private void inut() {
        //将id返回的记录查询在edittext
        user_ed = (EditText) findViewById(R.id.r_name);
        pwd_ed = (EditText) findViewById(R.id.r_password);
        username = findViewById(R.id.user_name);
        phone = findViewById(R.id.r_phone);
        sex = findViewById(R.id.r_sex);
        update_bt = (Button) findViewById(R.id.r_register);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.queryid(id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user_ed.setText(cursor.getString(cursor.getColumnIndex("user")));
            pwd_ed.setText(cursor.getString(cursor.getColumnIndex("password")));
            username.setText(cursor.getString(cursor.getColumnIndex("name")));
            phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
            sex.setText(cursor.getString(cursor.getColumnIndex("sex")));

        }
        //修改按钮的事件监听
        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                String uname = username.getText().toString();
                String phonenum = phone.getText().toString();
                String usersex = sex.getText().toString();
                String md5Psw = MD5Utils.md5(strpwd);//把密码用MD5加密
                SQLiteDatabase db = help.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user", struser);
                values.put("name", uname);
                values.put("password", md5Psw);
                values.put("sex", usersex);
                values.put("phone", phonenum);
                db.update("admin", values, "_id=?", new String[]{String.valueOf(id)});
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
                user_ed.setText("");
                username.setText("");
                pwd_ed.setText("");
                sex.setText("");
                phone.setText("");
            }
        });

    }
}
