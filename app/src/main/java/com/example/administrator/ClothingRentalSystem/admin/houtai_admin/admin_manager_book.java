package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;

public class admin_manager_book extends BaseActivity {
    private ImageButton back_bt, addbook, editbook, searchbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_book);
        init();//界面出售化
    }

    private void init() {
        back_bt = (ImageButton) findViewById(R.id.manbook_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_content.class);
                startActivity(intent);
            }
        });
        addbook = (ImageButton) findViewById(R.id.ad_add);
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_add_book.class);
                startActivity(intent);
            }
        });
        searchbook = (ImageButton) findViewById(R.id.ad_search);
        searchbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_manager_book.this, admin_search_book.class);
                startActivity(intent);
            }
        });
    }
}
