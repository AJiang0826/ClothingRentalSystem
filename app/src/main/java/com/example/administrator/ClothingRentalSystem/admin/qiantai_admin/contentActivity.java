package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.administrator.ClothingRentalSystem.R;

import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;
import com.google.android.material.navigation.NavigationView;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/*
服装信息展示界面
功能：用于展示租借系统的服装信息
     展示的服装包括服装图片 服装名称 服装类型 服装价格
     顶部设置侧边栏菜单，滑动进入菜单选项，包括修改个人信息，个人用户收藏信息，个人服装租借信息 退出等选项
     根据用户的选择跳转到相应的功能界面
*/
public class contentActivity extends AppCompatActivity implements View.OnClickListener {
    Uri uri;//图片路径
    private DrawerLayout drawerLayout;
    private ListView listView;
    private long mExitTime;
    private CountDownLatch countDownLatch;
    private ResultSet rs;
    String sql;
    int rows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Resources r = getResources();
        uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.click_white) + "/"
                + r.getResourceTypeName(R.drawable.click_white) + "/"
                + r.getResourceEntryName(R.drawable.click_white));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        listView = (ListView) findViewById(R.id.list_view);

        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        sql="select clothes_img,name,type,price from clothes_information";//查询整张表

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
            String[] names1={"clothes_img","name","type","price"};//建立字段名结果集
            String[] names2={"clothes_img","clothes_name","clothes_type","clothes_price"};//建立字段名结果集2 这个要和SimpleAdapter中的string[]一样
            List<Map<String, Object>> data = ItemUtils.getList(names1,names2,rs,contentActivity.this);//调用ItemUtils获取结果集
            System.out.println("list="+data.toString());
            SimpleAdapter adapter = new SimpleAdapter(
                    contentActivity.this, data, R.layout.content_item,
                    new String[]{"clothes_img","clothes_name","clothes_type","clothes_price"},
                    new int[]{R.id.clothes_img, R.id.clothes_name, R.id.clothes_type, R.id.clothes_price});//后两个String[] int[]数组都是borrow_item中的id
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        //侧滑菜单栏的选项
        navigationView.setCheckedItem(R.id.shoucang);//设置菜单项的默认选项
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shoucang:
                        Intent intent2 = new Intent(contentActivity.this, collectActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.exit:
                        finish();
                        break;
                    case R.id.jieyue:
                        //跳转到个人借书的页面
                        Intent intent = new Intent(contentActivity.this, person_borrow.class);
                        startActivity(intent);
                        break;
                    case R.id.updateInfo:
                        //跳转到个人借书的页面
                        Intent intent3 = new Intent(contentActivity.this, UserUpdateInfo.class);

                        startActivity(intent3);
                        break;
                    default:

                }
                drawerLayout.closeDrawers();//将滑动菜单关闭
                return true;
            }
        });
        insert();//插入数据往借书表中
    }

    private void insert() {

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                int i = position + 1;
//                点击1的item 提示点击了第几个item
//                setTitle("点击" + i + "的item");
                Intent intent = new Intent(contentActivity.this, borrowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //toolbar的菜单栏的选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Toast.makeText(contentActivity.this, "search", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(contentActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
