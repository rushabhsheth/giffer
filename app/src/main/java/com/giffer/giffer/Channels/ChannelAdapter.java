package com.giffer.giffer.Channels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.giffer.giffer.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by archanaarunkumar on 8/13/17.
 */

public class ChannelAdapter extends ArrayAdapter<Channel> {

    private Context context;
    public ChannelAdapter(Context context,int resourceId,int textViewResourceId, List<Channel> channelList) {
        super(context, resourceId,textViewResourceId, channelList);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView channelName;
        CircleImageView channelLogo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        // Get the data item for this position
        Channel channel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_selectchannel, parent, false);
            mHolder = new ViewHolder();
            mHolder.channelName = (TextView) convertView.findViewById(R.id.selectchannel_channelName);
            mHolder.channelLogo = (CircleImageView) convertView.findViewById(R.id.selectchannel_channelLogo);
            convertView.setTag(mHolder);
        }
        else
            mHolder = (ViewHolder) convertView.getTag();


        // Populate the data into the template view using the data object
        mHolder.channelName.setText(channel.getChannelName());

        int id = getContext().getResources().getIdentifier(channel.getChannelLogo(), "mipmap", context.getPackageName());
        mHolder.channelLogo.setImageResource(id);

        // Return the completed view to render on screen
        return convertView;
    }
}
