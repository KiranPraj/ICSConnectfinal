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

public class HolderYou extends RecyclerView.ViewHolder {

    private TextView time, chatText;
    private ImageView iv_other_you;
    private ImageView img_attchment_view;
    ;
    private LinearLayout ll_chat_attachment_you;

    public HolderYou(View v) {
        super(v);
        time = (TextView) v.findViewById(R.id.tv_time);
        chatText = v.findViewById(R.id.tv_chat_text);
        iv_other_you = v.findViewById(R.id.iv_other_you);
        img_attchment_view = v.findViewById(R.id.img_attchment_view_you);
        ll_chat_attachment_you = v.findViewById(R.id.ll_chat_attachment_you);
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

    public ImageView getImg_attchment_view() {
        return img_attchment_view;
    }

    public LinearLayout getLl_chat_attachment_you() {
        return ll_chat_attachment_you;
    }
}
