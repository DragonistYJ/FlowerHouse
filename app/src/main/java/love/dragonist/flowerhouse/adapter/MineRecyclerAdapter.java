package love.dragonist.flowerhouse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.bean.Flower;
import love.dragonist.flowerhouse.bean.Record;

public class MineRecyclerAdapter extends RecyclerView.Adapter<MineRecyclerAdapter.VH> {
    private Context context;
    private List<Record> records;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MineRecyclerAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mine_record, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        Glide.with(context).load(records.get(i).getFlower().getImg()).into(vh.img);
        vh.textDay.setText(records.get(i).getDate());
        vh.textMonth.setText(records.get(i).getMonth().substring(1)+"月");
        vh.textName.setText(records.get(i).getFlower().getName());
        vh.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView textDay;
        private TextView textMonth;
        private TextView textName;
        private ImageView img;

        public VH(@NonNull View itemView) {
            super(itemView);
            textDay = itemView.findViewById(R.id.item_mine_record_day);
            textMonth = itemView.findViewById(R.id.item_mine_record_month);
            textName = itemView.findViewById(R.id.item_mine_record_name);
            img = itemView.findViewById(R.id.item_mine_record_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int i);
    }
}
