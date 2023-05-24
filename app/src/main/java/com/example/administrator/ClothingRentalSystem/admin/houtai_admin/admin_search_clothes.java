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
                Intent  intent = new Intent(admin_search_clothes.this, admin_search_clothesinfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", search_name.getText().toString());//将name值传给admin_search_clothesinfo
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}