package com.example.administrator.ClothingRentalSystem.admin.utils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类是可对ListView引入的数据结构，调用后可直接生成通过数据库内容相应的数据结构
 * 具体传递的参数为：字段名数组String[],第二个数组同SimpleAdapter，结果集ResultSet
 * 通过调用get方法可直接返回
 **/
public class ItemUtils {
    private static List<Map<String, Object>> list;

    public static List<Map<String, Object>> getList(String[] names1,String[] names2, ResultSet rs)
    {
        if (rs==null) {
            System.out.println("传递的结果集为空！请检查修改后再进行下一步操作！");
            return null;
        }
        if(names1==null||names2==null) {
            System.out.println("传递的字段名数组为空！请检查修改后再进行下一步操作！");
            return null;
        }
        list = new ArrayList<>();
        try{
            if (rs.isBeforeFirst())
            {
                while(rs.next())
                {
                    System.out.println("开始创建map");
                    Map<String, Object> map = new HashMap<String, Object>();
                    for(int i=0;i<names2.length;i++)
                    {
                        map.put(names2[i],rs.getString(names1[i]));
                    }
                    list.add(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
