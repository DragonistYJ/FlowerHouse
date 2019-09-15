package love.dragonist.flowerhouse.net;

import android.os.Handler;
import android.os.Message;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import love.dragonist.flowerhouse.bean.Flower;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetQuiz {
    private OkHttpClient client;
    private Handler handler;

    public GetQuiz() {
        this.client = new OkHttpClient();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void get() {
        Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/quiz")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject object;
                List<String> urls = new ArrayList<>();
                int choice = 0;
                int ans = 0;
                try {
                    object = new JSONObject(response.body().string());
                    choice = object.getJSONObject("correct").getInt("id");
                    JSONArray array = object.getJSONArray("choices");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        urls.add(object1.getString("image_url"));
                        if (object1.getInt("id") == choice) ans = i;
                    }
                    urls.add(object.getJSONObject("correct").getJSONObject("info").getString("name"));
                    urls.add(array.getJSONObject(ans).getString("poem"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = ans + 1;
                message.obj = urls;
                handler.sendMessage(message);
            }
        });
    }
}
