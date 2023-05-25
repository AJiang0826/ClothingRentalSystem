package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * 管理员删除用户
 * 功能：1.listview列表显示所有用户信息
 *      2.点击删除会跳出弹窗询问是否删除
 *      3.通过用户名查询用户信息进行删除
 */
public class admin_delete_user extends AppCompatActivity {
    private ListView listView;
    private ImageButton back_bt;//返回按钮
    private CountDownLatch countDownLatch;//创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private String sql1,sql2;
    private ResultSet rs;
    private EditText search_name;//搜索框
    private int rows;

    private Button search_btn_;//搜索按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//控制页面不随着软键盘上移
        setContentView(R.layout.activity_admin_delete_user);//删除界面
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        init();//初始化界面

    }

    private void init() {

        //返回--图片按钮监听
        back_bt = (ImageButton) findViewById(R.id.edituser_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_delete_user.this, admin_manager_user.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.edit_user_list);


        //显示所有用户信息
        sql1="select username,password,name,sex,phone from user where identity=0";
        //System.out.println("sql1="+sql1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结果
                    rs= DBUtils.getSelectResultSet(sql1);
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
            List<Map<String, Object>> data1 = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data1.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_delete_user.this, data1, R.layout.select_user_item,
                    new String[]{"username","password","name", "sex", "phone"},//数据库中的字段
                    new int[]{R.id.user_user, R.id.user_pwd, R.id.user_name, R.id.user_sex, R.id.user_phone});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*AlertDialog.Builder是AlertDialog封装的一个内部类，实现了构造器模式，
        所有AlertDialog需要设置一些属性必须通过构造器去构造。
        Builder设计模式可以很好地控制参数的个数以及灵活的组合多种参数。*/
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //listview的单击事件监听
        //确定按钮和取消按钮
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String name=null;
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //获取该行值
                String str=listView.getItemAtPosition(position).toString();
                String[] strs=str.split(", ");
                //查找包含username字符串
                for (int j=0;j<strs.length;j++){
                    if (strs[j].contains("username"))
                    {
                        name=strs[j].substring(9,strs[j].length()-1);
                    }
                }

                builder.setMessage("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sql2="delete from user where username='"+name+"';";
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    rows= DBUtils.getUpdateRows(sql2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }finally {
                                    //该线程执行完毕-1
                                    countDownLatch.countDown();
                                }
                            }
                        }).start();
                        //等待线程插入完结果
                        try {
                            countDownLatch.await();
                            Toast.makeText(admin_delete_user.this,"用户："+name+"删除成功！",Toast.LENGTH_SHORT).show();
                         } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(admin_delete_user.this, admin_delete_user.class);
                        startActivity(intent);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //搜索按钮监听
        search_btn_=findViewById(R.id.search_btn);
        search_name = findViewById(R.id.search_name1);
        search_btn_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //传值
                Intent intent = new Intent(admin_delete_user.this, admin_delete_userinfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",search_name.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
