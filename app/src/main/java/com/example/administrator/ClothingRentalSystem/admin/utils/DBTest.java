package com.example.administrator.ClothingRentalSystem.admin.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 本类只是为了举例说明DBUtils类的使用方法，并不做任何其他作用。
 **/
public class DBTest {
    public DBTest() {
        System.out.println("查看数据库连接是否成立：" + (DBUtils.conn != null));
        //System.out.println("conn="+);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //查询
                    ResultSet rs = DBUtils.getSelectResultSet("Select count(*) from customer");
                    rs.last();
                    System.out.println("举例使用ResultSet查询数据库中的customer表行数：" + (rs.getInt(1)));
                    //增删改同一写法
                    int rows = DBUtils.getUpdateRows("update customer set id=1 where id=2");
                    if (rows > 0)
                        System.out.println("增删改完成");
                    else
                        System.out.println("sql语句有问题，请修改后再继续。");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    /**
     * 本方法只是演示更复杂情况的，调用DBUtils的使用方法
     * 例：需要等到线程完成后，才可以继续后续的内容操作，且后续内容操作不可以在线程中完成（如将输入栏置空）
     * 最好是将CountDownLatch建成全局变量，在方法中再修改个数，有需要还可以再次设置个数
     **/

    //创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
    private CountDownLatch countDownLatch;
    private int rows;
    private String sql="insert into user values(xxxxxxxxx)";
    public DBTest(int a)
    {
        countDownLatch = new CountDownLatch(1);//创建线程计时器个数是1
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
        //等待线程插入完结果，跳转页面或者
        try {
            countDownLatch.await();//阻塞等待线程执行完毕
            //if (rows>0){
                //用户注册成功，就跳转到登录页面 因有错故注释掉
                //Intent intent = new Intent(registerActivity.this, MainActivity.class);
                //startActivity(intent);
            //}
            //else
                //Toast.makeText(registerActivity.this,"注册失败，请联系管理员！",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
