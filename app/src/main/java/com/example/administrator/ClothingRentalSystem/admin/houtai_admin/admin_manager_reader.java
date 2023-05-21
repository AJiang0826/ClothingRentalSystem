package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;

/**
 * 管理读者分式界面【第二层】
 */

public class admin_manager_reader extends AppCompatActivity {
    private ImageButton back_tn, AddUser, EditUser, DeleteUser, SelectUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_reader);
        inti();//界面初始化
    }

    private void inti() {
        //返回按钮的事件监听
        /*back_tn = (ImageButton) findViewById(R.id.ManUser_Back);
        back_tn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_reader.this, admin_content.class);
                startActivity(intent);
            }
        });*/
        //查找读者按钮的事件监听
        SelectUser = (ImageButton) findViewById(R.id.ad_SelectUser);
        SelectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_reader.this, select_user_admin.class);
                startActivity(intent);
            }
        });
        //添加读者按钮的事件监听
        AddUser = (ImageButton) findViewById(R.id.ad_AddUser);
        AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_reader.this, admin_add_reader.class);
                startActivity(intent);
            }
        });
        //编辑读者按钮的事件监听
        EditUser = (ImageButton) findViewById(R.id.ad_EditUser);
        EditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_reader.this, admin_editer_user.class);
                startActivity(intent);
            }
        });
        //删除读者按钮的事件监听
        DeleteUser = (ImageButton) findViewById(R.id.ad_DeleteUser);
        DeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_reader.this, admin_delete_user.class);
                startActivity(intent);
            }
        });
    }
}
