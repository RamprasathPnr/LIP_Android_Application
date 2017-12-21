package com.omneagate.lip.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Activity.EventsFullViewActivity;
import com.omneagate.lip.Activity.NewsActivity;
import com.omneagate.lip.Activity.RecyclerItemClickListener;
import com.omneagate.lip.Adaptor.SchemesAdapter;
import com.omneagate.lip.Model.EventDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.ConstantsKeys;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 31/5/16.
 */


public class EventsFragment extends Fragment implements Handler.Callback {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    SchemesAdapter adapter;
    SwipeRefreshLayout swipe_refresh_layout;
    private ResponseDto responseData;
    private String userId;
    private ResReqController controller;
    List<EventDto> eventdto;
    private ResponseDto category;
    Dialog dialog;
    private String selectedLanguage;

    public static int flag = 0;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            flag = 1;
            getAllEvents();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new ResReqController(getActivity());
        controller.addOutboxHandler(new android.os.Handler(this));
        selectedLanguage = ((NewsActivity) getActivity()).languageChcek();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_news, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_news_list);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setRefreshing(false);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(super.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(super.getContext(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent in = new Intent(getActivity(), EventsFullViewActivity.class);
                in.putExtra(ConstantsKeys.EVENT_DETAIL, eventdto.get(position));
                startActivity(in);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(true);
                swipe_refresh_layout.setRefreshing(false);
            }
        });


        /*mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/
        String userdetailsDTO = MySharedPreference.readString(getActivity(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();

        return view;


    }

    private void getAllEvents() {
        try {

            Object data = userId;
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.ALL_EVENTS_, inputParams, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {

        // specify an adapter (see also next example)
        adapter = new SchemesAdapter(super.getContext(), eventdto, selectedLanguage);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Utilities.getInstance().dismissProgressBar();
        dismissProgressBar();
        switch (msg.what) {
            case ResReqController.ALL_EVENTS_SUCCESS:
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                category = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                if (category != null && category.getStatus().equalsIgnoreCase("true")) {
                    flag = 0;
                    eventdto = category.getEventsDtoList();
                    setAdapter();

                }
                break;

            case ResReqController.ALL_EVENTS_FAILED:
                Toast.makeText(getActivity(), getActivity().getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                Log.d("Error", "" + msg.obj.toString());
                break;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getAllEvents();
    }

    public void dismissProgressBar() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}