package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

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
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

/**
 * 修改读者信息的页面
 */

public class ReaderUpdateInfo extends BaseActivity {
    private EditText user_ed, username ,pwd_ed,  birthday, phone, sex;
    private Button update_bt;
    String uname2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_update_info);
        inut();//初始化界面
    }

    private void inut() {
        //将id返回的记录查询在edittext
        user_ed = (EditText) findViewById(R.id.u_name);
        pwd_ed = (EditText) findViewById(R.id.u_password);
        username = findViewById(R.id.user_name);
        birthday = findViewById(R.id.u_birthday);
        phone = findViewById(R.id.u_phone);
        sex = findViewById(R.id.u_sex);
        update_bt = (Button) findViewById(R.id.r_register);
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
        uname2 = perf.getString("users", "");//获得当前用户名称
        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.queryname(uname2);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
//            不能修改用户名
            user_ed.setText(cursor.getString(cursor.getColumnIndex("user")));
            pwd_ed.setText(cursor.getString(cursor.getColumnIndex("password")));
            username.setText(cursor.getString(cursor.getColumnIndex("name")));
            birthday.setText(cursor.getString(cursor.getColumnIndex("birthday")));
            phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
            sex.setText(cursor.getString(cursor.getColumnIndex("sex")));
        }
        //修改按钮的事件监听
        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String struser = user_ed.getText().toString();
                String strpwd = pwd_ed.getText().toString();
                String uname = username.getText().toString();
                String birth = birthday.getText().toString();
                String phonenum = phone.getText().toString();
                String usersex = sex.getText().toString();
                SQLiteDatabase db = help.getWritableDatabase();
                ContentValues values = new ContentValues();
//                values.put("user", struser);
                values.put("name", uname);
                values.put("password", strpwd);
                values.put("sex", usersex);
                values.put("phone", phonenum);
                values.put("birthday", birth);
                db.update("admin", values, "user=?", new String[]{String.valueOf(uname2)});
                Toast.makeText(ReaderUpdateInfo.this, "除用户名外信息修改成功", Toast.LENGTH_LONG).show();
            }
        });

    }
}
