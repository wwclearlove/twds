package cdictv.twds.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cdictv.twds.R;
import cdictv.twds.receiver.NetWorkChangReceiver;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    private WebView webview;
    //判断当前的是否注册
    private boolean isReistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;
    private IntentFilter filter;

    //http://ming.cdivtc.edu.cn/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("http://ming.cdivtc.edu.cn");


    }

    @Override
    protected void onStart() {
        super.onStart();
        //判断当前网络的状态
        netWorkChangReceiver = new NetWorkChangReceiver();
        filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver,filter);
        isReistered = true;
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        webview = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**注销 unregisterReceiver()，否则可能引起内存泄露。*/
        if(isReistered){
            unregisterReceiver(netWorkChangReceiver);
        }
    }
}
