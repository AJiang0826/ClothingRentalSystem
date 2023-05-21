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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 管理员添加图书的界面
 */

public class admin_update_book extends BaseActivity implements View.OnClickListener {
    private ImageButton back_bt;
    //    private EditText name_ed, author_Ed, page_ed, price_ed, publish_ed, intime_rd;
    private Button add_book_bt;
    private String str;
    private int id;
    private databaseHelp helper;
    Uri uri;
    private ImageView bookimg;
    private EditText et_bookid,et_bookname,et_booktype,et_bookwriter,et_bookpublicer,et_bookprice,et_bookrank,et_bookcomment;
    private Button btn_bookcommit,btn_bookback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upadte_book);

        initdata();//界面初始化
        helper = new databaseHelp(getApplicationContext());
    }

    private void initdata() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id")+1 ;
        Log.i("cursor", "initdata: " + id);

        bookimg=findViewById(R.id.bookimg);

        et_bookid=findViewById(R.id.et_bookid);
        et_bookname=findViewById(R.id.et_bookname);
        et_booktype=findViewById(R.id.et_booktype);
        et_bookwriter=findViewById(R.id.et_bookwriter);
        et_bookpublicer=findViewById(R.id.et_bookpublicer);
        et_bookprice=findViewById(R.id.et_bookprice);
        et_bookrank=findViewById(R.id.et_bookrank);
        et_bookcomment=findViewById(R.id.et_bookcomment);


        btn_bookcommit=findViewById(R.id.btn_bookcommit);
        btn_bookback=findViewById(R.id.btn_bookback);

        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.querybookinfoid(id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            bookimg.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex("img"))));
            et_bookid.setText(cursor.getString(cursor.getColumnIndex("bookid")));
            et_bookname.setText(cursor.getString(cursor.getColumnIndex("name")));
            et_bookwriter.setText(cursor.getString(cursor.getColumnIndex("writer")));
            et_booktype.setText(cursor.getString(cursor.getColumnIndex("type")));
            et_bookprice.setText(cursor.getString(cursor.getColumnIndex("price")));
            et_bookpublicer.setText(cursor.getString(cursor.getColumnIndex("publicer")));
            et_bookcomment.setText(cursor.getString(cursor.getColumnIndex("comment")));
            et_bookrank.setText(cursor.getString(cursor.getColumnIndex("rank")));
        }

        btn_bookcommit.setOnClickListener(this);
        bookimg.setOnClickListener(this);
        btn_bookback.setOnClickListener(this);

    }
    //对管理员输入的图书信息进行验证，全部符合要求才能通过
    boolean testid=true,testother=true;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_bookcommit:
                //ISBN为十位，且不为空
                if (et_bookid.getText().length()!=10) {
                    Toast.makeText(admin_update_book.this,"请输入10位图书ISBN",Toast.LENGTH_SHORT).show();
                    testid=false;
                    break;
                }

                if(et_bookname.getText().length()==0){
                    Toast.makeText(admin_update_book.this,"请输入完整图书信息",Toast.LENGTH_SHORT).show();
                    testother=false;
                    break;
                }
                if(testid==true&&testother==true){
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", et_bookname.getText().toString());
                    values.put("writer", et_bookwriter.getText().toString());
                    values.put("bookid", et_bookid.getText().toString());
                    values.put("price", et_bookprice.getText().toString());
                    values.put("publicer", et_bookpublicer.getText().toString());
                    values.put("comment", et_bookcomment.getText().toString());
                    values.put("rank", et_bookrank.getText().toString());
                    values.put("type", et_booktype.getText().toString());
                    db.update("bookinfo", values, "_id=?", new String[]{String.valueOf(id)});
                    Toast.makeText(admin_update_book.this,"修改图书成功！",Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.bookimg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);  // 第二个参数是请求码
                break;

            case R.id.btn_bookback:
                Intent intentback=new Intent();
                intentback.setClass(admin_update_book.this,admin_select_bookinfo.class);
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
                is=admin_update_book.this.getContentResolver().openInputStream(uri);
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
        bookimg.setImageBitmap(bmp);
    }

    //    private void init() {
//        //返回按钮的事件监听
//        back_bt = (ImageButton) findViewById(R.id.addbook_back);
//        back_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(admin_add_book.this, admin_manager_book.class);
//                startActivity(intent);
//            }
//        });
////        name_ed = (EditText) findViewById(R.id.add_bokname);
////        author_Ed = (EditText) findViewById(R.id.add_bokauthor);
////        page_ed = (EditText) findViewById(R.id.add_bokpage);
////        price_ed = (EditText) findViewById(R.id.add_bokprice);
////        publish_ed = (EditText) findViewById(R.id.add_bokpublish);
//
//        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    ");
//        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
//        str    =    formatter.format(curDate);
//        //添加按钮的事件监听
//        add_book_bt = (Button) findViewById(R.id.add_book);
//        add_book_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String strname = name_ed.getText().toString();
////                String strauthor = author_Ed.getText().toString();
////                String strpage = page_ed.getText().toString();
////                String strprice = price_ed.getText().toString();
////                String strpublish = publish_ed.getText().toString();
//
//                //将字符串型转换成double类型
////                Double dprice = Double.parseDouble(strprice);
////                if (strname.equals("")) {
////                    Toast.makeText(admin_add_book.this, "名称不能为空，请重新输入",
////                            Toast.LENGTH_LONG).show();
////
////                } else if (strauthor.equals("")) {
////                    Toast.makeText(admin_add_book.this, "作者不能为空，请重新输入",
////                            Toast.LENGTH_LONG).show();
////
////                } else if ("".equals(dprice)) {
////                    Toast.makeText(admin_add_book.this, "价格不能为空，请重新输入",
////                            Toast.LENGTH_LONG).show();
////
////                } else if (strpage.equals("")) {
////                    Toast.makeText(admin_add_book.this, "页数不能为空，请重新输入",
////                            Toast.LENGTH_LONG).show();
////
////                } else if (strpublish.equals("")) {
////                    Toast.makeText(admin_add_book.this, "出版社不能为空，请重新输入",
////                            Toast.LENGTH_LONG).show();
////
////                } else {
////                    ContentValues values = new ContentValues();
////                    values.put("bookname", strname);
////                    values.put("author", strauthor);
////                    values.put("page", strpage);
////                    values.put("price", strprice);
////                    values.put("publish", strpublish);
////                    values.put("intime", str);
////                    databaseHelp helper = new databaseHelp(
////                            getApplicationContext());
//                    helper.insertbookinfo(values);
////                    Toast.makeText(admin_add_book.this, "图书添加成功",
////                            Toast.LENGTH_LONG).show();
////                    Intent intent = new Intent(admin_add_book.this,
////                            admin_manager_book.class);
////                    startActivity(intent);
////                    ActivityCollector.finishAll();
////                }
//            }
//        });
//    }
}
