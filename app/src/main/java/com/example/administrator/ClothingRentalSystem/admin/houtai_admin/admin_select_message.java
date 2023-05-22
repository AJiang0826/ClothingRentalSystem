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
private ImageButton Clothes_Info,Borrow_Bt,Pay_Bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_message);
        inti();//界面初始化；
    }

    private void inti() {

        Clothes_Info=(ImageButton)findViewById(R.id.Add_Select_ClothesInfo);
        Clothes_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this, admin_select_clothesinfo.class);
                startActivity(intent);
            }
        });
        //查询租借衣服信息界面
        Borrow_Bt=(ImageButton)findViewById(R.id.Add_Select_Brrow);
        Borrow_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_select_message.this,admin_borrow_info.class);
                startActivity(intent);
            }
        });
        //查询还衣信息界面
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
