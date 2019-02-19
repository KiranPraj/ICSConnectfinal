package org.icspl.icsconnect.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.icspl.icsconnect.R;


/**
 * Created by Dytstudio.
 */

public class HolderMe extends RecyclerView.ViewHolder {

    private TextView time, chatText;
    private LinearLayout mAttachmentLayout;
    private ImageView img_attchment_view;

    public HolderMe(View v) {
        super(v);
        time = v.findViewById(R.id.tv_time);
        img_attchment_view = v.findViewById(R.id.img_attchment_view_me);
        chatText = v.findViewById(R.id.tv_chat_text);
        mAttachmentLayout = v.findViewById(R.id.ll_chat_attachment);
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

    public LinearLayout getmAttachmentLayout() {
        return mAttachmentLayout;
    }

    public ImageView getImg_attchment_view() {
        return img_attchment_view;
    }
}
