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
import com.omneagate.lip.Activity.NewsActivity;
import com.omneagate.lip.Activity.NewsDetailActivity;
import com.omneagate.lip.Activity.RecyclerItemClickListener;
import com.omneagate.lip.Adaptor.NewsAdaptor;
import com.omneagate.lip.Model.NewsDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.OnLoadMoreListener;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.ConstantsKeys;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends Fragment implements Handler.Callback {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    //    LatestNewsAdapter adapter;
    NewsAdaptor adaptor;
    SwipeRefreshLayout swipe_refresh_layout;
    private ResReqController controller;
    private ResponseDto responseData;
    private String userId;
    private ResponseDto category;
    private List<NewsDto> newsDtoList;
    private String selectedLanguage;
    Dialog dialog;


    private LinearLayoutManager linearLayoutManager;
    private int newsCount;
    protected Handler handler;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new ResReqController(getActivity());
        controller.addOutboxHandler(new Handler(this));
        selectedLanguage = ((NewsActivity) getActivity()).languageChcek();
        newsDtoList = new ArrayList<>();
        handler = new Handler();
        newsCount = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_news_list);
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setRefreshing(false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(super.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        linearLayoutManager = (LinearLayoutManager) mRecyclerView
                .getLayoutManager();

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(super.getContext(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_LONG).show();
                newsDtoList.get(position);
                Intent i = new Intent(getActivity(), NewsDetailActivity.class);
                i.putExtra(ConstantsKeys.NEWS_FEED, newsDtoList.get(position));
                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        String userDetailsDTO = MySharedPreference.readString(getActivity(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();



        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(true);

                if (EventsFragment.flag == 0) {
                    newsCount = 0;
                    newsDtoList.clear();
                    getAllNews();
                }
                swipe_refresh_layout.setRefreshing(false);
            }
        });

        getAllNews();
        return view;
    }

    /*
    * News first time call
    * */
    private void getAllNews() {
        //String object = newsCount + "/" + userId;
        String object = newsCount + "/" + userId;
        Object data = object;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("", "");
        controller.handleMessage(ResReqController.ALL_NEWS, inputParams, data);
    }

    /*
    * For load more need to call everytime while scroll the recycleview
    * */
    private void getLoadMoreNews() {
        //String object = newsCount + "/" + userId;
        String object = newsCount + "/" + userId;
        Object data = object;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("", "");
        controller.handleMessage(ResReqController.NEWS_LOAD_MORE, inputParams, data);
    }

    /*private void setAdapter_(List<NewsDto> serverResponseList) {
        swipe_refresh_layout.setRefreshing(false);
        if (newsDtoList != null
                && newsDtoList.size() > 0) {

            newsDtoList.addAll(serverResponseList);
            adaptor = new NewsAdaptor(super.getContext(), this.newsDtoList,
                    selectedLanguage, mRecyclerView);
            mRecyclerView.setAdapter(adaptor);
            adaptor.setLoaded();
        }
    }*/

    private void setAdapter(List<NewsDto> serverResponseList) {
        swipe_refresh_layout.setRefreshing(false);
        //   remove progress item
        newsDtoList.remove(newsDtoList.size() - 1);
        adaptor.notifyItemRemoved(newsDtoList.size());
        //add news data
        newsDtoList.addAll(serverResponseList);
        adaptor.notifyItemInserted(newsDtoList.size());
        adaptor.setLoaded();
        adaptor.notifyDataSetChanged();
    }

    @Override
    public boolean handleMessage(Message msg) {
        Utilities.getInstance().dismissProgressBar();
        switch (msg.what) {
            case ResReqController.ALL_NEWS_SUCCESS:
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                category = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                if (category != null
                        && category.getStatus().equalsIgnoreCase("true")) {
                    if (category.getNewsDtoList() != null
                            && category.getNewsDtoList().size() > 0) {

                        newsDtoList.addAll(category.getNewsDtoList());
                        adaptor = new NewsAdaptor(super.getContext(), this.newsDtoList,
                                selectedLanguage, mRecyclerView);
                        mRecyclerView.setAdapter(adaptor);
                        adaptor.setLoaded();

                        adaptor.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                //add null , so the adapter will check view_type and show progress bar at bottom
                                newsDtoList.add(null);
                                adaptor.notifyItemInserted(newsDtoList.size() - 1);

                                getLoadMoreNews();
                            }
                        });
                        newsCount = ++newsCount;
                    }
                }

                break;
            case ResReqController.ALL_NEWS_FAILED:
                Toast.makeText(getActivity(), getActivity().getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                Log.d("Error", "" + msg.obj.toString());
                break;

            case ResReqController.NEWS_LOAD_MORE_FAILED:
                Toast.makeText(getActivity(), getActivity().getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                Log.d("Error", "" + msg.obj.toString());
                break;


            case ResReqController.NEWS_LOAD_MORE_SUCCESS:
                GsonBuilder gsonBuilder_ = new GsonBuilder();
                Gson gson_ = gsonBuilder_.create();
                category = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                if (category != null
                        && category.getStatus().equalsIgnoreCase("true")) {
                    if (category.getNewsDtoList() != null
                            && category.getNewsDtoList().size() > 0) {
                        setAdapter(category.getNewsDtoList());
                        newsCount = ++newsCount;
                    } else {
                        newsDtoList.remove(newsDtoList.size() - 1);
                        adaptor.notifyItemRemoved(newsDtoList.size());
                    }
                }
                break;
        }
        return true;
    }
}
