package love.dragonist.flowerhouse.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import love.dragonist.flowerhouse.bean.Flower;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetInfo {
    private OkHttpClient client;
    private Handler handler;

    public GetInfo() {
        client = new OkHttpClient();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void get(Pair pair) {
        Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/info/?" + pair.first + "=" + pair.second)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                HashSet<Flower> flowers = new HashSet<>();
                try {
                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Flower flower = new Flower();
                        flower.setId(object.getInt("id"));
                        flower.setName(object.getString("name"));
                        flower.setGenus(object.getString("genus"));
                        flower.setFamily(object.getString("family"));
                        flowers.add(flower);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1;
                message.obj = flowers;
                handler.sendMessage(message);
            }
        });
    }
}
