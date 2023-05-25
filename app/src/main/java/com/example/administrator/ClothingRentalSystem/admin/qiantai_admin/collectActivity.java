package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
/*
个人收藏表
功能：用于展示登录用户对服装信息收藏的界面以及数据展示
     服装收藏信息展示如下信息：用户名，服装编号 服装名称 服装尺码 收藏日期
     用户可单击收藏的服装进行取消收藏的操作。
 */
public class collectActivity extends AppCompatActivity {
    private ListView listView;
    private String sql;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        listView = (ListView) findViewById(R.id.show_collect);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql="select user_name,clothes_id,clothes_name,clothes_size,collect_date from user_collect where user_name='"+MainActivity.getStrUserName()+"' "; //查询整张衣服信息表
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
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View curr = adapterView.getChildAt((int)l);
                TextView id = curr.findViewById(R.id.C_clothes_id);
                //读取id
                String strId=id.getText().toString();
                sql="DELETE FROM  user_collect where clothes_id="+strId;
                countDownLatch = new CountDownLatch(1);//设定计时器
                //以下开始数据库操作，使用线程，修改flage，表示已取消收藏
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //成功创建普通用户
                            rows=DBUtils.getUpdateRows(sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //该线程执行完毕-1
                            countDownLatch.countDown();
                        }
                    }
                }).start();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(rows>0)
                    Toast.makeText(collectActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(collectActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(collectActivity.this, collectActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
