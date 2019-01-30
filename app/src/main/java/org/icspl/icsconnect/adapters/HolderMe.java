package org.icspl.icsconnect.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import org.icspl.icsconnect.R;


/**
 * Created by Dytstudio.
 */

public class HolderMe extends RecyclerView.ViewHolder {

    private TextView time, chatText;

    public HolderMe(View v) {
        super(v);
        time = v.findViewById(R.id.tv_time);
        chatText = v.findViewById(R.id.tv_chat_text);
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
}
