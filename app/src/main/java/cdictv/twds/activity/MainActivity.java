package cdictv.twds.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cdictv.twds.R;
import cdictv.twds.util.DeviceUtils;
import cdictv.twds.util.Sputils;

public class MainActivity extends AppCompatActivity {
    private Button cancel;
    private Button save;
    private EditText ed_port;
    private EditText ed_ip;
    private TextView set;
    private WebView webview;
    private ProgressDialog progressDialog;
    String uri;
    private String mAndroidID;
    public  Handler mHandler=new Handler();
    public  Runnable sRunnable=new Runnable() {
        @Override
        public void run() {

            webview.loadUrl("http://ming.cdivtc.edu.cn/?id="+mAndroidID);
        }
    };
    //http://ming.cdivtc.edu.cn/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
       initWeb();
       initlistener();
    }

    private void initlistener() {
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflate = View.inflate(MainActivity.this, R.layout.set_ip_dialog, null);
                cancel=inflate.findViewById(R.id.cancel);
                save=inflate.findViewById(R.id.save);
                ed_port=inflate.findViewById(R.id.ed_port);
                ed_ip=inflate.findViewById(R.id.ed_ip);
                if(Sputils.getString("ip").isEmpty()||Sputils.getString("port").isEmpty()){

                }else {
                    ed_port.setText(Sputils.getString("port"));
                    ed_ip.setText(Sputils.getString("ip"));
                }
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setView(inflate).show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Sputils.putString("ip",ed_ip.getText().toString().trim());
                        Sputils.putString("port",ed_port.getText().toString().trim());
                        panduan();
                    }
                });
            }
        });
    }

    private void initWeb() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                Log.e("bh", "shouldOverrideUrlLoading: "+url );
                mHandler.postDelayed(sRunnable,5000);
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
                 Toast.makeText(getApplicationContext(),"ip地址或者端口错误,马上默认跳转首页",Toast.LENGTH_SHORT).show();
                view.loadUrl("http://ming.cdivtc.edu.cn/?id="+mAndroidID);// 加载自定义错误页面
            }




        });
       panduan();
    }

    private void panduan() {
        if(Sputils.getString("ip").isEmpty()){
            uri="http://ming.cdivtc.edu.cn/?id="+mAndroidID;
        }else {
            uri="http://"+Sputils.getString("ip")+":"+Sputils.getString("port")+"/oupi/?id="+mAndroidID;
        }
        Log.e("uri",uri);
        webview.loadUrl(uri);
        Log.e("id", "onCreate: "+ mAndroidID  );
    }

    private void initView() {
        set = (TextView) findViewById(R.id.set);
        webview = (WebView) findViewById(R.id.webview);
        mAndroidID = DeviceUtils.getAndroidID(MainActivity.this);
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
    public void removeProgress(){
        if (progressDialog==null){
            return;
        }
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }


}
