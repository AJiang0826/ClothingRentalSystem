package com.example.administrator.ClothingRentalSystem.admin.qiantai_admin;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.administrator.ClothingRentalSystem.R;

import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;
import com.google.android.material.navigation.NavigationView;

public class contentActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

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
                        Intent intent3 = new Intent(contentActivity.this, ReaderUpdateInfo.class);

                        startActivity(intent3);
                        break;
                    default:

                }
                drawerLayout.closeDrawers();//将滑动菜单关闭
                return true;
            }
        });
        insert();//插入数据往借书表中
        listView = (ListView) findViewById(R.id.list_view);
        databaseHelp help = new databaseHelp(getApplicationContext());

        Cursor cursor = help.querybookinfo();
        String from[] = {"img","name", "writer"};
        int to[] = {R.id.book_image,R.id.book_name, R.id.book_author};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.book_item, cursor, from, to);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.book_image) {
                    ImageView iconImageView = (ImageView) view;
                     iconImageView.setImageURI(Uri.parse(cursor.getString(columnIndex)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        listView.setAdapter(adapter);
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
