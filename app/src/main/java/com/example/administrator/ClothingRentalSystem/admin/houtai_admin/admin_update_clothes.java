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
 * 管理员修改衣服的界面
 */

public class admin_update_clothes extends BaseActivity implements View.OnClickListener {
    private ImageButton back_bt;
    private Button add_book_bt;
    private String str;
    private int id;
    private databaseHelp helper;
    Uri uri;
    private ImageView ClothesImg;
    private EditText Et_ClothesId,Et_ClothesName,Et_ClotheStype,Et_ClothesWriter,Et_ClothesPublicer,Et_ClothesPrice,Et_ClothesRank,Et_ClothesComment;
    private Button Btn_ClothesCommit,Btn_ClothesBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upadte_clothes);

        initdata();//界面初始化
        helper = new databaseHelp(getApplicationContext());
    }

    private void initdata() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id")+1 ;
        Log.i("cursor", "initdata: " + id);

        ClothesImg=findViewById(R.id.ClothesImg);
        Et_ClothesId=findViewById(R.id.et_ClothesId);
        Et_ClothesName=findViewById(R.id.et_ClothesName);
        Et_ClotheStype=findViewById(R.id.et_ClotheStype);
        Et_ClothesWriter=findViewById(R.id.et_ClothesWriter);
        Et_ClothesPublicer=findViewById(R.id.et_ClothesPublicer);
        Et_ClothesPrice=findViewById(R.id.et_ClothesPrice);
        Et_ClothesRank=findViewById(R.id.et_ClothesRank);
        Et_ClothesComment=findViewById(R.id.et_ClothesComment);


        Btn_ClothesCommit=findViewById(R.id.Btn_ClothesCommit);

        final databaseHelp help = new databaseHelp(getApplicationContext());
        Cursor cursor = help.querybookinfoid(id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ClothesImg.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex("img"))));
            Et_ClothesId.setText(cursor.getString(cursor.getColumnIndex("bookid")));
            Et_ClothesName.setText(cursor.getString(cursor.getColumnIndex("name")));
            Et_ClothesWriter.setText(cursor.getString(cursor.getColumnIndex("writer")));
            Et_ClotheStype.setText(cursor.getString(cursor.getColumnIndex("type")));
            Et_ClothesPrice.setText(cursor.getString(cursor.getColumnIndex("price")));
            Et_ClothesPublicer.setText(cursor.getString(cursor.getColumnIndex("publicer")));
            Et_ClothesComment.setText(cursor.getString(cursor.getColumnIndex("comment")));
           Et_ClothesRank.setText(cursor.getString(cursor.getColumnIndex("rank")));
        }

        Btn_ClothesCommit.setOnClickListener(this);
        ClothesImg.setOnClickListener(this);
//        btn_clothesback.setOnClickListener(this);

    }
    //对管理员输入的衣服信息进行验证，全部符合要求才能通过
    boolean testid=true,testother=true;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.Btn_ClothesCommit:
                //ISBN为十位，且不为空
                if ( Et_ClothesId.getText().length()!=10) {
                    Toast.makeText(admin_update_clothes.this,"请输入10位衣服编码",Toast.LENGTH_SHORT).show();
                    testid=false;
                    break;
                }

                if(Et_ClothesName.getText().length()==0){
                    Toast.makeText(admin_update_clothes.this,"请输入完整衣服信息",Toast.LENGTH_SHORT).show();
                    testother=false;
                    break;
                }
                if(testid==true&&testother==true){
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", Et_ClothesName.getText().toString());
                    values.put("writer", Et_ClothesWriter.getText().toString());
                    values.put("bookid", Et_ClothesId.getText().toString());
                    values.put("price", Et_ClothesPrice.getText().toString());
                    values.put("publicer", Et_ClothesPublicer.getText().toString());
                    values.put("comment", Et_ClothesComment.getText().toString());
                    values.put("rank", Et_ClothesRank.getText().toString());
                    values.put("type",Et_ClotheStype.getText().toString());
                    db.update("bookinfo", values, "_id=?", new String[]{String.valueOf(id)});
                    Toast.makeText(admin_update_clothes.this,"修改衣服信息成功！",Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.ClothesImg:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);  // 第二个参数是请求码
                break;


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
                is= admin_update_clothes.this.getContentResolver().openInputStream(uri);
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
