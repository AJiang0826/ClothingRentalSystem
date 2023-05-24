package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;
/*
个人借书表
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class collectActivity extends AppCompatActivity {
    private ListView listView;
    private String bookid, bookname, bookauthor, booktime,Rorname;
    private String user_name, clothes_id, clothes_name,clothes_size,collect_date;
    private String sql;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;



    static{
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始连接数据库……");
                // new DBUtils("192.168.43.149:3306","clothes_rental_system","Android","123456");
                new DBUtils("192.168.43.71:3306","clothes_rental_system","root","123456");
                System.out.println("查看数据库连接是否成立："+ (DBUtils.conn!=null));
            }
        }
        ).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        listView = (ListView) findViewById(R.id.show_collect);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql="select user_name,clothes_id,clothes_name,clothes_size,collect_date from user_collect";//查询整张衣服信息表
        System.out.println("sql="+sql);
        //以下开始数据库操作，使用线程，插入用户
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结果
                    rs= DBUtils.getSelectResultSet(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //该线程执行完毕-1
                    countDownLatch.countDown();
                }
            }
        }).start();
        //等待线程插入完结果，把结果集转换成一定格式，并呈现在页面上
        try {
            countDownLatch.await();//阻塞等待线程执行完毕
            //根据用户查询自己的收藏信息
            String[] names1={"user_name","clothes_id","clothes_name","clothes_size","collect_date"};//建立字段名结果集
            String[] names2={"C_user_name","C_clothes_id","C_clothes_name","C_clothes_size","C_collect_date"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    collectActivity.this, data, R.layout.collect_item,
                    new String[]{"C_user_name","C_clothes_id","C_clothes_name","C_clothes_size","C_collect_date"},
                    new int[]{R.id.C_user_name, R.id.C_clothes_id, R.id.C_clothes_name, R.id.C_clothes_size, R.id.C_collect_date});//后两个String[] int[]数组都是borrow_item中的id
            System.out.println("读取到数据");
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

























        //通过id查询图书表里的所有信息，用bundle进行数据交互

        //取消收藏
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        }); */


    }
}
