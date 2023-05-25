package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
 * 编辑用户页面
 * 功能：1.listview列表显示所有用户信息，单击某一行可实现页面的跳转，跳转到修改界面
 *      2.可通过用户名搜索用户信息，并跳转到修改界面
 */

public class admin_editer_user extends AppCompatActivity {
    private ListView listView;
    private ImageButton back_bt;//图片按钮
    private CountDownLatch countDownLatch;//创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private String sql;
    private ResultSet rs;
    private EditText search_name;//搜索框
    private Button search_btn;//搜索按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//控制页面不随着软键盘上移
        setContentView(R.layout.activity_admin_editer_user);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        init();//初始化界面

    }

    private void init() {

        //返回--图片按钮监听
        back_bt = (ImageButton) findViewById(R.id.deleteuser_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editer_user.this, admin_manager_user.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.delete_user_item);

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
            String[] names1={"username","password","name", "sex", "phone"};//建立字段名结果集1---数据库中对应的字段名
            String[] names2={"username","password","name", "sex", "phone"};//建立字段名结果集2---xml中对应的id
            List<Map<String, Object>> data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_editer_user.this, data, R.layout.select_user_item,
                    new String[]{"username","password","name", "sex", "phone"},//数据库中对应的字段名
                    new int[]{R.id.user_user, R.id.user_pwd, R.id.user_name, R.id.user_sex, R.id.user_phone});//select_user_item中的id
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
                //查找包含字符串username
                for (int j=0;j<strs.length;j++){
                    if (strs[j].contains("username"))
                    {
                        name=strs[j].substring(9,strs[j].length()-1);
                    }
                }
                //传值---单击选中该行信息，获取username，并传值到修改界面
                Intent intent = new Intent(admin_editer_user.this, admin_update_user.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        //搜索按钮监听
        search_btn=findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name1);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //获取搜索框中输入信息，传给修改界面
                Intent intent = new Intent(admin_editer_user.this, admin_update_user.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",search_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
