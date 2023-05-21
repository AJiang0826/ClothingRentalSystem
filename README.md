# 按照功能分析代码模块11

一、注册界面  activity_register.xml-----registerActivity.java------------------------------------------郑1

二、登录界面  activity_main.xml----MainActivity.java---------------------------------------------------璇1

1.普通用户登录：（目录：qiantai_main/）  

1）显示书本列表    activity-content.xml------contentActivity.java -----------------------------璇2

2）显示侧边栏 menu/nav_menu.xml-------contentActivity.java(导入)--------------------------------璇3

3）修改个人信息    activity_reader_update_info.xml-----ReaderUpdateInfo.java-------------------璇4

4）我的收藏      activity_collect.xml-----collectActivity.java--------------------------------璇5

5）借阅信息      activity_person_borrow.xml-----person_borrow.java----------------------------郑2

6）图书详细信息界面      activity_borrow.xml-----borrowActivity.java----------------------------郑3

2.管理员登录: （目录：houtai_admin)

1）管理员登录界面       activity_admin.xml-----./AdminActivity.java-----------------------------郑4

2）后台管理分式页面      activity_admin_content.xml-----admin_content.java----------------------郑5

3）查询信息分式页面      activity_admin_select_message.xml-----admin_select_message.java -------祝1

4）查看图书信息详细界面      activity_admin_select_bookinfo.xml-----admin_select_bookinfo.java---祝2

5）点击修改图书信息      activity_admin_upadte_book.xml-----admin_update_book.java---------------祝3

6）查看借阅信息详细界面        activity_admin_borrow_info.xml------admin_borrow_info.java--------祝4

7）查看还书信息详细界面        activity_admin_pay_info.xml-----admin_pay_info.java---------------祝5

8）管理图书分式页面      activity_admin_manager_book.xml-----admin_manager_book.java------------李1

9）添加图书详细界面      activity_add_book.xml-----admin_add_book.java--------------------------李2

10）查询图书搜索界面        activity_admin_search_book.xml-----admin_search_book.java-----------李3

11）查询图书详细界面     activity_admin_search_bookinfo.xml-----admin_search_bookinfo.java------李4

12）管理读者分式界面     activity_admin_manager_reader.xml-----admin_manager_reader.java--------李5

13）查找读者详细界面     activity_select_reader_admin.xml-----select_reader_admin.java----------王1

14）添加读者详细界面     activity_admin_add_reader.xml-----admin_add_reader.java----------------王2

15）编辑读者列表界面     activity_admin_editer_reader.xml-----admin_editer_reader.java----------王3

16）点击读者详细编辑界面       activity_admin_update_reader.xml-----admin_update_reader.java-----王4

17）删除读者列表界面     activity_admin_delete_reader.xml-----admin_delete_reader.java----------王5
