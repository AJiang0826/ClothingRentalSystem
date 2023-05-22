package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
/**
 * 本类主要是跳转页面使用
 * 通过选择不通内容，及点击不同 > 符号跳转页面
 * 不做其他算法处理
 **/
public class admin_content extends AppCompatActivity {
    private long mExitTime;
    private ImageButton SelectClothes, ManagerClothes, ManagerUser;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_content);
        init();//界面初始化

    }

    private void init() {
        //查询服装信息
        SelectClothes=(ImageButton)findViewById(R.id.SelectClothes);
        SelectClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_content.this,admin_select_message.class);
                startActivity(intent);
            }
        });
        //管理服装信息
        ManagerClothes=(ImageButton)findViewById(R.id.ManagerClothes);
        ManagerClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_content.this, admin_manager_clothes.class);
                startActivity(intent);
            }
        });
        //管理用户信息
        ManagerUser=(ImageButton)findViewById(R.id.ManagerUser);
        ManagerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_content.this, admin_manager_user.class);
                startActivity(intent);
            }
        });
    }
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(admin_content.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {

            finish();
            System.exit(0);
        }
    }
}
