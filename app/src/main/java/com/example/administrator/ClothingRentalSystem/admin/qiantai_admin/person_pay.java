package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;
/*
查询我的还书
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

import java.util.List;
import java.util.Map;

public class person_pay extends AppCompatActivity {
private ListView pay_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_pay_nobody);
        pay_view=(ListView)findViewById(R.id.show_pay);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);
//
        String username = perf.getString("users", "");//获得当前用户名称
        //根据用户查询自己的还书信息
        List<Map<String, Object>> data = help.querypayuser(username);
        SimpleAdapter adapter = new SimpleAdapter(
                person_pay.this, data, R.layout.pay_item,
                new String[]{"Borname", "Bookid", "bookname",
                        "bookauthor", "nowtime"},
                new int[]{R.id.Id,R.id.CustomerName, R.id.ClothesId,
                        R.id.ClothesSize, R.id.PayTime,
                        });
        pay_view.setAdapter(adapter);
    }
}
