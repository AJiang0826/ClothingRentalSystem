package com.example.administrator.ClothingRentalSystem.admin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 使用的数据库是MySQL
 * 连接数据库工具是JDBC
 * 本类是工具类，目的是简化连接数据库、查询、插入、更新、删除数据的步骤
 * 继承本类后，调用构造器即可迅速完成数据库的连接，通过调用方法即可完成增删改数据操作
 * 可直接访问本类中的对象，来完成操作
 */
public class DBUtils{
    public static Connection conn;
    public static ResultSet rs;
    public static Statement st;

    /**
     *本方法用于连接本地数据库，参数为本地数据库名，端口号，登陆的账号和密码
     */
    public DBUtils(int port,String LocalDBname,String username,String password){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.56.1:"+port+"/"+LocalDBname+"?characterEncoding=UTF-8&&serverTimezone=GMT",username, password);		//获取数据库连接
            st=conn.createStatement();
        }catch (ClassNotFoundException e) {
            System.out.println("未能成功加载驱动程序，请检查是否导入驱动程序！");
            //e.printStackTrace();
            //System.out.println("连接成功");
        }catch(SQLException e){
            e.printStackTrace(System.out);
            System.out.println("连接失败！");
        }
        //return conn;
    }

    /**
     *本方法用于连接远程数据库，参数为远程URL(包含了IP和端口号)，远程数据库名，登陆的账号和密码
     */
    public DBUtils(String RemoteURL,String DBname,String username,String password) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+RemoteURL+"/"+DBname+"?characterEncoding=UTF-8&&serverTimezone=GMT",username, password);		//获取数据库连接
            st=conn.createStatement();
        }catch (ClassNotFoundException e) {
            System.out.println("未能成功加载驱动程序，请检查是否导入驱动程序！");
            e.printStackTrace();
            System.out.println("连接成功");
        }catch(SQLException e){
            e.printStackTrace(System.out);
            System.out.println("连接失败！");
        }
        //return conn;
    }

    /**
     * 本方法用来 查询 并获得相关结果集，参数为sql语句。
     * 通过返回的rs来进行后续的操作。
     * 若返回为空，则sql语句有误。
     **/
    public static ResultSet getSelectResultSet(String sql)
    {
        try{
            if (st==null)
                System.out.println("st是空！！！");
            rs=st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }

    /**
     * 本方法用来 插入、修改、删除 并获得返回结果影响行数，参数为sql语句
     * 若返回值=0则是sql语句有误。
     * 返回值>0则是正确执行sql语句并返回影响行数。
     **/
    public static int getUpdateRows(String sql)
    {
        int countsRows=0;
        try{
            countsRows=st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return countsRows;
    }

    public static void close() throws Exception{
        conn.close();
    }
}

