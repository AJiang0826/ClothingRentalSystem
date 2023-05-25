package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员查询衣物详细信息【搜索结果】
 *
 * 功能：1.显示搜索出的衣物详情信息
 * 变量：1.listView,
 */

public class admin_search_clothesinfo extends AppCompatActivity {
    private ListView listView;
    private String sql,sql1,strSearch_Name;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_clothesinfo);
        init();//界面初始化
    }

    private void init() {
        listView = (ListView) findViewById(R.id.Select_Clothes_List);//列表

        Bundle bundle=this.getIntent().getExtras();
        strSearch_Name=bundle.getString("name");
        System.out.println("strSearch_Name="+strSearch_Name);

        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        //查询整张衣物信息表
        sql="select clothes_img,name,designer,type,price,rank,size from clothes_information;";
        sql1="select clothes_img,name,designer,type,price,rank,size from clothes_information where name='"+strSearch_Name+"';";
        System.out.println("sql="+sql);
        //以下开始数据库操作，使用线程,查找出的用户显示在listView中
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结果
                    if (strSearch_Name.equals("")) {
                        rs= DBUtils.getSelectResultSet(sql);
                        return;
                    }else{
                        rs= DBUtils.getSelectResultSet(sql1);
                        return;
                    }
                    //rs= DBUtils.getSelectResultSet(sql);
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
            //根据用户查询自己的衣物信息
            String[] names1={"clothes_img","name","designer","type","price","rank","size"};//建立字段名结果集  //数据库中的
            String[] names2={"Clothes_Info_Img", "Clothes_Name", "Clothes_Author", "Clothes_Type", "Clothes_Price", "Clothes_Rank", "Clothes_Publish"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            //后两个String[] int[]数组都是admin_book_item中的id
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_search_clothesinfo.this, data, R.layout.admin_clothes_item1,
                    new String[]{"Clothes_Info_Img", "Clothes_Name", "Clothes_Author", "Clothes_Type", "Clothes_Price", "Clothes_Rank", "Clothes_Publish"},
                    new int[]{R.id.Clothes_Info_Img, R.id.Clothes_Name, R.id.Clothes_Author, R.id.Clothes_Type, R.id.Clothes_Price, R.id.Clothes_Rank, R.id.Clothes_Publish});
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
