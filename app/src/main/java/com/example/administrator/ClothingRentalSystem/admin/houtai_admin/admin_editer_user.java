package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

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
 * 编辑用户页面
 * 功能：1.编辑用户信息
 */

public class admin_editer_user extends AppCompatActivity {
    private ListView listView;
    private ImageButton back_bt;
    private CountDownLatch countDownLatch;
    private String sql;
    private ResultSet rs;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editer_user);
        init();//初始化界面

    }

    private void init() {
        back_bt = (ImageButton) findViewById(R.id.deleteuser_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editer_user.this, admin_manager_user.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.delete_user_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());

        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        //查询所有用户信息
        sql="select username,password,name,sex,phone from user where identity=0";
        System.out.println("sql="+sql);
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
            //根据信息
            String[] names1={"username","password","name", "sex", "phone"};//建立字段名结果集
            String[] names2={"username","password","name", "sex", "phone"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_editer_user.this, data, R.layout.select_user_item,
                    new String[]{"username","password","name", "sex", "phone"},//数据库中的字段
                    new int[]{R.id.user_user, R.id.user_pwd, R.id.user_name, R.id.user_sex, R.id.user_phone});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //listview的单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //传值到修改界面
                int i = position + 1;
                System.out.println("i="+i);
                String str=listView.getItemAtPosition(position).toString();
                String[] strs=str.split(", ");
                String name=null;
                for (int j=0;j<strs.length;j++){
                    if (strs[j].contains("username"))
                    {
                        name=strs[j].substring(9,strs[j].length()-1);
                    }
                }
                System.out.println("1111111111111111111name="+name);
                Intent intent = new Intent(admin_editer_user.this, admin_update_user.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
