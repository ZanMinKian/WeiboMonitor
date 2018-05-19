# WeiboMonitor
中文：屌丝微博监视器

输入微博ID即可“监视”某个微博博主

基于Jsoup，定时爬取某个微博返回的JSON，一有更新通过Notification提醒

点击通知栏提醒即可查看更新的微博和图片（无需安装浏览器）

Android方面，采用了双进程守活，确保App常驻后台不被系统杀死
