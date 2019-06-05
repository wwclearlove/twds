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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdictv.twds.R;
import cdictv.twds.util.DeviceUtils;
import cdictv.twds.util.Sputils;

public class MainActivity extends BaseActivity {
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


    private Context mContext;


    private int time=180;
    private ProgressDialog progressDialog;
    String uri;
    private String mAndroidID;
    public Handler mHandler = new Handler();
    public Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
//
            time--;
            if(time>0){

                Log.e("time", "run: "+time);
                if(time<=31){
                    djs.setVisibility(View.VISIBLE);
                    djs.setText(time+"");
                    if(time==1){
                        webview.loadUrl("http://ming.cdivtc.edu.cn/?id=" + mAndroidID);
                        djs.setVisibility(View.GONE);
                        time=300;
                    }
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
        initWeb();
        initlistener();
        xlPopip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initPopWindow(v);
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
                time=180;
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

    




    //点击下拉列表
//    private void initPopWindow(View v) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popip, null, false);
//        final TextView shouye = (TextView) view.findViewById(R.id.web_index);
//        final TextView sys = (TextView) view.findViewById(R.id.web_sys);
//        final TextView yx = (TextView) view.findViewById(R.id.web_yx);
//        final TextView kq = (TextView) view.findViewById(R.id.web_kq);
//        final TextView xx = (TextView) view.findViewById(R.id.web_xx);
//        final TextView bz = (TextView) view.findViewById(R.id.web_bz);
//        final TextView ws = (TextView) view.findViewById(R.id.web_ws);
//        final TextView syaq = (TextView) view.findViewById(R.id.web_syaq);
//        final TextView news = (TextView) view.findViewById(R.id.web_news);
//        final TextView ss = (TextView) view.findViewById(R.id.web_ss);
//        //1.构造一个PopupWindow，参数依次是加载的View，宽高
//        final PopupWindow popWindow = new PopupWindow(view,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//
//        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
//
//        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
//        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
//        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
//        popWindow.setTouchable(true);
//        popWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
//        popWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0000000")));    //要为popWindow设置一个背景才有效
//
//        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
//        popWindow.showAsDropDown(v, -10, 10);
//
//        //设置popupWindow里的按钮的事件
//        shouye.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, shouye.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        sys.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, sys.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        yx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        kq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, kq.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        xx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        bz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        ws.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        syaq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        news.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        ss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, yx.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}

