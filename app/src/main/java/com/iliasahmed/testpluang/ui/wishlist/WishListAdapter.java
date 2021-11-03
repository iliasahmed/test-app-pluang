package com.iliasahmed.testpluang.ui.wishlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iliasahmed.testpluang.R;
import com.iliasahmed.testpluang.model.QuotesModel;
import com.iliasahmed.testpluang.utils.Utils;

import java.util.ArrayList;


public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishlistViewHolder> {

    private ArrayList<QuotesModel> wishlist;
    private Context context;
    private WishListAdapter instance;
    private WishListListener listener;

    public WishListAdapter(ArrayList<QuotesModel> wishlist, Context context, WishListListener listener) {
        this.wishlist = wishlist;
        this.context = context;
        this.listener = listener;
        instance = this;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wishlist, viewGroup, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder myViewHolder, int i) {
        QuotesModel model = wishlist.get(i);
        myViewHolder.tvDate.setText(Utils.formattedDateFromString("", "dd/MM/yyyy hh:mm:ss aa", model.getDate()));
        myViewHolder.tvName.setText(Utils.getName(model.getSid()));
        myViewHolder.tvPrice.setText(String.format("%s", model.getPrice()));
        myViewHolder.tvHighPrice.setText(String.format("%s", model.getHigh()));
        myViewHolder.tvLowPrice.setText(String.format("%s", model.getLow()));
        myViewHolder.tvChanged.setText(String.format("%s", model.getChange()));
        myViewHolder.tvClosingPrice.setText(String.format("%s", model.getClose()));
        myViewHolder.tvVolume.setText(String.valueOf(model.getVolume()));

        if (model.getChange() < 0){
            myViewHolder.llChanged.setBackground(context.getResources().getDrawable(R.drawable.red_rounded_bg));
        }else{
            myViewHolder.llChanged.setBackground(context.getResources().getDrawable(R.drawable.green_rounded_bg));
        }
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public interface WishListListener {

        void checkEmpty();

        void delete(String sid);

    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvHighPrice, tvLowPrice, tvChanged, tvVolume, tvClosingPrice, tvDate;
        ImageView trash;
        LinearLayout llChanged;
        View view;

        public WishlistViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvHighPrice = itemView.findViewById(R.id.tvHighPrice);
            tvLowPrice = itemView.findViewById(R.id.tvLowPrice);
            tvChanged = itemView.findViewById(R.id.tvChanged);
            tvVolume = itemView.findViewById(R.id.tvVolume);
            tvClosingPrice = itemView.findViewById(R.id.tvClosingPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            trash = itemView.findViewById(R.id.trash);
            llChanged = itemView.findViewById(R.id.llChanged);
            view = itemView;

            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.delete(wishlist.get(getLayoutPosition()).getSid());
                }
            });
        }
    }

}
