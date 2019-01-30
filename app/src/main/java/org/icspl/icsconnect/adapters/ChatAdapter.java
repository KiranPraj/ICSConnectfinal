package org.icspl.icsconnect.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.icspl.icsconnect.R;
import org.icspl.icsconnect.models.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dytstudio.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Chat> items;

    private final int DATE = 0, YOU = 1, ME = 2, IMAGES = 3;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<Chat> items) {
        this.items = items;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        switch (items.get(position).getType()) {
            case "0":
                return DATE;
            case "1":
                return YOU;
            case "2":
                return ME;
            case "3":
                return IMAGES;
        }

        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case DATE:
                View v1 = inflater.inflate(R.layout.layout_holder_date, viewGroup, false);
                viewHolder = new HolderDate(v1);
                break;
            case YOU:
                View v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false);
                viewHolder = new HolderYou(v2);
                break;
            case IMAGES:
                View v3 = inflater.inflate(R.layout.layout_holder_me_image, viewGroup, false);
                viewHolder = new HolderMeImg(v3);
                break;
            default:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderMe(v);
                break;
        }
        return viewHolder;
    }

    public void addItem(List<Chat> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case DATE:
                HolderDate vh1 = (HolderDate) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case YOU:
                HolderYou vh2 = (HolderYou) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case IMAGES:
                HolderMeImg vh3 = (HolderMeImg) viewHolder;
                configureViewHolder4(vh3);
                break;
            default:
                HolderMe vh = (HolderMe) viewHolder;
                configureViewHolder3(vh, position);
                break;
        }
    }

    private void configureViewHolder4(final HolderMeImg vh3) {
        final ArrayList<Integer> color_vib = new ArrayList<>();

        Picasso.get().load("http://icspl.org/data/Employeephoto/ICS429413551_381440145232650_1895843746_o.jpg")
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                //work with the palette here
                                for (Palette.Swatch swatch : palette.getSwatches()) {
                                    color_vib.add(swatch.getRgb());
                                }
                                vh3.getChatText().setBackgroundColor(color_vib.get(0));
                            }
                        });
                        vh3.getIvImgMe().setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }

    private void configureViewHolder3(HolderMe vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolder2(HolderYou vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
        Picasso.get().load("http://icspl.org/data/Employeephoto/" + items.get(position).getPhotoPath())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(vh1.getIv_other_you());
    }

    private void configureViewHolder1(HolderDate vh1, int position) {
        vh1.getDate().setText(items.get(position).getText());
    }


}
