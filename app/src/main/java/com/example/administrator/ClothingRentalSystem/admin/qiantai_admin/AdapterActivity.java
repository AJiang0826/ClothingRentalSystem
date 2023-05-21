package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ClothingRentalSystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterActivity extends AppCompatActivity {
    private Button back;

    ArrayAdapter<String> adapter;   //适配器
    List<String> contactsList= new ArrayList<>(); //列表读取适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdapterActivity.this,borrowActivity.class);
                startActivity(intent);
                finish();
            }
        });



        ListView contactsView = (ListView) findViewById(R.id.friend);  //声明定义列表
        adapter = new ArrayAdapter<String>(AdapterActivity.this, android.R.layout.simple_list_item_1,contactsList);
        contactsView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(AdapterActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //调用系统通讯录
            ActivityCompat.requestPermissions(AdapterActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
            //系统系统通讯录授权第一次进行授权 ，询问是否授权
        } else{
            readCotacts();
            //授权成功过后，每次打开自动读取通讯录
        }


        ListView listView = findViewById(R.id.friend);
        listView.setAdapter(adapter);
//点击事件添加
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                   default:
                       Toast.makeText(AdapterActivity.this,"分享成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //读取通讯录
    private  void  readCotacts(){
        Cursor cursor = null;   //先置为空值
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if (cursor != null){  //当游标不为空
                while (cursor.moveToNext()){
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //读取电话簿名字
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //读取电话簿电话
                    contactsList.add(displayName+"\n"+number);
                    //输出名字和电话列表
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){   //当游标不为空
                cursor.close();  //结束
            }
        }
    }

    //申请权限
    public  void onRequestPermissionsResult(int requestCode , String[] permissions, int[] grantResults){
        switch (requestCode){ //请求授权信息
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //收到授权
                    readCotacts();
                    Toast.makeText(AdapterActivity.this,"成功授权",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(AdapterActivity.this,"你拒绝授权，无法使用通讯录",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}

