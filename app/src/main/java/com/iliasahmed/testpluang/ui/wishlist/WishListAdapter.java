package com.iliasahmed.testpluang.ui.wishlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iliasahmed.testpluang.R;
import com.iliasahmed.testpluang.databinding.ItemWishlistBinding;
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
        ItemWishlistBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_wishlist, viewGroup, false);

        return new WishlistViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder myViewHolder, int i) {
        QuotesModel model = wishlist.get(i);
        myViewHolder.binding.tvDate.setText(Utils.formattedDateFromString("", "dd/MM/yyyy hh:mm:ss aa", model.getDate()));
        myViewHolder.binding.tvName.setText(Utils.getName(model.getSid()));
        myViewHolder.binding.tvPrice.setText(String.format("%s", model.getPrice()));
        myViewHolder.binding.tvHighPrice.setText(String.format("%s", model.getHigh()));
        myViewHolder.binding.tvLowPrice.setText(String.format("%s", model.getLow()));
        myViewHolder.binding.tvChanged.setText(String.format("%s", model.getChange()));
        myViewHolder.binding.tvClosingPrice.setText(String.format("%s", model.getClose()));
        myViewHolder.binding.tvVolume.setText(String.valueOf(model.getVolume()));

        if (model.getChange() < 0) {
            myViewHolder.binding.llChanged.setBackground(context.getResources().getDrawable(R.drawable.red_rounded_bg));
            myViewHolder.binding.tvChanged.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            myViewHolder.binding.llChanged.setBackground(context.getResources().getDrawable(R.drawable.green_rounded_bg));
            myViewHolder.binding.tvChanged.setTextColor(context.getResources().getColor(R.color.dark_green));
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
        ItemWishlistBinding binding;

        public WishlistViewHolder(ItemWishlistBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            binding.trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.delete(wishlist.get(getLayoutPosition()).getSid());
                }
            });
        }
    }

}
