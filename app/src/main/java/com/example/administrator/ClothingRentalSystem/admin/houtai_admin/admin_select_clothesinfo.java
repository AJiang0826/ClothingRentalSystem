package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员查衣服信息
 */

public class admin_select_clothesinfo extends AppCompatActivity {
    private ListView listView;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;
    List<Map<String, Object>> data;
    private int row;
    int clothesid;
    String clothesname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_clotheskinfo);
        init();//界面初始化

    }

    private void init() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        listView = (ListView) findViewById(R.id.Select_Clothes_List);
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql="Select clothes_img,id,name,type,price, size from clothes_information";//查询整张租借表
        System.out.println("sql="+sql);
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
            String[] names1={"clothes_img","id","name","type","price","size"};//建立字段名结果集
            String[] names2={"Clothes_Info_Img", "Clothes_Id", "Clothes_Name", "Clothes_Type", "Clothes_Price", "Clothes_Publish"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
             data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_select_clothesinfo.this, data, R.layout.admin_clothes_item,
                    new String[]{"Clothes_Info_Img", "Clothes_Id", "Clothes_Name", "Clothes_Type", "Clothes_Price", "Clothes_Publish"},
                    new int[]{R.id.Clothes_Info_Img, R.id.Clothes_Id, R.id.Clothes_Name, R.id.Clothes_Type, R.id.Clothes_Price, R.id.Clothes_Publish});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //长时间停留则跳出来是否删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long temp = l;
                int a = i+ 1;



                System.out.println("i="+a);
                String str=listView.getItemAtPosition(i).toString();
                String[] strs=str.split(", ");
                clothesname=null;
                for (int j=0;j<strs.length;j++){
//                    System.out.println("strs[j]="+strs[j]);
                    if (strs[j].contains("Id"))
                    {
                        clothesname=strs[j].substring(11,strs[j].length()-1);
                    }
                }
                clothesid= Integer.parseInt(clothesname);
                builder.setMessage("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
                        sql="delete from clothes_information where id="+clothesid;//查询整张租借表
                        System.out.println("sql="+sql);
                        //以下开始数据库操作，使用线程，插入用户
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //获得查询结果
                                    row=DBUtils.getUpdateRows(sql);
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
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("----------------------row="+row);
                        if (row > 0)
                            Toast.makeText( admin_select_clothesinfo.this, "删除衣服信息成功！", Toast.LENGTH_SHORT).show();
                        else
                            System.out.println("删除衣服失败！请重新尝试！");
                        Intent intent = new Intent( admin_select_clothesinfo.this, admin_select_clothesinfo.class);
                        startActivity(intent);
                        finish();


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
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
                System.out.println("i="+i);
                String str=listView.getItemAtPosition(position).toString();
                String[] strs=str.split(", ");
                String name=null;
                for (int j=0;j<strs.length;j++){
//                    System.out.println("strs[j]="+strs[j]);
                    if (strs[j].contains("Id"))
                    {
                        name=strs[j].substring(11,strs[j].length()-1);
                    }
                }
                int id= Integer.parseInt(name);
                System.out.println("1111111111111111111111111111111111name="+id);
                Intent intent = new Intent(admin_select_clothesinfo.this, admin_update_clothes.class);
                Bundle bundle = new Bundle();
               // bundle.putString("name",name);
                bundle.putInt("Id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
