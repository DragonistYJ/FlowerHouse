package love.dragonist.flowerhouse.net;

import android.os.Handler;
import android.os.Message;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import love.dragonist.flowerhouse.bean.Flower;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetIntroByName {
    private Handler handler;
    private OkHttpClient okHttpClient;

    public GetIntroByName(Handler handler) {
        this.handler = handler;
        this.okHttpClient = new OkHttpClient();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void get(String name) {
        Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/info/?name=" + name)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Flower flower = new Flower();
                        flower.setId(jsonObject.getInt("id"));
                        flower.setName(jsonObject.getString("name"));
                        flower.setFamily(jsonObject.getString("family"));
                        flower.setGenus(jsonObject.getString("genus"));
                        Message message = new Message();
                        message.obj = flower;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
