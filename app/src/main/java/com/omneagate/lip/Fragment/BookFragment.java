package com.omneagate.lip.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;

import com.example.user.lip.R;
import com.omneagate.lip.Adaptor.ScaleInAnimationAdapter;
import com.omneagate.lip.Adaptor.ShoppingAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by USER1 on 17-06-2016.
 */
public class BookFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ShoppingAdapter adapter;
    private ListView search_lv;
//    private List<PolingSquardDetails> pollingSquadList_search, pollingSquadList_;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

//        pollingSquadList_search = new ArrayList<>();
//        pollingSquadList_ = new ArrayList<>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        search_lv = (ListView) view.findViewById(R.id.search_lv);
        mSearchView = (SearchView) view.findViewById(R.id.book_searchView);
        mSearchView.setQueryHint("Search by Categories");
        setAdapter();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>1){
                    search_lv.setVisibility(View.VISIBLE);
                }else if(newText.length()==0){
                    search_lv.setVisibility(View.GONE);
                }
                return false;
            }
        });

        return view;
    }


    private void setAdapter() {

//        adapter = new ShoppingAdapter(getActivity());
//        ScaleInAnimationAdapter scaleInAdapter = new ScaleInAnimationAdapter(adapter);
//        scaleInAdapter.setFirstOnly(true);
//        scaleInAdapter.setDuration(500);
//        scaleInAdapter.setInterpolator(new OvershootInterpolator(.5f));
//        mRecyclerView.setAdapter(scaleInAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*
    * Category Search
    * */
    /*private void categorySearch(String categoryName) {
//        Log.d("Check list size", "" + pollingSquadList_.size());
//        if(pollingSquadList_search != null) {
//            pollingSquadList_search.clear();
//            Log.d("Check list size", "" + pollingSquadList_.size());
//            for (PolingSquardDetails selecteAgentList : pollingSquadList_) {
//                if (selecteAgentList.getName().toLowerCase(Locale.ENGLISH).contains(voterNameId.toLowerCase())) {
//                    pollingSquadList_search.add(selecteAgentList);
//                }
//            }
//            voterListAdaptor = new ListOfAgent(pollingSquadList_search, context, selectedLanguage);
//            listView.setAdapter(voterListAdaptor);
        }*/
    }

