package cdictv.twds.network;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cdictv.twds.App;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpApi {
    private static Handler mHandler=new Handler(Looper.getMainLooper());
    private static OkHttpClient sOkHttpClient=new OkHttpClient.Builder()
            .writeTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200,TimeUnit.SECONDS)
            .readTimeout(200,TimeUnit.SECONDS)
            .build();
    public  static  void requser(Request request,final  Mycall mycall){
        sOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mycall.filed(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str=response.body().string().trim();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(TextUtils.isEmpty(str)){
                             Toast.makeText(App.INSTANCE,"请求数据为空",Toast.LENGTH_LONG).show();
                        }else {
                            mycall.success(str);
                        }
                    }
                });
            }
        });
    }

}
