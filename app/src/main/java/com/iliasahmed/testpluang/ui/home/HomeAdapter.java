package com.iliasahmed.testpluang.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iliasahmed.testpluang.R;
import com.iliasahmed.testpluang.databinding.ItemHomeBinding;
import com.iliasahmed.testpluang.model.QuotesModel;
import com.iliasahmed.testpluang.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<QuotesModel> list;
    private Context context;
    private HashMap<String, QuotesModel> map = new HashMap<>();

    public HomeAdapter(ArrayList<QuotesModel> wishlist, Context context) {
        this.list = wishlist;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemHomeBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_home, viewGroup, false);
        return new HomeViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder myViewHolder, int i) {
        QuotesModel model = list.get(i);
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

    public HashMap<String, QuotesModel> getMap() {
        return map;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        ItemHomeBinding binding;

        public HomeViewHolder(ItemHomeBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            binding.cbHomeItem.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    map.put(list.get(getLayoutPosition()).getSid(), list.get(getLayoutPosition()));
                } else {
                    if (map.get(list.get(getLayoutPosition()).getSid()) != null) {
                        map.remove(list.get(getLayoutPosition()).getSid());
                    }
                }
            });
        }
    }

}
