# 按照功能分析代码模块11

一、注册界面                  activity_register.xml------------------------registerActivity.java-----------------------------郑伊芳【1】

二、登录界面                  activity_main.xml----------------------------MainActivity.java---------------------------------徐璇【1】

1.普通用户登录：（目录：qiantai_main/）  

1）显示衣服列表                activity-content.xml------------------------contentActivity.java -----------------------------徐璇【2】

2）显示侧边栏                  menu/nav_menu.xml/Activitycontent.xml-------contentActivity.java(导入)------------------------徐璇【3】

3）修改个人信息                 activity_user_update_info.xml---------------UserUpdateInfo.java------------------------------徐璇【4】

4）我的收藏                    activity_collect.xml------------------------collectActivity.java-----------------------------徐璇【5】

5）租借信息                    activity_person_borrow.xml------------------person_borrow.java-------------------------------郑伊芳【2】

6）衣服详细信息界面              activity_borrow.xml-------------------------borrowActivity.java------------------------------郑伊芳【3】

2.管理员登录: （目录：houtai_admin)

1）管理员登录界面               activity_admin.xml--------------------------./AdminActivity.java-----------------------------郑伊芳【4】

2）后台管理分式页面              activity_admin_content.xml------------------admin_content.java-------------------------------郑伊芳【5】

3）查询信息分式页面              activity_admin_select_message.xml-----------admin_select_message.java -----------------------祝保玉【1】

4）查看衣服信息详细界面           activity_admin_select_clothesinfo.xml-------admin_select_clothesinfo.java--------------------祝保玉【2】

5）点击修改衣服信息              activity_admin_upadte_clothes.xml-----------admin_update_clothes.java------------------------祝保玉【3】

6）查看租借衣服信息详细界面        activity_admin_borrow_info.xml--------------admin_borrow_info.java---------------------------祝保玉【4】

7）查看归还衣服信息详细界面        activity_admin_pay_info.xml-----------------admin_pay_info.java------------------------------祝保玉【5】

8）管理衣服分式页面              activity_admin_manager_clothes.xml----------admin_manager_clothes.java-----------------------李雅倩【1】

9）添加衣服详细界面              activity_add_clothes.xml--------------------admin_add_clothes.java---------------------------李雅倩【2】

10）查询衣服搜索界面             activity_admin_search_clothes.xml-----------admin_search_clothes.java------------------------李雅倩【3】

11）查询衣服详细界面             activity_admin_search_clothesinfo.xml-------admin_search_clothesinfo.java--------------------李雅倩【4】

12）管理用户分式界面             activity_admin_manager_user.xml-------------admin_manager_user.java--------------------------李雅倩【5】

13）查找用户详细界面             activity_select_user_admin.xml--------------select_user_admin.java---------------------------王晓慧【1】

14）添加用户详细界面             activity_admin_add_user.xml-----------------admin_add_user.java------------------------------王晓慧【2】

15）编辑用户列表界面             activity_admin_editer_user.xml--------------admin_editer_user.java---------------------------王晓慧【3】

16）点击用户详细编辑界面          activity_admin_update_user.xml--------------admin_update_user.java---------------------------王晓慧4】

17）删除用户列表界面             activity_admin_delete_user.xml--------------admin_delete_user.java---------------------------王晓慧【5】

18）删除用户搜索界面             activity_admin_delete_userinfo.xml----------admin_delete_userinfo.java-----------------------王晓慧【6】

19）添加用户搜索界面             activity_admin_add_userinfo.xml-------------admin_add_userinfo.java--------------------------王晓慧【7】

使用DBUtils工具类说明： （首先，因为连接数据库使用了一点网络编程，所以在AndroidManifest.xml文件中，插入了新的网络权限，所以一定要将虚拟机上的app卸载重装。）

在主类MainActivity我已经使用静态代码块使得连接一直存在在内存中。所以之后每次调用都不需要重新初始化即调用构造方法。

在使用DBUtils的时候，需要使用线程来控制。可直接复制以下代码，将相关内容写在run()方法中即可。也可查看utils文件夹下的DBtest类，模仿写法。

new Thread(new Runnable(){
@Override
public void run() {
//请求详情
}).start();
