package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;

/*
* 搜索衣物界面
*
 * 功能：1.搜索框输入衣服名（name）,
 *      2.输入name时查询所有衣物信息
 *      3.输入值按name查询
 * 变量：1.search搜索按钮
* */
public class admin_search_clothes extends AppCompatActivity {
    private Button search;//搜索按钮
    private EditText search_name;//输入搜索的衣服名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_clothes);

        search = findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name);
        //搜索按钮加监听
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从admin_search_clothes跳转到admin_search_clothesinfo
                Intent  intent = new Intent(admin_search_clothes.this, admin_search_clothesinfo.class);
                //两个activity之间的通讯可以通过bundle类来实现
                Bundle bundle = new Bundle();
                //bundle类中加入数据（key -value的形式）键值对
                bundle.putString("name", search_name.getText().toString());//将name值传给admin_search_clothesinfo
                //并将该bundle加入这个intent对象
                intent.putExtras(bundle);
                //要启动一个新的Activity
                startActivity(intent);
            }
        });
    }
}