package love.dragonist.flowerhouse.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import love.dragonist.flowerhouse.bean.Flower;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetIntro {
    private OkHttpClient client;
    private Handler handler;

    public GetIntro() {
        client = new OkHttpClient();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void get(final int flowerId, final Flower flower) {
        final Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/intro/?username=rabbull&id=" + flowerId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    flower.setName(object.getString("name"));
                    flower.setLanguage(object.getString("flower_lang"));
                    flower.setIntroduce(object.getString("intro_content"));
                    flower.setPoem(object.getString("poem"));
                    flower.setImg(object.getString("image_url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 0;
                message.obj = flower;
                handler.sendMessage(message);
            }
        });
    }
}
