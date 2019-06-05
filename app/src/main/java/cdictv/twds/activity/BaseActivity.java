package cdictv.twds.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cdictv.twds.receiver.NetWorkChangReceiver;

public class BaseActivity extends AppCompatActivity {

    private boolean isReistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播监听网络
        netWorkChangReceiver = new NetWorkChangReceiver();
        filter = new IntentFilter();
        //filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver,filter);
        isReistered = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isReistered){
            unregisterReceiver(netWorkChangReceiver);
        }
    }
}
