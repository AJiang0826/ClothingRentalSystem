package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 查找读者的界面
 * 功能有：1.通过用户名查询用户
 *       2.显示所有用户
 *
 */
public class select_user_admin extends AppCompatActivity {
    private ListView listView;
    private Button search_btn;
    private EditText search_name;
    private CountDownLatch countDownLatch;
    private String sql;
    private ResultSet rs;
    private ImageButton back_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_admin);
        listView=(ListView)findViewById(R.id.sel_reader_list);

        //返回--图片按钮监听
        back_bt = (ImageButton) findViewById(R.id._back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(select_user_admin.this, admin_manager_user.class);
                startActivity(intent);
            }
        });

        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
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
                    select_user_admin.this, data, R.layout.select_user_item,
                    new String[]{"username","password","name", "sex", "phone"},//数据库中的字段
                    new int[]{R.id.user_user, R.id.user_pwd, R.id.user_name, R.id.user_sex, R.id.user_phone});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //搜索按钮获取并监听
        search_btn=findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(select_user_admin.this, select_user_admininfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",search_name.getText().toString());//传值
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
