package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;

/**
 * 该界面能主要功能用于实现点击各个按钮跳转到所对应的功能模块界面
 * 该界面的变量有按钮Clothes_Info跳转到查询的界面,Borrow_Bt跳转到租借界面,Pay_Bt跳转到还衣界面
 */
public class admin_select_message extends AppCompatActivity {
private ImageButton Clothes_Info,Borrow_Bt,Pay_Bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_message);
        inti();//界面初始化；
    }

    private void inti() {

        Clothes_Info=(ImageButton)findViewById(R.id.Add_Select_ClothesInfo);//获取界面的按钮
        //为按钮添加监听
        Clothes_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this, admin_select_clothesinfo.class);//实现界面跳转
                startActivity(intent);//只需要在参数列表里传一个Intent对象，指明跳转前后的页
            }
        });
        //查询租借衣服信息界面,为按钮添加监听
        Borrow_Bt=(ImageButton)findViewById(R.id.Add_Select_Brrow);
        Borrow_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this,admin_borrow_info.class);//实现界面跳转
                startActivity(intent);
            }
        });
        //查询还衣信息界面，为按钮添加监听
        Pay_Bt=(ImageButton)findViewById(R.id.Add_Manager_User);
        Pay_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this,admin_pay_info.class);
                startActivity(intent);
            }
        });
    }
}
