package com.iliasahmed.testpluang.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iliasahmed.testpluang.R;
import com.iliasahmed.testpluang.model.QuotesModel;
import com.iliasahmed.testpluang.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<QuotesModel> list;
    private Context context;
    private WishListListener listener;
    private HashMap<String, QuotesModel> map = new HashMap<>();

    public HomeAdapter(ArrayList<QuotesModel> wishlist, Context context, WishListListener listener) {
        this.list = wishlist;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, viewGroup, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder myViewHolder, int i) {
        QuotesModel model = list.get(i);
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

        myViewHolder.checkBox.setChecked(false);

    }

    public HashMap<String, QuotesModel> getMap() {
        return map;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface WishListListener {

        void checkEmpty();

        void delete(String slug);

    }

     class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvHighPrice, tvLowPrice, tvChanged, tvVolume, tvClosingPrice, tvDate;
        CheckBox checkBox;
        LinearLayout llChanged;
        View view;

        public HomeViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvHighPrice = itemView.findViewById(R.id.tvHighPrice);
            tvLowPrice = itemView.findViewById(R.id.tvLowPrice);
            tvChanged = itemView.findViewById(R.id.tvChanged);
            tvVolume = itemView.findViewById(R.id.tvVolume);
            tvClosingPrice = itemView.findViewById(R.id.tvClosingPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            checkBox = itemView.findViewById(R.id.cbHomeItem);
            llChanged = itemView.findViewById(R.id.llChanged);
            view = itemView;

            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    map.put(list.get(getLayoutPosition()).getSid(), list.get(getLayoutPosition()));
                }else{
                    if (map.get(list.get(getLayoutPosition()).getSid()) != null){
                        map.remove(list.get(getLayoutPosition()).getSid());
                    }
                }
            });
        }
    }

}
