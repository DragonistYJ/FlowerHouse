package love.dragonist.flowerhouse.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.List;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.net.GetQuiz;

public class GameFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView cross1;
    private ImageView cross2;
    private ImageView cross3;
    private ImageView cross4;
    private TextView textName;
    private TextView textLanguage;
    private Button btnNext;
    private ConstraintLayout constraintWhole;
    private Handler handler;
    private GetQuiz getQuiz = new GetQuiz();
    private int answer;

    private OnFragmentInteractionListener mListener;
    private Animation animation;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
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
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        initData();

        img1 = view.findViewById(R.id.game_img1);
        img2 = view.findViewById(R.id.game_img2);
        img3 = view.findViewById(R.id.game_img3);
        img4 = view.findViewById(R.id.game_img4);
        cross1 = view.findViewById(R.id.game_cross1);
        cross2 = view.findViewById(R.id.game_cross2);
        cross3 = view.findViewById(R.id.game_cross3);
        cross4 = view.findViewById(R.id.game_cross4);
        btnNext = view.findViewById(R.id.game_btn_next);
        constraintWhole = view.findViewById(R.id.game_whole);
        textName = view.findViewById(R.id.game_name);
        textLanguage = view.findViewById(R.id.game_language);
        getQuiz.get();

        initListener();
        return view;
    }

    private void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<String> urls = (List<String>) msg.obj;
                if (urls.size() == 0) return;
                Glide.with(getContext()).load(urls.get(0)).into(img1);
                Glide.with(getContext()).load(urls.get(1)).into(img2);
                Glide.with(getContext()).load(urls.get(2)).into(img3);
                Glide.with(getContext()).load(urls.get(3)).into(img4);
                textName.setText(urls.get(4));
                textLanguage.setText(urls.get(5));
                answer = msg.what;
                setAllInvisible();
            }
        };
        getQuiz.setHandler(handler);
    }

    private void initListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_right);
                constraintWhole.startAnimation(animation);
                getQuiz.get();
            }
        });
        img1.setOnClickListener(new MyAnswer(1));
        img2.setOnClickListener(new MyAnswer(2));
        img3.setOnClickListener(new MyAnswer(3));
        img4.setOnClickListener(new MyAnswer(4));
    }

    private class MyAnswer implements View.OnClickListener {
        private int self;

        MyAnswer(int self) {
            this.self = self;
        }

        @Override
        public void onClick(View v) {
            if (self == answer) {
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_out_right);
                constraintWhole.startAnimation(animation);
                getQuiz.get();
            } else {
                animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.blink_short);
                switch (self) {
                    case 1:
                        cross1.startAnimation(animation);
                        break;
                    case 2:
                        cross2.startAnimation(animation);
                        break;
                    case 3:
                        cross3.startAnimation(animation);
                        break;
                    case 4:
                        cross4.startAnimation(animation);
                }

            }
        }
    }

    private void setAllInvisible() {
        cross1.setVisibility(View.INVISIBLE);
        cross2.setVisibility(View.INVISIBLE);
        cross3.setVisibility(View.INVISIBLE);
        cross4.setVisibility(View.INVISIBLE);
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
