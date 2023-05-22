package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;

/*
* 衣物管理分式界面【第二层】
* */
public class admin_manager_clothes extends BaseActivity {
    private ImageButton back_bt, addClothes, editClothes, searchClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_clothes);
        init();//界面出售化
    }

    private void init() {
        //回退--初始后台管理
        /*back_bt = (ImageButton) findViewById(R.id.manClothes_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_content.class);
                startActivity(intent);
            }
        });*/
        //添加图书
        addClothes = (ImageButton) findViewById(R.id.ad_add);
        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_clothes.this, admin_add_clothes.class);
                startActivity(intent);
            }
        });
        //查询图书
        searchClothes = (ImageButton) findViewById(R.id.ad_search);
        searchClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_clothes.this, admin_search_clothes.class);
                startActivity(intent);
            }
        });
    }
}
