package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
 * 该界面用于查询数据库中关于所有衣服的部分信息，
 * 通过单击李listview列表中的行可以实现页面的跳转，跳转到修改界面
 * 通过长时间点击列表中的行，可以实现对数据的删除
 */

public class admin_select_clothesinfo extends AppCompatActivity {
    private ListView listView;//定义列表
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;
    List<Map<String, Object>> data;//定义列表用于存放结果
    private int row;
    int clothesid;
    String clothesname;
    private ImageView clothesImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_clotheskinfo);
        init();//界面初始化

    }

    private void init() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        listView = (ListView) findViewById(R.id.Select_Clothes_List);
        clothesImg=(ImageView)findViewById(R.id.Clothes_Info_Img);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql = "Select clothes_img,id,name,type,price, size from clothes_information";//查询整张租借表
       // System.out.println("sql=" + sql);
        //以下开始数据库操作，使用线程，查询衣服信息
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
            String[] names1 = {"clothes_img", "id", "name", "type", "price", "size"};//建立数据库字段名结果集
            String[] names2 = {"Clothes_Info_Img", "Clothes_Id", "Clothes_Name", "Clothes_Type", "Clothes_Price", "Clothes_Publish"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            data = ItemUtils.getList(names1, names2, rs);//调用ItemUtils获取结果集
            //System.out.println("list=" + data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_select_clothesinfo.this, data, R.layout.admin_clothes_item,
                    new String[]{"Clothes_Info_Img", "Clothes_Id", "Clothes_Name", "Clothes_Type", "Clothes_Price", "Clothes_Publish"},
                    new int[]{R.id.Clothes_Info_Img, R.id.Clothes_Id, R.id.Clothes_Name, R.id.Clothes_Type, R.id.Clothes_Price, R.id.Clothes_Publish});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //长时间点击则跳出来是否删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int a = i + 1;

                //该段代码用于通过获取点击listview那一列的所有数据内容，并且将该内容存入数组中，通过将数组中的数据进行拆分取出需要的衣服的id
               // System.out.println("i=" + a);
                String str = listView.getItemAtPosition(i).toString();
                String[] strs = str.split(", ");//通过，将字符串str进行拆分
                clothesname = null;
                for (int j = 0; j < strs.length; j++) {
//                    System.out.println("strs[j]="+strs[j]);
                    if (strs[j].contains("Id")) {//将字符串中包含Id的字符串给获取出来
                        clothesname = strs[j].substring(11, strs[j].length() - 1);
                    }
                }
                clothesid = Integer.parseInt(clothesname);//将获取的字符串id修改为int型
                builder.setMessage("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
                        sql = "delete from clothes_information where id=" + clothesid;//查询整张租借表
                     //   System.out.println("sql=" + sql);
                        //以下开始数据库操作，使用线程删除选中的衣服信息
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //获得查询结果
                                    row = DBUtils.getUpdateRows(sql);
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
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("----------------------row=" + row);
                        if (row > 0)
                            Toast.makeText(admin_select_clothesinfo.this, "删除衣服信息成功！", Toast.LENGTH_SHORT).show();
                        else
                            System.out.println("删除衣服失败！请重新尝试！");
                        Intent intent = new Intent(admin_select_clothesinfo.this, admin_select_clothesinfo.class);
                        startActivity(intent);
                        finish();


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();//弹窗
                dialog.show();
                return true;
            }
        });


        //listview的单击事件,修改衣服信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //传值到修改界面
                int i = position + 1;
             //   System.out.println("i=" + i);
                String str = listView.getItemAtPosition(position).toString();
                String[] strs = str.split(", ");
                String name = null;
                for (int j = 0; j < strs.length; j++) {
//                    System.out.println("strs[j]="+strs[j]);
                    if (strs[j].contains("Id")) {
                        name = strs[j].substring(11, strs[j].length() - 1);
                    }
                }
                int id = Integer.parseInt(name);
            //    System.out.println("1111111111111111111111111111111111name=" + id);
                Intent intent = new Intent(admin_select_clothesinfo.this, admin_update_clothes.class);//跳转界面
                Bundle bundle = new Bundle();
                // bundle.putString("name",name);
                bundle.putInt("Id", id);//传递id，将id传递给另一个界面
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
