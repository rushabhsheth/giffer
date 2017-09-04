package com.giffer.giffer.Channels;

/**
 * Created by archanaarunkumar on 8/13/17.
 */

public class Channel {
    public String channelName;
    public String channelLogo;

    public Channel(String channelName, String channelLogo) {
        this.channelName = channelName;
        this.channelLogo = channelLogo;
    }

    public String getChannelName(){
        return channelName;
    }

    public String getChannelLogo(){
        return channelLogo;
    }
}
