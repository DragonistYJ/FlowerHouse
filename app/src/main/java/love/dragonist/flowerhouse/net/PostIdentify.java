package love.dragonist.flowerhouse.net;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostIdentify {
    private Handler handler;
    private OkHttpClient client;

    public PostIdentify(Handler handler) {
        this.handler = handler;
        client = new OkHttpClient();
    }

    public void post(double latitude, double longitude, String bitmap) {
        RequestBody requestBody = new FormBody.Builder()
                .add("latitude", String.valueOf(latitude))
                .add("longitude", String.valueOf(longitude))
                .add("image", bitmap)
                .build();
        Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/identify/")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                List<String> names = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    JSONObject object = new JSONObject(response.body().string()).getJSONObject("result");
                    jsonArray = new JSONArray(object.getJSONArray("result").toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        names.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.obj = names;
                handler.sendMessage(message);
            }
        });

    }
}
