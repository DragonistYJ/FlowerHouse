package love.dragonist.flowerhouse.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.adapter.MineRecyclerAdapter;
import love.dragonist.flowerhouse.bean.Flower;
import love.dragonist.flowerhouse.bean.Record;
import love.dragonist.flowerhouse.net.GetIntro;
import love.dragonist.flowerhouse.net.GetRecord;

public class MineFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final static int DETAIL = 1;

    private ImageView imgBg;
    private ImageView imgPortrait;
    private RecyclerView recyclerRecord;

    private MineRecyclerAdapter mineRecyclerAdapter;

    private MineRecyclerAdapter.OnItemClickListener mineRecyclerListener;
    private OnFragmentInteractionListener mListener;
    private Gson gson;
    private GetRecord getRecord = new GetRecord();
    private List<Record> records = new ArrayList<>();

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initData();

        imgBg = view.findViewById(R.id.mine_bg);
        imgPortrait = view.findViewById(R.id.mine_portrait);
        Glide.with(getContext())
                .load(R.mipmap.login_portrait)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imgPortrait);
        Glide.with(getContext())
                .load(R.mipmap.mine_bg)
                .into(imgBg);
        recyclerRecord = view.findViewById(R.id.mine_recycler);
        recyclerRecord.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerRecord.setAdapter(mineRecyclerAdapter);

        initListener();
        return view;
    }

    private void initData() {
        gson = new Gson();

        final GetIntro getIntro = new GetIntro();
        getIntro.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mineRecyclerAdapter.setRecords(records);
                mineRecyclerAdapter.notifyDataSetChanged();
            }
        });

        getRecord.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Pair> pairs = (List<Pair>) msg.obj;
                for (int i = 0; i < pairs.size(); i++) {
                    Pair pair = pairs.get(i);
                    Flower flower = new Flower();
                    Record record = new Record();
                    record.setDate(((String) pair.second).split("-")[2]);
                    record.setMonth(((String) pair.second).split("-")[1]);
                    record.setFlower(flower);
                    records.add(record);
                    getIntro.get((Integer) pair.first, record.getFlower());
                }
            }
        });
        getRecord.get();

        mineRecyclerAdapter = new MineRecyclerAdapter(getContext(), records);
    }

    private void initListener() {
        mineRecyclerListener = new MineRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                JSONObject object = new JSONObject();
                try {
                    object.put("fragment", "mine");
                    object.put("action", DETAIL);
                    object.put("record", records.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(object);
            }
        };
        mineRecyclerAdapter.setListener(mineRecyclerListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(JSONObject jsonObject);
    }
}
