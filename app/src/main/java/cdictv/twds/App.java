package cdictv.twds;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    public static Context INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
    }
}
