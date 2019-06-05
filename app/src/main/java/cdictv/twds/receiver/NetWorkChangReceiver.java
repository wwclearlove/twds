package cdictv.twds.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class NetWorkChangReceiver extends BroadcastReceiver {
    private boolean netWork = true;
    private AlertDialog.Builder bulider;
    private static AlertDialog alertDialog = null;
    private DialogInterface dialogInterface;

    public NetWorkChangReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {



        Log.i("onReceive","----网络连接-----");
        if(!isNetworkConnected(context)){
            Log.i("----network-----","网络连接");
            Toast.makeText(context,"没有网络连接",Toast.LENGTH_SHORT).show();

            //直接进入手机中的wifi网络设置界面
            bulider =new AlertDialog.Builder(context);
            bulider.setTitle("提示");
            bulider.setMessage("网络连接不可用，请检查网络设置");
            bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    // 打开设置界面
                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    netWork = false;
                }
            });
            bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                    dialogInterface = dialog;
                    dialogInterface.dismiss();
                    netWork = false;
                }
            });
            if(alertDialog == null && netWork){
                alertDialog =  bulider.create();
                alertDialog.show();
                netWork = false;
            }

        }else {
            if(alertDialog != null){
                alertDialog.dismiss();
            }
            netWork = false;
            return;
        }
    }


    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
