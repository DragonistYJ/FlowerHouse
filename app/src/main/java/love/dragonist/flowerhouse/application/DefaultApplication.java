package love.dragonist.flowerhouse.application;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class DefaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
