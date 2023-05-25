package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.utils.ContentUriUtil;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.MainActivity;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 本类作用是首页点击服装，呈现具体的服装内容
 * 也是与后端数据库相连，选择服装尺码后，选择是租借还是收藏衣服
 **/
public class borrowActivity extends AppCompatActivity {
    private TextView id,clothesName,clothesType,clothesPrice;
    private ImageView clothesImg;
    private Spinner clothesSize;
    private Button colthesCollection,clothesBorrow;
    private String borrowTime,collectTime;//获取当前租借、收藏时间
    int rowId;
    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private String sql;
    private ResultSet rs;
    private String[] size;//尺码数组，从数据库中读取
    private String selectedSize;//获取下拉框尺码内容
    private int rows;//获取数据库影响数据行数
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        id=(TextView)findViewById(R.id.ClothesId);
        clothesName=(TextView)findViewById(R.id.ClothesName);
        clothesType=(TextView)findViewById(R.id.ClothesTypes);
        clothesSize=(Spinner)findViewById(R.id.ClothesSize);
        clothesPrice=(TextView)findViewById(R.id.ClothesPrice);
        clothesImg=(ImageView)findViewById(R.id.ClothesImg);
        Bundle bundle=this.getIntent().getExtras();
        System.out.println("bundle.getInt(\"id\")====="+bundle.getInt("id"));
        rowId=bundle.getInt("id") + 1;
        System.out.println("rowId"+rowId);
        sql="select *from clothes_information";
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        //以下开始数据库操作，使用线程，插入用户
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //成功创建普通用户
                    rs= DBUtils.getSelectResultSet(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //该线程执行完毕-1
                    countDownLatch.countDown();
                }
            }
        }).start();
        //等待线程查找完结果，输出结果到界面上
        try {
            countDownLatch.await();//阻塞等待线程执行完毕
            rs.last();
            if (rs.getRow()<rowId){
                System.out.println("记录行数不对！当前结果集的行数是："+rs.getRow()+"，而点击行数为："+rowId+"！请检查修改后再后续操作！");
            }
            rs.beforeFirst();
            rs.relative(rowId);
            System.out.println("borrowActivity---------rs.getRow="+rs.getRow());
            id.setText(rs.getString("id"));
            clothesName.setText(rs.getString("name"));
            clothesType.setText(rs.getString("type"));
            String strSize=rs.getString("size");
            size=strSize.split("、");
            //clothesSize.setText(rs.getString("size"));
            // 数组适配器
            ArrayAdapter<String> gradeAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,size);
            clothesSize.setAdapter(gradeAdapter);
            //设置默认选中项
            clothesSize.setSelection(0);
            clothesPrice.setText(rs.getString("price"));
            //设置图片
            Uri uri= Uri.parse(rs.getString("clothes_img"));
            System.out.println(ContentUriUtil.getPath2uri(borrowActivity.this,uri));
            clothesImg.setImageURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取下拉框选中尺码的内容
        selectedSize = clothesSize.getSelectedItem().toString();//首先获取默认内容
        //增加监听获取内容
        clothesSize.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selectedSize = size[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        clothesBorrow=(Button) findViewById(R.id.RentClothes);
        clothesBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSize.equals("")){
                    Toast.makeText(borrowActivity.this,"还未选择尺码，请选择后再租借！",Toast.LENGTH_LONG).show();
                }
                //获取当前系统时间用作借阅日期time
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                borrowTime = formatter.format(curDate);
                sql="insert into clothes_lease(user_name,clothes_id,clothes_size,clothes_borrow_data,flage) values(" +
                        "'"+MainActivity.getStrUserName()+"',"+id.getText().toString()+",'"+selectedSize+"','"+borrowTime+"',0);";
                countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
                //以下开始数据库操作，使用线程，插入用户
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //成功添加租借信息
                            rows=DBUtils.getUpdateRows(sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //该线程执行完毕-1
                            countDownLatch.countDown();
                        }
                    }
                }).start();
                //等待线程插入完结果，跳转页面或者
                try {
                    countDownLatch.await();//阻塞等待线程执行完毕
                    if (rows>0){
                        //添加租借信息成功，弹出提示
                        Toast.makeText(borrowActivity.this,"租借成功！请前往前台领取衣服！",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(borrowActivity.this,"租借失败，请联系管理员！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        colthesCollection=(Button) findViewById(R.id.CollectClothes);
        colthesCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSize.equals("")){
                    Toast.makeText(borrowActivity.this,"还未选择尺码，请选择后再收藏！",Toast.LENGTH_LONG).show();
                }
                //获取当前系统时间用作收藏日期time
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                collectTime = formatter.format(curDate);
                sql="insert into user_collect(user_name,clothes_id,clothes_name,clothes_size,collect_date) values(" +
                        "'"+MainActivity.getStrUserName()+"',"+id.getText().toString()+",'"+clothesName.getText().toString()+"','"+selectedSize+"','"+collectTime+"');";
                countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
                //以下开始数据库操作，使用线程，插入用户
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //成功添加收藏信息
                            rows=DBUtils.getUpdateRows(sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //该线程执行完毕-1
                            countDownLatch.countDown();
                        }
                    }
                }).start();
                //等待线程插入完结果，弹窗提醒
                try {
                    countDownLatch.await();//阻塞等待线程执行完毕
                    if (rows>0){
                        //添加租借信息成功，弹出提示
                        Toast.makeText(borrowActivity.this,"收藏成功！期待您的租借！",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(borrowActivity.this,"收藏失败！请联系管理员！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
