package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;
/*
个人借书表
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

import java.util.List;
import java.util.Map;

public class collectActivity extends AppCompatActivity {
    private ListView listView;
    private String bookid, bookname, bookauthor, booktime,Rorname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        listView = (ListView) findViewById(R.id.show_collect);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);

        String username = perf.getString("users", "");//获得当前用户名称
        //根据用户查询自己的收藏信息
        List<Map<String, Object>> data = help.queryuser(username);
        final SimpleAdapter adapter = new SimpleAdapter(
                collectActivity.this, data, R.layout.collect_item,
                new String[]{"Borname", "Bookid", "bookname",
                        "bookauthor", "nowtime"},
                new int[]{R.id.Borname, R.id.Bbookid,
                        R.id.Bbookname, R.id.Bbookauthor,
                        R.id.Bnowtimae});
        listView.setAdapter(adapter);
        //通过id查询图书表里的所有信息，用bundle进行数据交互

        //取消收藏
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View curr = adapterView.getChildAt((int)l);
                TextView name = curr.findViewById(R.id.Bbookname);
                help.delcollect(name.getText().toString());
                Toast.makeText(collectActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                listView.setAdapter(adapter);
                Intent intent = new Intent(collectActivity.this, collectActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
