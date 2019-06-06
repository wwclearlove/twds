package cdictv.twds.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdictv.twds.R;
import cdictv.twds.bean.JsonBean;
import cdictv.twds.bean.MenuBean;
import cdictv.twds.bean.MenuDataBean;
import cdictv.twds.network.Mycall;
import cdictv.twds.network.ShowOkhkhttpapi;
import cdictv.twds.receiver.NetWorkChangReceiver;
import cdictv.twds.util.DeviceUtils;
import cdictv.twds.util.Sputils;

public class MainActivity extends BaseActivity  {
    private ImageView logoImg;
    private TextView addressname;
    private TextView adminname;
    private TextView classTeach;
    private TextView teachNum;
    private TextView timeClass;
    private TextView dateStudent;
    private Button cancel;
    private Button save;
    private Button ture;
    private Button quxiao;
    private EditText ed_password;
    private EditText ed_port;
    private EditText ed_ip;
    private ImageView set;
    private TextView tc;
    private TextView id;
    private TextView djs;
    private WebView webview;
    private TextView xlPopip;


    private boolean flag;
    private Context mContext;
    List<String> menulist = new ArrayList<>();
    List<String> urllist = new ArrayList<>();
    ListPopupWindow mListPop;


    private int time=180;
    private ProgressDialog progressDialog;
    String uri;
    String bhuri;
    private String mAndroidID;
    public Handler mHandler = new Handler();
    public Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if(time>0){
                Log.e("time", "run: "+time);
                Log.d("bhuri",bhuri);
                if(time<=31&&!bhuri.equals("http://ming.cdivtc.edu.cn/?id="+mAndroidID)){
                    djs.setVisibility(View.VISIBLE);
                    djs.setText(time+"");
                    if(time==1){
                        webview.loadUrl("http://ming.cdivtc.edu.cn/?id=" + mAndroidID);
                        djs.setVisibility(View.GONE);
                        time=35;
                    }
                }else {
                    djs.setVisibility(View.GONE);
                    mHandler.removeCallbacks(sRunnable);
                }
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    //http://ming.cdivtc.edu.cn/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        initView();
        List<MenuDataBean> dataList= Sputils.getDataList("menu",  MenuDataBean[].class);
        if(super.isFlag()){
            initMeunData();
        }else if(dataList.size() != 0){
            try {
                for(MenuDataBean dataBean:dataList){
                    Log.i("MenuDataBean", "initListPopWindow: "+dataBean.name);
                    menulist.add(dataBean.name);
                    urllist.add(dataBean.url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        initdata();
        initWeb();
        initlistener();
        initListPopWindow();

        //点击的下拉菜单
        xlPopip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initPopWindow(v);
                mListPop.show();
            }
        });
    }

    private void initMeunData() {
        //初始化下拉菜单的值
        ShowOkhkhttpapi.show("http://ming.cdivtc.edu.cn/api/getmenu.aspx?code=1", new Mycall() {
            @Override
            public void success(String json) {
                Gson gson = new Gson();
                MenuBean menuBean = null;
                try {
                    menuBean = gson.fromJson(json, MenuBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                Log.i("menuBean", "success: "+menuBean.errcode);
                for(MenuBean.DataBean dataBean : menuBean.data){
                    menulist.add(dataBean.name);
                    urllist.add(dataBean.url);
                }
                if(menuBean.errcode == 200){
                    Sputils.setDataList("menu",menuBean.data);

                }
            }

            @Override
            public void filed(String msg) {

            }
        });
    }

    private void initdata() {
        ShowOkhkhttpapi.show("http://ming.cdivtc.edu.cn/api/getlabrun.aspx?code=123", new Mycall() {
            @Override
            public void success(String json) {
                Log.i("json",json);
                Gson gson=new Gson();
                JsonBean newsBean= null;
                try {
                    newsBean = gson.fromJson(json,JsonBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                JsonBean.DataBean data = newsBean.data;
                Log.d("json", "success: "+data.admin);
                String[] words = data.time.split(" ");
                addressname.setText(data.name+"");
                adminname.setText("管理员:"+data.admin);
                classTeach.setText("班级:"+data.classX+"\n教师"+data.teacher);
                teachNum.setText("教学内容:"+data.jiaoxueneirong+"\n实训人数:"+
                        data.count+"   应到:"+data.yingdao+"   实到:"+data.shidao);
                timeClass.setText("   "+words[0]+" \n   "+words[1]+" "+data.jiechi);
                dateStudent.setText(data.xuenian+" "+data.xueqi+" \n         "+data.zhouchi+"  "+data.xinqi);
            }

            @Override
            public void filed(String msg) {

            }
        });


    }

    private void initlistener() {
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = View.inflate(MainActivity.this, R.layout.set_password, null);
                ture = view1.findViewById(R.id.ture);
                quxiao = view1.findViewById(R.id.quxiao);
                ed_password = view1.findViewById(R.id.ed_password);
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setView(view1).show();
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                ture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = ed_password.getText().toString().trim();
                        if (password.equals("u@13438487878")) {
                            alertDialog.dismiss();
                            View inflate = View.inflate(MainActivity.this, R.layout.set_ip_dialog, null);
                            cancel = inflate.findViewById(R.id.cancel);
                            save = inflate.findViewById(R.id.save);
                            ed_port = inflate.findViewById(R.id.ed_port);
                            ed_ip = inflate.findViewById(R.id.ed_ip);
                            id = inflate.findViewById(R.id.android);
                            tc = inflate.findViewById(R.id.tc);
                            id.setText("当前设备ID:"+mAndroidID);
                            if (Sputils.getString("ip").isEmpty() || Sputils.getString("port").isEmpty()) {

                            } else {
                                ed_port.setText(Sputils.getString("port"));
                                ed_ip.setText(Sputils.getString("ip"));
                            }
                            final AlertDialog alertDialog2 = new AlertDialog.Builder(MainActivity.this).setView(inflate).show();
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog2.dismiss();
                                }
                            });
                            tc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog2.dismiss();
                                    finish();
                                }
                            });
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog2.dismiss();
                                    Sputils.putString("ip", ed_ip.getText().toString().trim());
                                    Sputils.putString("port", ed_port.getText().toString().trim());
                                    panduan();
                                }
                            });
                        }else {
                             Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                            ed_password.setText("");
                        }
                    }
                });

            }
        });
    }

    private void initWeb() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);

                Log.e("bh", "shouldOverrideUrlLoading: " + url);
                mHandler.removeCallbacks(sRunnable);
                time=35;
                mHandler.postDelayed(sRunnable, 1000);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress("页面加载中");//开始加载动画
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                bhuri=url;
                Log.d("bhurl", "onPageFinished: "+url);
                removeProgress();//当加载结束时移除动画
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
//                view.loadUrl("about:blank");// 避免出现默认的错误界面
                Toast.makeText(getApplicationContext(), "ip地址或者端口错误,马上默认跳转首页", Toast.LENGTH_SHORT).show();
                view.loadUrl("http://ming.cdivtc.edu.cn/?id=" + mAndroidID);// 加载自定义错误页面
            }


        });
        panduan();
    }

    private void panduan() {
        if (Sputils.getString("ip").isEmpty()) {
            uri = "http://ming.cdivtc.edu.cn/?id=" + mAndroidID;
        } else if (judgeContainsStr(Sputils.getString("ip"))) {
            uri = "http://" + Sputils.getString("ip") + "/?id=" + mAndroidID;
        } else {
            uri = "http://" + Sputils.getString("ip") + ":" + Sputils.getString("port") + "/oupi/?id=" + mAndroidID;
        }
        Log.e("uri", uri);
        webview.loadUrl(uri);
        Log.e("id", "onCreate: " + mAndroidID);
    }

    private void initView() {


        logoImg = (ImageView) findViewById(R.id.logo_img);
        addressname = (TextView) findViewById(R.id.addressname);
        adminname = (TextView) findViewById(R.id.adminname);
        classTeach = (TextView) findViewById(R.id.class_teach);
        teachNum = (TextView) findViewById(R.id.teach_num);
        timeClass = (TextView) findViewById(R.id.time_class);
        dateStudent = (TextView) findViewById(R.id.date_student);

        set = (ImageView) findViewById(R.id.set);
        djs = (TextView) findViewById(R.id.djs);
        webview = (WebView) findViewById(R.id.webview);
        mAndroidID = DeviceUtils.getAndroidID(MainActivity.this);
        xlPopip = (TextView) findViewById(R.id.xl_popip);
    }

    //-----显示ProgressDialog
    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);//设置点击不消失
        }
        if (progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    //------取消ProgressDialog
    public void removeProgress() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public boolean judgeContainsStr(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }




    void initListPopWindow(){


       List<MenuDataBean> dataList= Sputils.getDataList("menu",  MenuDataBean[].class);

        try {
            for(MenuDataBean dataBean:dataList){
                Log.i("MenuDataBean", "initListPopWindow: "+dataBean.name);
                if(menulist.size() == 0 && urllist.size() == 0){
                    menulist.add(dataBean.name);
                    urllist.add(dataBean.url);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mListPop = new ListPopupWindow(MainActivity.this);


        mListPop.setAdapter(new ArrayAdapter<String>(MainActivity.this,R.layout.item_popip,R.id.pop_text,menulist));
        mListPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mListPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setAnchorView(xlPopip);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);//设置是否是模式
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                Log.i("onItemClick", "onItemClick: "+"http://ming.cdivtc.edu.cn"+urllist.get(position)+"/?id=" + mAndroidID);

                webview.loadUrl("http://ming.cdivtc.edu.cn"+urllist.get(position)+"?id=" + mAndroidID);
                //Toast.makeText(MainActivity.this,urllist.get(position),Toast.LENGTH_SHORT).show();
                mListPop.dismiss();
            }
        });

    }


}


