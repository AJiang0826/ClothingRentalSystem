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

public class admin_content extends AppCompatActivity {
    private long mExitTime;
    private ImageButton selct_bt, manReader_bt, manBook_bt;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_content);
        init();//界面初始化

    }

    private void init() {
        //查询信息
        selct_bt=(ImageButton)findViewById(R.id.ad_select);
        selct_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_content.this,admin_select_message.class);
                startActivity(intent);
            }
        });
        //管理图书
        manBook_bt=(ImageButton)findViewById(R.id.ad_manager_book);
        manBook_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_content.this,admin_manager_book.class);
                startActivity(intent);
            }
        });
        //管理读者
        manReader_bt=(ImageButton)findViewById(R.id.ad_manager_reader);
        manReader_bt.setOnClickListener(new View.OnClickListener() {
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
