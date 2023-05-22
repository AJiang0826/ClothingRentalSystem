package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;

/**
 * 管理用户
 */

public class admin_manager_user extends AppCompatActivity {
    private ImageButton back_tn, adduser, editreader, deletereader, selectReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_reader);
        //inti();//界面初始化
    }

    private void inti() {
        //返回按钮的事件监听
       /* back_tn = (ImageButton) findViewById(R.id.manClothes_bk);
        back_tn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_content.class);
                startActivity(intent);
            }
        });*/
        //查找读者按钮的事件监听
        selectReader = (ImageButton) findViewById(R.id.ad_SelectUser);
        selectReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, select_user_admin.class);
                startActivity(intent);
            }
        });
        //添加读者按钮的事件监听
        adduser = (ImageButton) findViewById(R.id.ad_AddUser);
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_add_user.class);
                startActivity(intent);
            }
        });
        //编辑读者按钮的事件监听
        editreader = (ImageButton) findViewById(R.id.ad_EditUser);
        editreader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_editer_user.class);
                startActivity(intent);
            }
        });
        //删除读者按钮的事件监听
        deletereader = (ImageButton) findViewById(R.id.ad_DeleteUser);
        deletereader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_user.this, admin_delete_user.class);
                startActivity(intent);
            }
        });
    }
}
