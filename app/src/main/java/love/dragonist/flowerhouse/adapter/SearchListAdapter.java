package love.dragonist.flowerhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.bean.Flower;

public class SearchListAdapter extends BaseAdapter {
    private Context context;
    private List<Flower> flowers;

    public SearchListAdapter(Context context, List<Flower> flowers) {
        this.context = context;
        this.flowers = flowers;
    }

    @Override
    public int getCount() {
        return flowers.size();
    }

    @Override
    public Object getItem(int position) {
        return flowers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            vh = new VH();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_result, null, true);
            vh.imgFlower = convertView.findViewById(R.id.item_search_result_img);
            vh.textName = convertView.findViewById(R.id.item_search_result_name);
            vh.textClass = convertView.findViewById(R.id.item_search_result_class);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }

        Flower flower = flowers.get(position);
        Glide.with(context).load(flower.getImg()).into(vh.imgFlower);
        vh.textName.setText(flower.getName());
        vh.textClass.setText(flower.getFamily() + flower.getGenus());

        return convertView;
    }

    class VH {
        private ImageView imgFlower;
        private TextView textName;
        private TextView textClass;
    }
}
