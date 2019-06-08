package cdictv.twds.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    @Override

    public IBinder onBind(Intent intent) {

// TODO Auto-generated method stub

        return null;

    }

    @Override

    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("cdictv.twds","cdictv.twds.activity.guideActivity");
        i.setComponent(cn);
        startActivity(i);

    }
}
