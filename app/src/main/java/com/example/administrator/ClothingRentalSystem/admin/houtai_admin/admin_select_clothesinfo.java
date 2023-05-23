package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
 * 管理员查衣服信息
 */

public class admin_select_clothesinfo extends AppCompatActivity {
    private ListView listView;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;
    List<Map<String, Object>> data;
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
        sql="Select clothes_img ,name,type,price, size from clothes_information";//查询整张租借表
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
            String[] names1={"clothes_img","name","type","price","size"};//建立字段名结果集
            String[] names2={"Clothes_Info_Img", "Clothes_Name", "Clothes_Type", "Clothes_Price", "Clothes_Publish"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
             data = ItemUtils.getList(names1,names2,rs);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    admin_select_clothesinfo.this, data, R.layout.admin_clothes_item,
                    new String[]{"Clothes_Info_Img", "Clothes_Name", "Clothes_Type", "Clothes_Price", "Clothes_Publish"},
                    new int[]{R.id.Clothes_Info_Img, R.id.Clothes_Name, R.id.Clothes_Type, R.id.Clothes_Price, R.id.Clothes_Publish});//后两个String[] int[]数组都是borrow_item中的id
             adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                 @Override
                 public boolean setViewValue(View view, Object o, String s) {
                     if (view.getId() == R.id.Clothes_Info_Img) {
                                    ImageView imageView = (ImageView) view;

                         for(Map<String,Object> map : data){
                             for(String key : map.keySet()){
                                 imageView.setImageURI(Uri.parse((String) map.get("Clothes_Info_Img")));
                                 System.out.println(key+":"+map.get(key));
                             }
                         }
                                    return true;
                                } else {
                                    return false;
                                }
                 }
             });
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //长时间停留则跳出来是否删除
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final long temp = l;
//                builder.setMessage("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        help.delbookinfo((int) temp);
////                        //删除后重新显示
////                        Cursor cursor = help.querybookinfo();
////                        String from[] = {"name", "type", "writer","publicer","rank","img"};
////                        int to[] = {R.id.Clothes_Name, R.id.Clothes_Type, R.id.Clothes_Author, R.id.Clothes_Publish, R.id.Clothes_Rank, R.id.Clothes_Info_Img};
////                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(admin_select_clothesinfo.this, R.layout.admin_book_item, cursor, from, to);
////                        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
////                            @Override
////                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
////                                if (view.getId() == R.id.Clothes_Info_Img) {
////                                    ImageView imageView = (ImageView) view;
////                                    imageView.setImageURI(Uri.parse(cursor.getString(columnIndex)));
////                                    return true;
////                                } else {
////                                    return false;
////                                }
////                            }
////                        });
//                        listView.setAdapter(adapter);
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//                return true;
//            }
//        });


        //listview的单击事件,修改衣服信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //传值到修改界面
                int i = position;
                Intent intent = new Intent(admin_select_clothesinfo.this, admin_update_clothes.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", i);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
