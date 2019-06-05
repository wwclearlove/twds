package cdictv.twds.network;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ShowOkhkhttpapi {
    public static void  show(String uri,Mycall mycall){
        RequestBody requestBody= new FormBody.Builder().build();
        Request request=new Request.Builder().url(uri).get()
                .build();
        OkhttpApi.requser(request,mycall);
    }
}
