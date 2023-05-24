package com.example.administrator.ClothingRentalSystem.admin.houtai_admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.administrator.ClothingRentalSystem.R;
import com.example.administrator.ClothingRentalSystem.admin.ActivityCollector;
import com.example.administrator.ClothingRentalSystem.admin.qiantai_admin.BaseActivity;
import com.example.administrator.ClothingRentalSystem.admin.utils.DBUtils;
import com.example.administrator.ClothingRentalSystem.admin.utils.ItemUtils;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 修改用户信息的页面
 * 功能：1.通过搜索界面输入的用户名进行用户信息的查询和修改（用户名不可修改、其余修改需满足条件）
 *      2.显示所查用户信息到EditText
 *      3.重置文本框内容
 *      4.修改成功后提示修改成功，返回上一界面
 */

public class admin_update_user extends BaseActivity {

    private EditText username, password ,name, sex, phone;

    private Button update_bt,cz;//确认修改、重置按钮
    int id;
    private String struser,strpwd,strname,strsex,strphone,sql,names;//names上一界面搜索框中的值
    private ResultSet rs;
    private int rows;
    private CountDownLatch countDownLatch;//创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private ImageButton back_bt;//图片按钮--返回上一界面


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//控制页面不随着软键盘上移
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
        inut();//初始化界面
    }

    private void inut() {

        //返回--图片按钮监听
        back_bt = (ImageButton) findViewById(R.id._back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_update_user.this, admin_editer_user.class);
                startActivity(intent);
            }
        });

        //获取文本框输入的信息
        username = (EditText) findViewById(R.id.r_name);
        password = (EditText) findViewById(R.id.r_password);
        name = (EditText) findViewById(R.id.user_name);
        sex = (EditText)findViewById(R.id.r_sex);
        phone = (EditText)findViewById(R.id.r_phone);

        //获取上一界面所传值
        Bundle bundle=this.getIntent().getExtras();
        names=bundle.getString("name");

        //根据用户名查询出用户信息
        sql="select username,password,name,sex,phone from user where username='"+names+"';";
        System.out.println("sql="+sql);
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
            while (rs.next()) {

                //将所查用户信息显示再文本框中
                ((EditText) findViewById(R.id.r_name)).setText(rs.getString("username"));
                ((EditText) findViewById(R.id.r_password)).setText(rs.getString("password"));
                ((EditText) findViewById(R.id.user_name)).setText(rs.getString("name"));
                ((EditText) findViewById(R.id.r_phone)).setText(rs.getString("phone"));
                ((EditText) findViewById(R.id.r_sex)).setText(rs.getString("sex"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //修改按钮的事件监听
        update_bt = (Button) findViewById(R.id.r_register);
        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                struser = username.getText().toString();
                strpwd = password.getText().toString();
                strname = name.getText().toString();
                strsex = sex.getText().toString();
                strphone = phone.getText().toString();

                //对用户的信息进行验证，全部符合要求才能通过
                if (username.getText().length()<3) {
                    Toast.makeText(admin_update_user.this,"请输入账号不可小于3位",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.getText().length()!=11){
                    Toast.makeText(admin_update_user.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sex.equals("")||struser.equals("")){
                    Toast.makeText(admin_update_user.this,"请输入完整内容",Toast.LENGTH_SHORT).show();
                    return;
                }

                //修改信息
                sql="update user set username='"+struser+"',password='"+strpwd+"',name='"
                        +strname+"',sex='"+strsex+"',phone='"+strphone+"' where username='"+names+"'";
                System.out.println("update_sql"+sql);
                //以下开始数据库操作，使用线程，插入用户
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //成功创建普通用户
                            rows=DBUtils.getUpdateRows(sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //该线程执行完毕-1
                            countDownLatch.countDown();
                        }
                    }
                }).start();
                //等待线程插入完结果
                try {
                    countDownLatch.await();
                    Toast.makeText(admin_update_user.this,"用户："+names+"修改成功！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //修改成功后返回上一界面
                Intent intent = new Intent(admin_update_user.this, admin_editer_user.class);
                startActivity(intent);
            }
        });

        // 获取重置按钮并且添加事件
        cz=(Button) findViewById(R.id.r_register_resetting);
        cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //username.setText("");//用户名不可置空
                password.setText("");
                name.setText("");
                sex.setText("");
                phone.setText("");

            }
        });

    }

}
