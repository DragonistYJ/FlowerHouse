package love.dragonist.flowerhouse.net;

import android.os.Handler;
import android.os.Message;
import android.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import love.dragonist.flowerhouse.bean.Record;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetRecord {
    private OkHttpClient client;
    private Handler handler;

    public GetRecord() {
        client = new OkHttpClient();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void get() {
        final Request request = new Request.Builder()
                .url("https://wdnmd.scuisdc.cn:1024/flower/record/?username=rabbull")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArray;
                List<Pair> date = new ArrayList<>();
                try {
                    jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        date.add(new Pair(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("date")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = date;
                handler.sendMessage(message);
            }
        });
    }
}
