package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;


import android.content.Intent;
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
import java.util.concurrent.CountDownLatch;

/**    管理员：该界面用于修改服装信息
 * 将其修改的信息存入到数据库
 * 同时通过上一个select_information 界面的传参到该界面，
 * 并且通过该参数，将信息展现在该界面
 */

public class admin_update_clothes extends BaseActivity implements View.OnClickListener {

    Uri uri;
    private ImageView ClothesImg;//界面上的图片所在的位置
    //定义界面各组件的名称
    private EditText Et_ClothesId, Et_ClothesName, Et_ClotheStype, Et_ClothesWriter, Et_ClothesPublicer, Et_ClothesPrice, Et_ClothesRank, Et_ClothesComment;
    private Button Btn_ClothesCommit;
    private CountDownLatch countDownLatch;//定义一个计数器，和一个阻塞队列，当计数器的值递减为0之前，阻塞队里里面的线程处于挂起状态，当为0时唤醒
    private ResultSet rs;//数据库结果集
    private int rows;//用于记录返回结果
    String uriname;//定义字符串用于接受图片原本的地址
    int id;//用于接收另一个界面传过来的衣服id的值

    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upadte_clothes);
        //System.out.println("3333333333333333333333333333333333333333333333");
        initdata();//界面初始化

    }

    private void initdata() {
        Bundle bundle = getIntent().getExtras();
        //name = bundle.getString("name") ;
        id = bundle.getInt("Id");//接受另一个界面传递过来的参数
       // System.out.println("sname=" + id);

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

        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1，表示当前线程任务为一个
        sql = "select * from clothes_information where id=" + id;//查询整张租借表
      //  System.out.println("sql=" + sql);
        //以下开始数据库操作，使用线程，搜素衣服信息，并且将 信息呈现在页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得查询结果，返回结果集
                    rs = DBUtils.getSelectResultSet(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //该线程执行完毕-1
                    countDownLatch.countDown();//对计数器减一，计数器值为0，会唤阻塞队列里面的所有线程
                }
            }
        }).start();
        //等待线程结果，把结果集转换成一定格式，并呈现在页面上
        try {
            countDownLatch.await();//阻塞等待线程执行完毕
            while (rs.next()) {
                ClothesImg.setImageURI(Uri.parse(rs.getString("clothes_img")));//接受一个URL字符串，解析它，然后返回一个URL对象
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
                    //将uri地址转换成相对地址
                    String realPath= ContentUriUtil.getPath2uri(admin_update_clothes.this,uri);
                    sql = "update clothes_information set clothes_img='" + realPath + "',name='" +
                            Et_ClothesName.getText().toString() + "',type='" + Et_ClotheStype.getText().toString()
                            + "',designer='" + Et_ClothesWriter.getText().toString() + "',size='" + Et_ClothesPublicer.getText().toString()
                            + "',price='" + Et_ClothesPrice.getText().toString() + "',rank='" + Et_ClothesRank.getText().toString()
                            + "',comment='" + Et_ClothesComment.getText().toString() + "'where id='" + id + "'";//查询整张租借表
                //    System.out.println("sql=" + sql);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //获得查询结果，查询结果row=1表示该数据库里面有该条数据，可以进行修改，并且修改成功
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
                    if (rows > 0)//在数据库里面有该数据，并且修改成功
                        Toast.makeText(admin_update_clothes.this, "修改衣服信息成功！", Toast.LENGTH_SHORT).show();
                    else
                        System.out.println("修改衣服失败！请重新尝试！");
                    //跳转界面
                    Intent intent = new Intent(admin_update_clothes.this, admin_select_clothesinfo.class);
                    //启动该intent，实现跳转
                    startActivity(intent);
                    finish();

                    break;
                }
            //点击图片出现文件选择器
            case R.id.ClothesImg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//打开虚拟机本地文件
                //Intent intent = new Intent(Intent.ACTION_PICK);//打开文件，ACTION_PICK选择照片
                intent.setType("image/*");//选择image类型的资源
                startActivityForResult(intent, 1);  // 第二个参数是请求码，用于之后回调判断数据来源，请求码要是唯一

                break;


        }
    }

    @Override
    //   返回的onActivityResultra中接收选取的返回图片资源，data是页面的返回资源
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {//对请求码进行判断，请求码不同返回的数据源不同
            case 1:  // 请求码
                parseUri(data);//调用方法parseUri
                break;
            default:
        }
    }
//获取虚拟中所选中图片的路径，并且将该路径传递给uri，更新数据库
    //将uri转成流的形式，再将流转成bmp的位图显示在界面上
    public void parseUri(Intent data) {
        uri = data.getData();//获取文件夹中中图片的uri
        InputStream is = null;//输入字节流
        Bitmap bmp = null;
        if (uri.getAuthority() != null) {
            try {
                //在Activity和SerVice中可以直接调用getContentResolver（）方法
                is = admin_update_clothes.this.getContentResolver().openInputStream(uri);//将uri转换成流形式
                bmp = BitmapFactory.decodeStream(is);//将流资源转换成位图

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
        ClothesImg.setImageBitmap(bmp);//将图片显示在界面上
    }


}
