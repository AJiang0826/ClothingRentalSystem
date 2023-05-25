package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;

/*
* 衣物管理分式界面【第二层】
* 包含采购衣物和查找衣物
 * 功能：1.点击按钮进入添加衣物信息信息界面
 *      2.点击按钮进入查找衣物信息界面
 * 变量：1.addClothes, searchClothes采购衣物、查找衣物图像按钮
* */
public class admin_manager_clothes extends BaseActivity {
    private ImageButton addClothes, searchClothes;//采购衣物、查找衣物图像按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_clothes);
        init();//界面初始化
    }

    private void init() {
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
