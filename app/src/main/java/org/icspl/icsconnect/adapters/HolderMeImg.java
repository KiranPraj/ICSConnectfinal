package org.icspl.icsconnect.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.icspl.icsconnect.R;


/**
 * Created by Dytstudio.
 */

public class HolderMeImg extends RecyclerView.ViewHolder {

    private TextView time, chatText;
    private ImageView ivImgMe;

    public HolderMeImg(View v) {
        super(v);
        time = v.findViewById(R.id.tv_time);
        chatText = v.findViewById(R.id.tv_chat_text);
        ivImgMe = v.findViewById(R.id.iv_chat_me);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getChatText() {
        return chatText;
    }

    public ImageView getIvImgMe() {
        return ivImgMe;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}
