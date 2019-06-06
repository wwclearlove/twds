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
    private Message message;

    public NetWorkChangReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i("onReceive","----网络连接-----");
        if(!isNetworkConnected(context)){
            try {
                message.getMsg(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("----network-----","网络连接");
            Toast.makeText(context,"没有网络连接", Toast.LENGTH_SHORT).show();

            //直接进入手机中的wifi网络设置界面
            bulider =new AlertDialog.Builder(context);
            bulider.setTitle("提示");
            bulider.setMessage("网络连接不可用，请检查网络设置");
            bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    // 打开设置界面
                    Intent intent=new Intent(Settings.ACTION_SETTINGS);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    netWork = true;
                }
            });
            bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    netWork = true;
                }
            });
            if(netWork){
                alertDialog =  bulider.create();
//                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                try {
                    alertDialog.show();
                } catch (Exception e) {
                    Log.i("alertDialog", "onReceive: "+e.getMessage());
                    e.printStackTrace();
                }
                netWork = false;
            }

        }else {
            try {
                message.getMsg(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(alertDialog != null){
//                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.dismiss();
            }
            netWork = true;
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
    public interface  Message{
        public void getMsg(boolean flag);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
