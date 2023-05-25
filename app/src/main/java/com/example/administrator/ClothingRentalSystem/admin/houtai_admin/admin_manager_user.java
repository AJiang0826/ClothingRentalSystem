package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;

/**
 * 管理读者分式界面【第二层】
 * 功能：1.点击按钮进入添加用户信息界面
 *      2.点击按钮进入删除用户信息界面
 *      3.点击按钮进入修改用户信息界面
 *      3.点击按钮进入查询用户信息界面
 * 变量：1.AddUser, EditUser, DeleteUser, SelectUser查找、添加、编辑、删除用户
 */

public class admin_manager_user extends AppCompatActivity {
    private ImageButton AddUser, EditUser, DeleteUser, SelectUser;//查找、添加、编辑、删除用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_user);
        inti();//界面初始化
    }

    private void inti() {
        //查找用户按钮的事件监听
        SelectUser = (ImageButton) findViewById(R.id.ad_SelectUser);
        SelectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, select_user_admin.class);
                startActivity(intent);
            }
        });
        //添加用户按钮的事件监听
        AddUser = (ImageButton) findViewById(R.id.ad_AddUser);
        AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_add_user.class);
                startActivity(intent);
            }
        });
        //编辑用户按钮的事件监听
        EditUser = (ImageButton) findViewById(R.id.ad_EditUser);
        EditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_editer_user.class);
                startActivity(intent);
            }
        });
        //删除用户按钮的事件监听
        DeleteUser = (ImageButton) findViewById(R.id.ad_DeleteUser);
        DeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_delete_user.class);
                startActivity(intent);
            }
        });
    }
}
