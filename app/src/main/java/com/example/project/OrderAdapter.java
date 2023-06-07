package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

/************************************************ The adapter for the orders  **********************************************************/

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private List<Order> OrderList= new ArrayList<>();
    static Context context;
    private static OrderViewModel viewModel;
    private int flag=0;
    private static ViewOrders main;
    private int selectedPos  =  -1;;
    public int slected_item;
    private int why=0;

    /************************************************ Constructor for the orders  **********************************************************/
    public OrderAdapter(Context con, ViewOrders main, OrderViewModel viewModel) {
        this.context = con;
        this.main = main;
        this.viewModel = viewModel;

        /************************************************ LiveData to see which position selected **********************************************************/
        OrderAdapter.viewModel.getSelectedOrderMutableLiveData().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer slectedorder) {
                slected_item = slectedorder;
                notifyDataSetChanged();
            }
        });

        /************************************************ LiveData Updates the orders list if anything changes **********************************************************/
        OrderAdapter.viewModel.getOrdersLiveData().observe((LifecycleOwner) context, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                OrderList = orders;
                notifyDataSetChanged();
            }
        });
    }

    /************************************************ Inflates the orders XML where each order will be displayed**********************************************************/
    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View orderView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_order,parent,false);

        return new OrderHolder(orderView);
    }

    /************************************************ binding the orders to each position where it will be displayed **********************************************************/
    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        String full_name;
        String quantity;
        String full_price;
        String date;
        Order current=OrderList.get(position);
        full_name=current.getFirstname()+" "+current.getLastname();
        holder.first_name_last_name.setText(full_name);
        quantity=String.valueOf(current.getQuantity());
        full_price=String.valueOf(current.getTotalprice());
        full_price="₪ "+full_price;
        date=current.getDate();
        holder.type.setText(current.getType());
        holder.quantity.setText(quantity);
        holder.total.setText(full_price);
        holder.date.setText(date);



    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }


    /************************************************ Inner class like we learned in class **********************************************************/

    class OrderHolder extends RecyclerView.ViewHolder{
        private TextView first_name_last_name;
        private TextView type;
        private TextView quantity;
        private TextView total;
        private TextView date;
        private ImageButton delete_btn;

        public OrderHolder(View orderView){
            super(orderView);
            first_name_last_name=orderView.findViewById(R.id.text_view_name);
            type=orderView.findViewById(R.id.type_name);
            quantity=orderView.findViewById(R.id.type_quantity);
            total=orderView.findViewById(R.id.priceinOrderSummary);
            delete_btn=orderView.findViewById(R.id.image_delete);
            date=orderView.findViewById(R.id.type_date);

            /************************************************ deleting the order if clicked the delete icon  **********************************************************/

            delete_btn.setOnClickListener(new HandleClick());//for the member class

            /************************************************ deleting using anonymous listener **********************************************************/
//            delete_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos=getAdapterPosition();
//                    if(pos!=RecyclerView.NO_POSITION){
//                        viewModel.remove_order(pos);
//                    }
//                    Toast.makeText(context, "delete successfully", Toast.LENGTH_SHORT).show();
//                    if (viewModel.SelectedOrder.getValue() == pos) {
//                        viewModel.setSelectedOrder(RecyclerView.NO_POSITION);
//
//                    }
//                    notifyDataSetChanged();
//                }
//            });


        }

        /************************************************ deleting using member class **********************************************************/

        private class HandleClick implements View.OnClickListener {
            public void onClick(View view) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        viewModel.remove_order(pos);
                    }
                    Toast.makeText(context, "delete successfully", Toast.LENGTH_SHORT).show();
                    if (viewModel.SelectedOrder.getValue() == pos) {
                        viewModel.setSelectedOrder(RecyclerView.NO_POSITION);

                    }
                    notifyDataSetChanged();
                }
        }
    }

}
