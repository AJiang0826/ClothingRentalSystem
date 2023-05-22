package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;

/**
 * 查询信息页面
 */
public class admin_select_message extends AppCompatActivity {
private ImageButton back_bt,clothes_info,borrow_bt,pay_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_message);
        inti();//界面初始化；
    }

    private void inti() {
        /*back_bt=(ImageButton)findViewById(R.id.sel_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this,admin_content.class);
                startActivity(intent);
            }
        });*/
        clothes_info=(ImageButton)findViewById(R.id.ad_select_clothesinfo);
        clothes_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this, admin_select_clothesinfo.class);
                startActivity(intent);
            }
        });
        //查询借书信息
        borrow_bt=(ImageButton)findViewById(R.id.ad_select_brrow);
        borrow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this,admin_borrow_info.class);
                startActivity(intent);
            }
        });
        //查询还书信息
        pay_bt=(ImageButton)findViewById(R.id.ad_manager_people);
        pay_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this,admin_pay_info.class);
                startActivity(intent);
            }
        });
    }
}
