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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.adapter.HomeRecyclerAdapter;
import love.dragonist.flowerhouse.bean.Flower;
import love.dragonist.flowerhouse.net.GetIntro;
import love.dragonist.flowerhouse.net.GetInfo;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recycler;
    private HomeRecyclerAdapter homeRecyclerAdapter;

    private List<Flower> flowers;
    private HomeRecyclerAdapter.OnItemClickListener homeRecyclerListener;
    private Gson gson;
    private static final int FLOWER_NUM = 47;
    private Random random = new Random();
    private Handler handler;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycler = view.findViewById(R.id.home_recycler);
        initData();
        initView();
        initListener();
        return view;
    }

    public void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(homeRecyclerAdapter);
    }

    public void initData() {
        gson = new Gson();
        flowers = new ArrayList<>();
        homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), flowers);

        GetInfo getInfo = new GetInfo();
        final GetIntro getIntro = new GetIntro();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    homeRecyclerAdapter.setFlowers(flowers);
                    homeRecyclerAdapter.notifyDataSetChanged();
                    return;
                }
                flowers.addAll((Collection<? extends Flower>) msg.obj);
                for (Flower flower : flowers) {
                    getIntro.get(flower.getId(), flower);
                }

            }
        };
        getInfo.setHandler(handler);
        getIntro.setHandler(handler);

        HashSet<String> ids = new HashSet<>();
        while (ids.size() < 10) {
            ids.add((random.nextInt(FLOWER_NUM) + 1) + "");
        }
        for (String id : ids) {
            getInfo.get(new Pair("id", id));
        }
    }

    public void initListener() {
        homeRecyclerListener = new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                JSONObject object = new JSONObject();
                try {
                    object.put("fragment", "home");
                    object.put("action", 1);
                    object.put("flowerInfo", gson.toJson(flowers.get(position)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(object);
            }
        };
        homeRecyclerAdapter.setListener(homeRecyclerListener);
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
