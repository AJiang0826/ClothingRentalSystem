package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.person_borrow;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
/**
 * 该界面用于显示数据库中被借出衣服的衣服信息，
 *将客服已经借出的衣服进行查看
 * 该界面所显示的衣服都是近一个月借出去的衣服
 */
public class admin_borrow_info extends AppCompatActivity {
    private ListView Add_Borrow;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_borrow_info);
        Add_Borrow = (ListView) findViewById(R.id.Add_Show_Borrow);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql = "SELECT * FROM  clothes_lease AS t WHERE date(t.clothes_borrow_data) >= DATE_SUB(CURDATE(),INTERVAL 1 MONTH) and flage=0";//查询flage=0，表示衣服已经借出去了
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
            String[] names1 = {"id", "user_name", "clothes_id", "clothes_size", "clothes_borrow_data"};//建立数据库字段名结果集
            String[] names2 = {"NumberId", "UserName", "ClothesId", "ClothesSize", "BorrowDate"};//建立xml组件的id字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1, names2, rs);//调用ItemUtils获取结果集
            System.out.println("list=" + data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_borrow_info.this, data, R.layout.ad_borrow_item,
                    new String[]{"NumberId", "UserName", "ClothesId", "ClothesSize", "BorrowDate"},
                    new int[]{R.id.NumberId, R.id.UserName, R.id.ClothesId, R.id.ClothesSize, R.id.BorrowDate});//后两个String[] int[]数组都是borrow_item中的id
            Add_Borrow.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
