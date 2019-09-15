package love.dragonist.flowerhouse.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import love.dragonist.flowerhouse.R;

public class FlowerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int TAKE_PHOTO = 1;
    private static final int SEARCH = 2;
    private static final int ALBUM = 3;
    private static final int CATEGORY = 4;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageView imgVine;
    private ImageView imgClover;
    private ImageView imgCamera;
    private ImageView imgSearch;
    private ImageView imgAlbum;
    private ImageView imgCategory;

    public FlowerFragment() {
        // Required empty public constructor
    }

    public static FlowerFragment newInstance(String param1, String param2) {
        FlowerFragment fragment = new FlowerFragment();
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
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        imgVine = view.findViewById(R.id.flower_vine);
        imgClover = view.findViewById(R.id.flower_bg);
        imgCamera = view.findViewById(R.id.flower_image_camera);
        imgSearch = view.findViewById(R.id.flower_image_search);
        imgAlbum = view.findViewById(R.id.flower_image_album);
        imgCategory = view.findViewById(R.id.flower_image_category);

        initData();
        initListener();
        return view;
    }

    public void initListener() {
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("fragment", "flower");
                    object.put("action", TAKE_PHOTO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(object);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("fragment", "flower");
                    object.put("action", SEARCH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(object);
            }
        });

        imgAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("fragment", "flower");
                    object.put("action", ALBUM);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(object);
            }
        });

        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("fragment", "flower");
                    object.put("action", CATEGORY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(object);
            }
        });
    }

    public void initData() {
        Glide.with(this).load(R.mipmap.flower_vine).into(imgVine);
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
