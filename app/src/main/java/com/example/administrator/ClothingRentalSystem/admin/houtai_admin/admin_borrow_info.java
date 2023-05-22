package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.databaseHelp;

import java.util.List;
import java.util.Map;

public class admin_borrow_info extends AppCompatActivity {
private ListView ad_borrow;
private ImageButton back_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_borrow_info);
        ad_borrow=(ListView)findViewById(R.id.ad_show_borrow);
        databaseHelp help=new databaseHelp(getApplicationContext());
        List<Map<String, Object>> data = help.queryborrow();
        SimpleAdapter adapter = new SimpleAdapter(
                admin_borrow_info.this, data, R.layout.ad_borrow_item,
                new String[] {  "_Bid","Borname","Bookid", "bookname", "nowtime" },
                new int[] { R.id.ad_bid, R.id.ad_borname,
                        R.id.ad_bbid, R.id.ad_bname,
                        R.id.ad_btime });
        ad_borrow.setAdapter(adapter);

    }
}
