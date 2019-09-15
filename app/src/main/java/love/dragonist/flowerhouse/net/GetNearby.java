package love.dragonist.flowerhouse.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetNearby {
    private Handler handler;
    private OkHttpClient client;


    public GetNearby(Handler handler) {
        this.handler = handler;
        this.client = new OkHttpClient();
    }

    public void get(double latitude, double longitude) {
        Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/nearby/?latitude=" + latitude + "&longitude=" + longitude + "&precision=1")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArray = null;
                List<LatLng> list = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LatLng latLng = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                        list.add(latLng);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.obj = list;
                handler.sendMessage(message);
            }
        });
    }
}
