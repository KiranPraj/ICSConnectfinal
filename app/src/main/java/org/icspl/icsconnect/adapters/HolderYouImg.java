package org.icspl.icsconnect.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.icspl.icsconnect.R;

/**
 * Created by Dytstudio.
 */

public class HolderYouImg extends RecyclerView.ViewHolder {

    private TextView time, chatText;
    private ImageView iv_other_you;

    public HolderYouImg(View v) {
        super(v);
        time = (TextView) v.findViewById(R.id.tv_time);
        chatText = v.findViewById(R.id.tv_chat_text);
        iv_other_you = v.findViewById(R.id.iv_other_you);
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

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }

    public ImageView getIv_other_you() {
        return iv_other_you;
    }

    public void setIv_other_you(ImageView iv_other_you) {
        this.iv_other_you = iv_other_you;
    }
}
