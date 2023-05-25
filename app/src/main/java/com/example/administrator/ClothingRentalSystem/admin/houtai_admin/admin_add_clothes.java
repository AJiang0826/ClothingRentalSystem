package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.ContentUriUtil;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员添加衣服详情信息的界面
 * 功能：1.添加衣服详情信息
 *      2.点击重置会清空所有文本框
 *      3.点击提交会验证name是否为空 and 验证id是否为六位 and 去数据库验证id是否重复
 * 变量：1.listView
 *      2.btn_ClothesCommit，btn_ClothesBack提交、重置按钮
 */

public class admin_add_clothes extends BaseActivity implements View.OnClickListener {
;
    Uri uri;//通用资源标志符（Universal Resource Identifier）
    private ImageView ClothesImg;

    //对应的activity_add_clothes.xml中的文本框
    private EditText et_ClothesId,et_ClothesName,et_ClothesType,et_ClothesDesigner,et_ClothesSize,et_ClothesPrice,et_ClothesRank,et_ClothesComment;
    private Button btn_ClothesCommit,btn_ClothesBack;//提交、重置按钮

    //获取的EditText中字符串
    private String strClothesImg,strClothesId,strClothesName,strClothesType,strClothesDesigner,strClothesSize,strClothesPrice,strClothesRank,strClothesComment,sql;
    private CountDownLatch countDownLatch;
    private int rows;
    private ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
        countDownLatch = new CountDownLatch(1);
        initdata();//界面初始化
    }

    private void initdata() {
        ClothesImg=findViewById(R.id.ClothesImg);

        et_ClothesId=findViewById(R.id.et_ClothesId);
        et_ClothesName=findViewById(R.id.et_ClothesName);
        et_ClothesType=findViewById(R.id.et_ClothesType);
        et_ClothesDesigner=findViewById(R.id.et_ClothesDesigner);
        et_ClothesSize=findViewById(R.id.et_ClothesSize);
        et_ClothesPrice=findViewById(R.id.et_ClothesPrice);
        et_ClothesRank=findViewById(R.id.et_ClothesRank);
        et_ClothesComment=findViewById(R.id.et_ClothesComment);

        //按钮
        btn_ClothesCommit=findViewById(R.id.btn_ClothesCommit);//添加
        btn_ClothesBack=findViewById(R.id.btn_ClothesBack);//返回

        ClothesImg.setOnClickListener(this);//选择图片
        btn_ClothesCommit.setOnClickListener(this);//添加按钮
        btn_ClothesBack.setOnClickListener(this);//重置按钮

        Resources r = getResources();//获取存在系统的资源
        //path转uri
        //Uri一般由三部分组成：访问资源的命名机制。存放资源的主机名。资源自身的名称，由路径表示。
        uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.click_white) + "/"
                + r.getResourceTypeName(R.drawable.click_white) + "/"
                + r.getResourceEntryName(R.drawable.click_white));
    }

    //对管理员输入的图书信息进行验证，全部符合要求才能通过
    boolean testid=true,testother=true;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_ClothesCommit://点击选中添加按钮
                //衣服编号为十位，且不为空
                if (et_ClothesId.getText().length()!=6) {
                    Toast.makeText(admin_add_clothes.this,"请输入6位衣物编号",Toast.LENGTH_SHORT).show();
                    testid=false;
                    break;
                }
                //衣服名不为空
                if(et_ClothesName.getText().length()==0){
                    Toast.makeText(admin_add_clothes.this,"请输入完整衣服名",Toast.LENGTH_SHORT).show();
                    testother=false;
                    break;
                }
                //向数据库内添加衣服信息
                if(testid==true&&testother==true){
                    //获取对应字符串
                    strClothesImg = ContentUriUtil.getPath2uri(admin_add_clothes.this,uri);//把uri转化为绝对路径
                    strClothesId = et_ClothesId.getText().toString();
                    strClothesName = et_ClothesName.getText().toString();
                    strClothesType = et_ClothesType.getText().toString();
                    strClothesDesigner = et_ClothesDesigner.getText().toString();
                    strClothesSize = et_ClothesSize.getText().toString();
                    strClothesPrice = et_ClothesPrice.getText().toString();
                    strClothesRank = et_ClothesRank.getText().toString();
                    strClothesComment = et_ClothesComment.getText().toString();

                    //添加信息到数据库
                    sql="insert into clothes_information(clothes_img,id,name,type,designer,size,price,rank,comment) values('" +
                            strClothesImg+"','"+strClothesId+"','"+strClothesName+"','"+strClothesType+"','"+strClothesDesigner+"','"+
                            strClothesSize+"','"+strClothesPrice+"','"+strClothesRank+"','"+strClothesComment+"');";

                    //使用线程，查询编号是否已经存在【编号唯一，不是衣服编号】
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                rs = DBUtils.getSelectResultSet("Select * from clothes_information where id='"+strClothesId+"'");
                                rs.last();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }finally {
                                //该线程执行完毕-1
                                //countDown();//对计数器进行递减1操作，当计数器递减至0时，当前线程会去唤醒阻塞队列里的所有线程。
                                countDownLatch.countDown();
                            }
                        }
                    }).start();
                    //等待线程查询完结果
                    try {
                        //await();//阻塞当前线程，将当前线程加入阻塞队列。
                        countDownLatch.await();
                        if(rs.getRow()!=0){//获取行数
                            /*System.out.println("strClothesId="+strClothesId);
                            System.out.println("rs="+rs);
                            System.out.println("rs.getRow="+rs.getRow());*/
                            Toast.makeText(admin_add_clothes.this, "该编号已存在！", Toast.LENGTH_LONG).show();
                            ((EditText) findViewById(R.id.et_ClothesId)).setText("");
                            ((EditText) findViewById(R.id.et_ClothesId)).requestFocus();
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }

                    //是上方select查询的
                    //CountDownLatch(int count); //构造方法，创建一个值为count的计数器。
                    countDownLatch = new CountDownLatch(1);
                    //以下开始数据库操作，使用线程，插入衣服信息
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //成功添加衣物信息
                                rows=DBUtils.getUpdateRows(sql);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }finally {
                                //该线程执行完毕-1
                                countDownLatch.countDown();
                            }
                        }
                    }).start();
                    try {
                        //await();//阻塞当前线程，将当前线程加入阻塞队列。
                        countDownLatch.await();
                        if (rows>0){
                            Toast.makeText(admin_add_clothes.this,"添加衣物信息成功！",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(admin_add_clothes.this,"添加衣物信息失败！",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                    //ActivityCollector.finishAll();
                }

            //点击图片进行选择
            case R.id.ClothesImg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);  // 第二个参数是请求码
                break;

            //重置--清空所有EditText内容
            case R.id.btn_ClothesBack:
                et_ClothesId.getText().clear();
                et_ClothesName.getText().clear();
                et_ClothesType.getText().clear();
                et_ClothesDesigner.getText().clear();
                et_ClothesSize.getText().clear();
                //spinner.getChildAt(0).setVisibility(View.INVISIBLE);
                et_ClothesPrice.getText().clear();
                et_ClothesRank.getText().clear();
                et_ClothesComment.getText().clear();
        }
    }

    /*
    * requestCode:请求码，用于启动子Activity
    * resultCode:子Activity设置的结果码，用于指示操作结果。可以是任何整数值，但通常是resultCode ==  RESULT_OK或resultCode==RESULT_CANCELED
    * Data:用于打包返回数据的Intent,可以包括用于表示所选内容的URI。子Activity也可以在返回数据Intent时，添加一些附加消息
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  //请求码----标志资源
                parseUri(data);
                break;
            default:
        }
    }

    public void parseUri(Intent data) {
        uri=data.getData();//返回的Uri  //有三种形式：content://，file://，/document/
        InputStream is=null;//字节输入流
        Bitmap bmp=null;//位图 //获取图像文件信息
        if(uri.getAuthority()!=null){
            try {
                /*使用输入流创建一张Bitmap图片
                *得到图片(通过流的形式)*/
                is= admin_add_clothes.this.getContentResolver().openInputStream(uri);//Uri转换成Inputstream
                bmp= BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();//关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //给ImageView设置图片资源
        ClothesImg.setImageBitmap(bmp);//setImageBitmap()方法其实是调用了setImageDrawable()方法进行重绘
    }
}
