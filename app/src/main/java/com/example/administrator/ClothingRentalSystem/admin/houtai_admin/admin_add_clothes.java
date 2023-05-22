package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员添加衣服的界面
 */

public class admin_add_clothes extends BaseActivity implements View.OnClickListener {
    private ImageButton back_bt;
    private Spinner spinner;
    private databaseHelp helper;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<String>();
    Uri uri;
    private String pub;
    private ImageView ClothesImg;
    private EditText et_ClothesId,et_ClothesName,et_ClothesType,et_ClothesWriter,et_ClothesPublicer,et_ClothesPrice,et_ClothesRank,et_ClothesComment;
    private Button btn_ClothesCommit,btn_ClothesBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pub = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initdata();//界面初始化
        helper = new databaseHelp(getApplicationContext());
    }

    private void initdata() {
        ClothesImg=findViewById(R.id.ClothesImg);

        et_ClothesId=findViewById(R.id.et_ClothesId);
        et_ClothesName=findViewById(R.id.et_ClothesName);
        et_ClothesType=findViewById(R.id.et_ClothesType);
        et_ClothesWriter=findViewById(R.id.et_ClothesWriter);
        et_ClothesPrice=findViewById(R.id.et_ClothesPrice);
        et_ClothesRank=findViewById(R.id.et_ClothesRank);
        et_ClothesComment=findViewById(R.id.et_ClothesComment);

        //按钮
        btn_ClothesCommit=findViewById(R.id.btn_ClothesCommit);//添加
        btn_ClothesBack=findViewById(R.id.btn_ClothesBack);//返回

        btn_ClothesCommit.setOnClickListener(this);//添加按钮
        ClothesImg.setOnClickListener(this);//选择图片
        btn_ClothesBack.setOnClickListener(this);//返回按钮

        Resources r = getResources();
        uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.clothes) + "/"
                + r.getResourceTypeName(R.drawable.clothes) + "/"
                + r.getResourceEntryName(R.drawable.clothes));
    }

    //对管理员输入的图书信息进行验证，全部符合要求才能通过
    boolean testid=true,testother=true;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_ClothesCommit://点击选中添加按钮
                //ISBN为十位，且不为空
                if (et_ClothesId.getText().length()!=10) {
                    Toast.makeText(admin_add_clothes.this,"请输入10位图书ISBN",Toast.LENGTH_SHORT).show();
                    testid=false;
                    break;
                }
                //书名不为空
                if(et_ClothesName.getText().length()==0){
                    Toast.makeText(admin_add_clothes.this,"请输入完整图书信息",Toast.LENGTH_SHORT).show();
                    testother=false;
                    break;
                }
                if(testid==true&&testother==true){
                    helper.inserbooktdata(et_ClothesId.getText().toString(),et_ClothesName.getText().toString(),
                            et_ClothesType.getText().toString(),et_ClothesWriter.getText().toString(),
                            pub,et_ClothesPrice.getText().toString(),
                            et_ClothesRank.getText().toString(),et_ClothesComment.getText().toString(),
                            String.valueOf(uri));
                    Toast.makeText(admin_add_clothes.this,"添加图书成功！",Toast.LENGTH_SHORT).show();
                    break;
                }

            //点击图片进行选择
            case R.id.ClothesImg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);  // 第二个参数是请求码
                break;

            //回退按钮
            case R.id.btn_ClothesBack:
                Intent intentback = new Intent();
                intentback.setClass(admin_add_clothes.this, admin_manager_clothes.class);
                startActivity(intentback);
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
