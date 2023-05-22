package com.example.administrator.ClothingRentalSystem.admin.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 本类只是为了举例说明DBUtils类的使用方法，并不做任何其他作用。
 **/
public class DBTest {
    public DBTest()
    {
        System.out.println("查看数据库连接是否成立："+ (DBUtils.conn!=null));
        //System.out.println("conn="+);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //查询
                    ResultSet rs = DBUtils.getSelectResultSet("Select count(*) from customer");
                    rs.last();
                    System.out.println("举例使用ResultSet查询数据库中的customer表行数："+ (rs.getInt(1)));
                    //增删改同一写法
                    int rows=DBUtils.getUpdateRows("update customer set id=1 where id=2");
                    if (rows>0)
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
}
