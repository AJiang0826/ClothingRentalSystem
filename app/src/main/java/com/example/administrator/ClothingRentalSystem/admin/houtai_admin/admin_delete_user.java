package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员删除读者
 * 功能：1.删除用户信息
 *      2.点击删除会跳出弹窗询问是否删除
 * 变量：1.listView
 *      2.back_bt删除按钮
 */
public class admin_delete_user extends AppCompatActivity {
    private ListView listView;
    private ImageButton back_bt;
    private CountDownLatch countDownLatch;
    private String sql1,sql2,sql3;
    private ResultSet rs,rs2;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);//删除界面
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        init();//初始化界面

    }

    private void init() {
        back_bt = (ImageButton) findViewById(R.id.edituser_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_delete_user.this, admin_manager_user.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.edit_user_list);
        final databaseHelp help = new databaseHelp(getApplicationContext());


        sql1="select username,password,name,sex,phone from user where identity=0";
        System.out.println("sql1="+sql1);
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


        //listView.setAdapter(adapter);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //listview的单击事件监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String name=null;
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //获取该行值
                String str=listView.getItemAtPosition(position).toString();
                String[] strs=str.split(", ");
                //String name=null;
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
                        System.out.println("sql2="+sql2);
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
                        ActivityCollector.finishAll();

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
        help.close();
    }
}
