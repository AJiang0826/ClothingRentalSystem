package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.person_borrow;
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
 * 该界面用于修改服装信息，并且将其修改的信息存入到数据库
 * 同时通过上一个select_information 界面的传参到该界面，
 * 并且通过该参数，将信息展现在该界面
 */

public class admin_update_clothes extends BaseActivity implements View.OnClickListener {

    private String name;
    Uri uri;
    private ImageView ClothesImg;//界面上的图片所在的位置
    //定义界面各组件的名称
    private EditText Et_ClothesId, Et_ClothesName, Et_ClotheStype, Et_ClothesWriter, Et_ClothesPublicer, Et_ClothesPrice, Et_ClothesRank, Et_ClothesComment;
    private Button Btn_ClothesCommit;
    private CountDownLatch countDownLatch;
    private ResultSet rs;//数据库结果集
    private int rows;//用于记录返回结果
    String uriname;//定义字符串用于接受图片原本的地址
    int id;//用于接收另一个界面传过来的衣服id的值

    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upadte_clothes);
        System.out.println("3333333333333333333333333333333333333333333333");
        initdata();//界面初始化

    }

    private void initdata() {
        Bundle bundle = getIntent().getExtras();
        //name = bundle.getString("name") ;
        id = bundle.getInt("Id");//接受另一个界面传递过来的参数
        // Log.i("cursor", "initdata: " +name);
        System.out.println("sname=" + id);

//初始化组件
        ClothesImg = findViewById(R.id.ClothesImg);
        Et_ClothesId = findViewById(R.id.et_ClothesId);
        Et_ClothesId.setFocusable(false);
        Et_ClothesName = findViewById(R.id.et_ClothesName);
        Et_ClotheStype = findViewById(R.id.et_ClotheStype);
        Et_ClothesWriter = findViewById(R.id.et_ClothesWriter);
        Et_ClothesPublicer = findViewById(R.id.et_ClothesPublicer);
        Et_ClothesPrice = findViewById(R.id.et_ClothesPrice);
        Et_ClothesRank = findViewById(R.id.et_ClothesRank);
        Et_ClothesComment = findViewById(R.id.et_ClothesComment);


        Btn_ClothesCommit = findViewById(R.id.Btn_ClothesCommit);

        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql = "select * from clothes_information where id=" + id;//查询整张租借表
        System.out.println("sql=" + sql);
        //以下开始数据库操作，使用线程，搜素衣服信息，并且将 信息呈现在页面
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
        //等待线程结果，把结果集转换成一定格式，并呈现在页面上
        try {
            countDownLatch.await();//阻塞等待线程执行完毕
            while (rs.next()) {
                ClothesImg.setImageURI(Uri.parse(rs.getString("clothes_img")));
                Et_ClothesId.setText(rs.getString("id"));
                Et_ClothesName.setText(rs.getString("name"));
                Et_ClotheStype.setText(rs.getString("type"));
                Et_ClothesWriter.setText(rs.getString("designer"));
                Et_ClothesPrice.setText(rs.getString("price"));
                Et_ClothesPublicer.setText(rs.getString("size"));
                Et_ClothesComment.setText(rs.getString("comment"));
                Et_ClothesRank.setText(rs.getString("rank"));
                uriname = rs.getString("clothes_img");
            }
            uri = Uri.parse(uriname);//用于将图片初始路径赋予给uri
        } catch (Exception e) {
            e.printStackTrace();
        }

//添加监听
        Btn_ClothesCommit.setOnClickListener(this);
        ClothesImg.setOnClickListener(this);

    }

    //对管理员输入的衣服信息进行验证，全部符合要求才能通过
    boolean testother = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_ClothesCommit:
                if (Et_ClothesName.getText().length() == 0) {
                    Toast.makeText(admin_update_clothes.this, "请输入完整衣服信息", Toast.LENGTH_SHORT).show();
                    testother = false;
                    break;
                }
                //符合要求的
                if (testother == true) {
                    System.out.println(ClothesImg.toString());
                    countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
                    System.out.println("进入更改数据库");
                    System.out.println("uri=+--------" + uri);
                    sql = "update clothes_information set clothes_img='" + uri + "',name='" +
                            Et_ClothesName.getText().toString() + "',type='" + Et_ClotheStype.getText().toString()
                            + "',designer='" + Et_ClothesWriter.getText().toString() + "',size='" + Et_ClothesPublicer.getText().toString()
                            + "',price='" + Et_ClothesPrice.getText().toString() + "',rank='" + Et_ClothesRank.getText().toString()
                            + "',comment='" + Et_ClothesComment.getText().toString() + "'where id='" + id + "'";//查询整张租借表
                    System.out.println("sql=" + sql);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //获得查询结果
                                rows = DBUtils.getUpdateRows(sql);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                //该线程执行完毕-1
                                countDownLatch.countDown();
                            }
                        }
                    }).start();
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("----------------------row=" + rows);
                    if (rows > 0)
                        Toast.makeText(admin_update_clothes.this, "修改衣服信息成功！", Toast.LENGTH_SHORT).show();
                    else
                        System.out.println("修改衣服失败！请重新尝试！");
                    Intent intent = new Intent(admin_update_clothes.this, admin_select_clothesinfo.class);
                    startActivity(intent);
                    finish();

                    break;
                }
            //点击图片出现文件选择器
            case R.id.ClothesImg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");//选择image类型的资源
                startActivityForResult(intent, 1);  // 第二个参数是请求码
                break;


        }
    }

    @Override
    //   用于显示图片
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  // 请求码
                parseUri(data);
                break;
            default:
        }
    }
//获取虚拟中所选中图片的路径，并且将该路径传递给uri，更新数据库
    public void parseUri(Intent data) {
        uri = data.getData();
        InputStream is = null;
        Bitmap bmp = null;
        if (uri.getAuthority() != null) {
            try {
                is = admin_update_clothes.this.getContentResolver().openInputStream(uri);
                bmp = BitmapFactory.decodeStream(is);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ClothesImg.setImageBitmap(bmp);
    }


}
