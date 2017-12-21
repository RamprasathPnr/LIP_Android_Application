package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.NewsDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.ConstantsKeys;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewsDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView title;
    private NewsDto newsDto;

    private TextView mTvTitle;
    //private TextView mTvDescription;
    private TextView mTvUpdate, short_des;
    private ImageView mIvNewsImage;
    private String selectedLanguage;
    private WebView myWebView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ShareActionProvider mShareActionProvider;
    private static final String TAG = NewsDetailActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        //short_des = (TextView) findViewById(R.id.short_des);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("My title");

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), String.valueOf(mIvNewsImage));

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("News Feed");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));




        /*setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
*/
        /*if (selectedLanguage.equalsIgnoreCase("en")) {
            title.setText("News Feed");
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            title.setText("समाचार फ़ीड्स");

        }*/


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        newsDto = (NewsDto) intent.getSerializableExtra(ConstantsKeys.NEWS_FEED);

        setupView();
        setupValue();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupValue() {
        try {
            if (newsDto != null) {
                if (selectedLanguage.equalsIgnoreCase("ta")) {
                    mTvTitle.setText(newsDto.getRegionalTitle());
                    // short_des.setText(newsDto.getShortDescription());
                    myWebView.loadData(newsDto.getRegionalTitle(), "text/html", "UTF-8");
                    //mTvDescription.setText(newsDto.getRegionalDescription());
                } else {
                    mTvTitle.setText(newsDto.getTitle());
                    myWebView.loadData(newsDto.getDescription(), "text/html", "UTF-8");
                    //mTvDescription.setText(newsDto.getDescription());
                }

                mTvUpdate.setText(dateConversion(newsDto.getCreatedDate()));
                Picasso.with(this)
                        .load(newsDto.getImageUrl())
                        .placeholder(R.drawable.no_available_image_200x150)
                        .into(mIvNewsImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupView() {

        mTvTitle = (TextView) findViewById(R.id.txt_news_title);
        //mTvDescription = (TextView) findViewById(R.id.txt_news_description);
        myWebView = (WebView) findViewById(R.id.wv_news_details);
        mTvUpdate = (TextView) findViewById(R.id.txt_news_update);
        mIvNewsImage = (ImageView) findViewById(R.id.img_news_image);
    }

    private String dateConversion(String dateString) {

        try {
            Date date;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
            date = dateFormat.parse(dateString);
            return sdf.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            String des;
            if (selectedLanguage.equalsIgnoreCase("ta")) {
                des = newsDto.getRegionalShareDescription();
            } else {
                des = newsDto.getShareDescription();
            }
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
            i.putExtra(android.content.Intent.EXTRA_TEXT, des);
            startActivity(Intent.createChooser(i, "Share via"));
        }
        return super.onOptionsItemSelected(item);
    }
}
