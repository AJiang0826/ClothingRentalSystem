package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;
/*
个人借书表
 */

import android.content.ContentValues;
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

public class person_borrow extends AppCompatActivity {
    private ListView listView;
    private String bookid, bookname, bookauthor, booktime,Rorname;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_borrow);
        listView = (ListView) findViewById(R.id.show_borrow);
        final databaseHelp help = new databaseHelp(getApplicationContext());
        SharedPreferences perf = getSharedPreferences("data", MODE_PRIVATE);

        username = perf.getString("users", "");//获得当前用户名称
        //根据用户查询自己的借阅信息
        List<Map<String, Object>> data = help.queryborrow(username);
        SimpleAdapter adapter = new SimpleAdapter(
                person_borrow.this, data, R.layout.borrow_item,
                new String[]{"Borname", "Bookid", "bookname",
                        "bookauthor", "nowtime"},
                new int[]{R.id.Borname, R.id.Bbookid,
                        R.id.Bbookname, R.id.Bbookauthor,
                        R.id.Bnowtimae});
        listView.setAdapter(adapter);
        //通过id查询图书表里的所有信息，用bundle进行数据交互

        //进行还书,跳转到信息界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                View curr = adapterView.getChildAt((int)l);
                TextView tv = curr.findViewById(R.id.Bbookname);
                TextView bookname = curr.findViewById(R.id.Bbookname);
                TextView bookID = curr.findViewById(R.id.Bbookid);
                TextView author = curr.findViewById(R.id.Bbookauthor);
                TextView time = curr.findViewById(R.id.Bnowtimae);
                help.delborrowbyname(tv.getText().toString());
                Toast.makeText(person_borrow.this, "还书成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(person_borrow.this, person_borrow.class);
//                startActivity(intent);
                ContentValues values=new ContentValues();
                values.put("Bookid",bookID.getText().toString());
                values.put("bookname",bookname.getText().toString());
                values.put("bookauthor",author.getText().toString());
                values.put("Borname",username);
                values.put("nowtime",time.getText().toString());
                help.insertpay(values);
                finish();


            }
        });


    }
}
