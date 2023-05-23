package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.content.ContentValues;
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
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 本类是个人租借衣服页面算法类的实现
 * 从数据库中读取数据，并通过ItemUtils类，将结果集的内容通过一定算法转换
 * 再将数据加入ListView并呈现在界面上
 **/
public class person_borrow extends AppCompatActivity {
    private ListView listView;
    private String id,borrowName,clothesNum,clothesSize,borrowTime;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;
    int rows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_borrow);
        listView = (ListView) findViewById(R.id.show_borrow);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql="select id,user_name,clothes_id,clothes_size,clothes_borrow_data from clothes_lease where user_name='"+MainActivity.getStrUserName()+"' and flage=0;";//查询整张租借表
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
            //根据用户查询自己的租借信息
            String[] names1={"id","user_name","clothes_id","clothes_size","clothes_borrow_data"};//建立字段名结果集
            String[] names2={"Id","CustomerName","ClothesId","ClothesSize","BorrowTime"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            SimpleAdapter adapter = new SimpleAdapter(
                    person_borrow.this, data, R.layout.borrow_item,
                        new String[]{"Id", "CustomerName", "ClothesId", "ClothesSize", "BorrowTime"},
                        new int[]{R.id.Id, R.id.CustomerName, R.id.ClothesId, R.id.ClothesSize, R.id.BorrowTime});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //进行归还衣服,跳转到信息界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View curr = adapterView.getChildAt((int)l);
                //将这一行的id转换成TextView，方便读取内容
                TextView id = curr.findViewById(R.id.Id);
                //读取id
                String strId=id.getText().toString();
                sql="update clothes_lease set flage=1 where id="+strId;
                countDownLatch = new CountDownLatch(1);//设定计时器
                //以下开始数据库操作，使用线程，修改flage，表示已还书
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
                    Toast.makeText(person_borrow.this, "归还衣服成功！欢迎下次再来！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(person_borrow.this, "归还衣服失败！请通知管理员！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(person_borrow.this, person_borrow.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
