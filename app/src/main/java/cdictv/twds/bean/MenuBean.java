package cdictv.twds.bean;

import java.util.List;

public class MenuBean {

    /**
     * errcode : 200
     * errmsg : 成功
     * data : [{"name":"返回首页","url":"/view/index.html"},{"name":"实验室介绍","url":"/view/lab.html"},{"name":"运行情况","url":"/view/labrun.html"},{"name":"实验课表","url":"/view/course.html"},{"name":"考勤信息","url":"/view/kq.html"},{"name":"信息查询","url":"/view/search.html"},{"name":"原料订单","url":"/view/dingd.html"},{"name":"增补原料","url":"/view/function.html"},{"name":"卫生情况","url":"/view/weis.html"},{"name":"实验安全","url":"/view/aq.html"},{"name":"新闻通知","url":"/view/news.html"},{"name":"师生作品","url":"/view/detail.html"}]
     */

    public int errcode;
    public String errmsg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * name : 返回首页
         * url : /view/index.html
         */

        public String name;
        public String url;
    }
}
