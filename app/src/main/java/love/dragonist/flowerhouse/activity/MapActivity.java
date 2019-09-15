package love.dragonist.flowerhouse.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.net.GetNearby;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap map;
    private LocationClient locationClient;
    private MapStatusUpdate mapStatusUpdate;
    private GetNearby getNearby;
    private List<LatLng> latLngs = new ArrayList<>();
    private boolean locate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        getNearby = new GetNearby(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<LatLng> newLatLngs = (List<LatLng>) msg.obj;
                for (LatLng latLng : newLatLngs) {
                    boolean flag = false;
                    for (LatLng lng : latLngs) {
                        if (Math.abs(latLng.latitude - lng.latitude) < 0.0000001
                                && Math.abs(latLng.longitude - lng.longitude) < 0.0000001) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) continue;
                    latLngs.add(latLng);

                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.flower);
                    OverlayOptions option = new MarkerOptions()
                            .position(latLng)
                            .icon(bitmap);
                    map.addOverlay(option);
                }
            }
        });
    }

    private void initView() {
        mapView = findViewById(R.id.map);
        map = mapView.getMap();
        map.setMyLocationEnabled(true);
        locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);

        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new MyLocationListener());
        locationClient.start();
    }

    public void initListener() {

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            map.setMyLocationData(locData);
            if (locate) {
                locate(new LatLng(location.getLatitude(), location.getLongitude()));
                locate = false;
            }
            getNearby.get(location.getLatitude(), location.getLongitude());
        }
    }

    public void locate(LatLng latLng) {
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        map.animateMapStatus(mapStatusUpdate);
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16);
        map.animateMapStatus(mapStatusUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        map.setMyLocationEnabled(false);
        mapView.onDestroy();
    }
}
