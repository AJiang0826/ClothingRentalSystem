package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
/**
 * 该界面用于显示数据库中已经归还衣服的衣服信息
 *
 */
public class admin_pay_info extends AppCompatActivity {
    private ListView ad_pay;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pay_info);
        ad_pay = (ListView) findViewById(R.id.Add_Show_Pay);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql = "select * from clothes_lease where flage=1";//查询整张租借表，flage=1，表示衣服已经归还
        System.out.println("sql=" + sql);
        //以下开始数据库操作，使用线程，插入用户
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结果
                    rs = DBUtils.getSelectResultSet(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //该线程执行完毕-1
                    countDownLatch.countDown();
                }
            }
        }).start();
        //等待线程插入完结果，把结果集转换成一定格式，并呈现在页面上
        try {
            countDownLatch.await();//阻塞等待线程执行完毕
            //根据用户查询自己的租借信息
            String[] names1 = {"id", "user_name", "clothes_id", "clothes_size", "clothes_borrow_data"};//建立字段名结果集
            String[] names2 = {"NumberId", "UserName", "ClothesId", "ClothesSize", "PayDate"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1, names2, rs);//调用ItemUtils获取结果集
            System.out.println("list=" + data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_pay_info.this, data, R.layout.ad_pay_item,
                    new String[]{"NumberId", "UserName", "ClothesId", "ClothesSize", "PayDate"},
                    new int[]{R.id.NumberId, R.id.UserName, R.id.ClothesId, R.id.ClothesSize, R.id.PayDate});//后两个String[] int[]数组都是borrow_item中的id
            ad_pay.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
