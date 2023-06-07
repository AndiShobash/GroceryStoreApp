package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{
    private List<Item> itemsList= new ArrayList<>();
    static Context context;
     ItemDB db;
    private static ItemViewModel viewModel;
    private int flag=0;
    int[]images;
    private static MainActivity main;
    private int selectedPos  =  -1;;
    public int slected_item;

    /************************************************ Contractor for the adapter **********************************************************/
    public ItemAdapter(Context con,MainActivity main,ItemViewModel viewModel,int [] images){
        this.context=con;
        db=ItemDB.getInstance(context);
        itemsList=db.getItemDao().getAllItems();
        this.images=images;
        this.main=main;
        this.viewModel=viewModel;
        /************************************************ LiveData to see which item is selected  **********************************************************/
        ItemAdapter.viewModel.getSelectedItemMutableLiveData().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer slecteditem) {
                slected_item = slecteditem;
               notifyDataSetChanged();
            }
        });

    }

    /************************************************ inflates the view where the items will be displayed  **********************************************************/
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_item,parent,false);

        return new ItemHolder(itemView);
    }

    /************************************************ binds each item to the position where is will be displayed **********************************************************/
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item currentItem=itemsList.get(position);
        holder.textViewTitle.setText(currentItem.getType());
        holder.textVieDesctrption.setText(currentItem.getDate());
        holder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setItems(List<Item> items){
        this.itemsList=items;
        notifyDataSetChanged();
    }

    /************************************************ inner class like we learned in the class **********************************************************/

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textVieDesctrption;
        private ImageView imageView;

        public ItemHolder(View itemView){
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.text_view_name);
            textVieDesctrption=itemView.findViewById(R.id.text_view_under);
            imageView=itemView.findViewById(R.id.imageView);

            //goes to the next activity after clicking the item(to the checkout) and sends what item is selected
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemAdapter.viewModel.setSelectedItem(getAdapterPosition());
                    main.call_app_order_item(itemsList.get(slected_item));

                }
            });
        }

    }
}
