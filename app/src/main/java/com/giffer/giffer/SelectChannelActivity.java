package com.giffer.giffer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.giffer.giffer.Channels.Channel;
import com.giffer.giffer.Channels.ChannelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archanaarunkumar on 8/13/17.
 */

public class SelectChannelActivity extends AppCompatActivity {

    private static final String TAG = "SelectChannelActivity";
    private List<Channel> mChannelList = new ArrayList<>();
    private ListView mSelectChannelListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectchannel);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarselectchannel);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.activity_selectchannel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDataset();
        mSelectChannelListView = (ListView) findViewById(R.id.listview_selectchannel);

        ChannelAdapter channelArrayAdapter = new ChannelAdapter(this,
                R.layout.item_selectchannel,
                R.id.selectchannel_channelName,
                mChannelList);

        mSelectChannelListView.setAdapter(channelArrayAdapter);

        // register onClickListener to handle click events on each item
        mSelectChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
            {
                String selectedChannelName = mChannelList.get(position).getChannelName();
                String selectedChannelLogo = mChannelList.get(position).getChannelLogo();
//                Toast.makeText(getApplicationContext(), "Channel Selected : "+ selectedChannel,   Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.putExtra("channelName",selectedChannelName);
                intent.putExtra("channelLogo",selectedChannelLogo);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initDataset() {


        mChannelList.add(0,new Channel("Technology", "ic_launcher_round"));
        mChannelList.add(1,new Channel("Sports", "ic_launcher_round"));
        mChannelList.add(2,new Channel("Entertainment", "ic_launcher_round"));
        mChannelList.add(3,new Channel("WorldNews", "ic_launcher_round"));
        mChannelList.add(4,new Channel("Politics", "ic_launcher_round"));
        mChannelList.add(5,new Channel("USA", "ic_launcher_round"));
        mChannelList.add(6,new Channel("Startups", "ic_launcher_round"));
        mChannelList.add(7,new Channel("Fashion", "ic_launcher_round"));
        mChannelList.add(8,new Channel("Science", "ic_launcher_round"));
        mChannelList.add(9,new Channel("Europe", "ic_launcher_round"));
        mChannelList.add(10,new Channel("Automobile", "ic_launcher_round"));
        mChannelList.add(11,new Channel("Travel", "ic_launcher_round"));
    }

}


