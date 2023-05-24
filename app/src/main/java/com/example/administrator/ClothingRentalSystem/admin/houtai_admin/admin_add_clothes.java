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
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

/**
 * 管理员添加衣服详情信息的界面
 */

public class admin_add_clothes extends BaseActivity implements View.OnClickListener {
;
    Uri uri;//图片路径
    private ImageView ClothesImg;

    //对应的activity_add_clothes.xml中的文本框
    private EditText et_ClothesId,et_ClothesName,et_ClothesType,et_ClothesDesigner,et_ClothesSize,et_ClothesPrice,et_ClothesRank,et_ClothesComment;
    private Button btn_ClothesCommit,btn_ClothesBack;//提交、重置按钮

    //获取的EditText中字符串
    private String strClothesImg,strClothesId,strClothesName,strClothesType,strClothesDesigner,strClothesSize,strClothesPrice,strClothesRank,strClothesComment,sql;
    private CountDownLatch countDownLatch;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
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

        Resources r = getResources();
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
                if (et_ClothesId.getText().length()!=10) {
                    Toast.makeText(admin_add_clothes.this,"请输入10位衣物编号",Toast.LENGTH_SHORT).show();
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
                    strClothesImg = String.valueOf(uri);
                    strClothesId = et_ClothesId.getText().toString();
                    strClothesName = et_ClothesName.getText().toString();
                    strClothesType = et_ClothesType.getText().toString();
                    strClothesDesigner = et_ClothesDesigner.getText().toString();
                    strClothesSize = et_ClothesSize.getText().toString();
                    strClothesPrice = et_ClothesPrice.getText().toString();
                    strClothesRank = et_ClothesRank.getText().toString();
                    strClothesComment = et_ClothesComment.getText().toString();

                    sql="insert into clothes_information(clothes_img,id,name,type,designer,size,price,rank,comment) values('" +
                            strClothesImg+"','"+strClothesId+"','"+strClothesName+"','"+strClothesType+"','"+strClothesDesigner+"','"+
                            strClothesSize+"','"+strClothesPrice+"','"+strClothesRank+"','"+strClothesComment+"');";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  // 请求码
                parseUri(data);
                break;
            default:
        }
    }

    public void parseUri(Intent data) {
        uri=data.getData();
        InputStream is=null;
        Bitmap bmp=null;
        if(uri.getAuthority()!=null){
            try {
                is= admin_add_clothes.this.getContentResolver().openInputStream(uri);
                bmp= BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
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
