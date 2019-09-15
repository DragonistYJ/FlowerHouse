package love.dragonist.flowerhouse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.VH> {
    private Context context;
    private List<Flower> flowers;
    private OnItemClickListener onItemClickListener;

    public void setListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public HomeRecyclerAdapter(Context context, List<Flower> flowers) {
        this.context = context;
        this.flowers = flowers;
    }

    public void setFlowers(List<Flower> flowers) {
        this.flowers = flowers;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_push, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        Glide.with(context).load(flowers.get(i).getImg()).into(vh.imgFlower);
        vh.textName.setText(flowers.get(i).getName());
        vh.textLanguage.setText(flowers.get(i).getLanguage());
        String s = flowers.get(i).getFamily() + flowers.get(i).getGenus();
        vh.textClass.setText(s.substring(0, 1));
        vh.textClassSmall.setText(s.substring(1));
        vh.constraintWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flowers.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView imgFlower;
        private TextView textName;
        private TextView textLanguage;
        private TextView textClass;
        private TextView textClassSmall;
        private ConstraintLayout constraintWhole;

        VH(@NonNull View itemView) {
            super(itemView);
            imgFlower = itemView.findViewById(R.id.item_home_push_flower);
            textName = itemView.findViewById(R.id.item_home_push_name);
            textLanguage = itemView.findViewById(R.id.item_home_push_language);
            textClass = itemView.findViewById(R.id.item_home_push_class);
            textClassSmall = itemView.findViewById(R.id.item_home_push_class_small);
            constraintWhole = itemView.findViewById(R.id.item_home_push_whole);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
