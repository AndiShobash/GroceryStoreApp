package com.example.project;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/************************************************ ViewModel for the orders  **********************************************************/

public class OrderViewModel extends AndroidViewModel {

    OrderDB db=OrderDB.getInstance(this.getApplication().getApplicationContext());
    List<Order> OrdersList;
    MutableLiveData<List<Order>> OrdersLiveData;
    MutableLiveData<Integer> SelectedOrder;
    public int SelectedOrderPos;
    SharedPreferences app_preferences;
    private boolean save_order=false;

    /************************************************ LiveData for the selected order **********************************************************/

    public OrderViewModel(@NonNull Application application) {
        super(application);
        if(SelectedOrder==null) {
            SelectedOrder=new MutableLiveData<>();
            SelectedOrderPos = RecyclerView.NO_POSITION;
            SelectedOrder.setValue(SelectedOrderPos);
        }
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication().getApplicationContext());
        save_order = app_preferences.getBoolean("checkBox",false);
        /************************************************ LiveData for the orders list  **********************************************************/

        OrdersLiveData=new MutableLiveData<>();
        OrdersList = db.getOrderDao().getAllOrder();
        OrdersLiveData.setValue(OrdersList);
    }
    public MutableLiveData<Integer> getSelectedOrderMutableLiveData() {
        return SelectedOrder;
    }
    public MutableLiveData<List<Order>> getOrdersLiveData() { return OrdersLiveData;}

    /************************************************ Removes the selected order from the list and from the DB  **********************************************************/

    public void remove_order(int position)
    {
        db.getOrderDao().delete(OrdersList.get(position));
        OrdersList.remove(position);
        OrdersLiveData.setValue(OrdersList);
    }


    public void setSelectedOrder(Integer noPosition) {
        SelectedOrderPos = noPosition;
        SelectedOrder.setValue(SelectedOrderPos);
    }
}
