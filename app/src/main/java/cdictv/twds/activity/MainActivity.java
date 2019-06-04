package cdictv.twds.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cdictv.twds.R;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    private WebView webview;

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

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        webview = (WebView) findViewById(R.id.webview);
    }
}
