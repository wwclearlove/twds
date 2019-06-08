package cdictv.twds.bean;

import com.google.gson.annotations.SerializedName;

public class JsonBean {
    /**
     * errcode : 200
     * errmsg : 成功
     * data : {"code":"123","name":"西餐教学演示室","logo":"/api/img/lablogo.png","admin":"陈东成","class":"烹饪1701班","teacher":"李成东","jiaoxueneirong":"宫爆鸡丁、西式点心拌菜","count":"40","yingdao":"32","shidao":"28","time":"2019-06-05 19:57:03","jiechi":"7-8节","xuenian":"2018-2019学年","xueqi":"第2学期","zhouchi":"第15周","xinqi":"星期三"}
     */

    public int errcode;
    public String errmsg;
    public DataBean data;

    public static class DataBean {
        /**
         * code : 123
         * name : 西餐教学演示室
         * logo : /api/img/lablogo.png
         * admin : 陈东成
         * class : 烹饪1701班
         * teacher : 李成东
         * jiaoxueneirong : 宫爆鸡丁、西式点心拌菜
         * count : 40
         * yingdao : 32
         * shidao : 28
         * time : 2019-06-05 19:57:03
         * jiechi : 7-8节
         * xuenian : 2018-2019学年
         * xueqi : 第2学期
         * zhouchi : 第15周
         * xinqi : 星期三
         */
        public String code;
        public String name;
        public String logo;
        public String admin;
        @SerializedName("class")
        public String classX;
        public String teacher;
        public String jiaoxueneirong;
        public String count;
        public String yingdao;
        public String shidao;
        public String time;
        public String jiechi;
        public String xuenian;
        public String xueqi;
        public String zhouchi;
        public String xinqi;
    }


}
