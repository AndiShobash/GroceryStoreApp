package com.example.project;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/************************************************ We are not using this class in the end  **********************************************************/

public class ItemRepository {
    private ItemDao itemDao;
    private List<Item> allItems;

    public ItemRepository(Application application){
        ItemDB database=ItemDB.getInstance(application);
        itemDao=database.getItemDao();
        allItems=itemDao.getAllItems();
    }

    public void insert(Item item){
        new InsertItemAsyncTask(itemDao).execute(item);

    }
    public void update(Item item){
        new UpdateItemAsyncTask(itemDao).execute(item);

    }
    public void delete(Item item){
        new DeleteItemAsyncTask(itemDao).execute(item);

    }
    public void DelteAlltItemAsyncTask(Item item){
        new InsertItemAsyncTask(itemDao).execute(item);

    }
    public List<Item>getAllItems(){
        return allItems;
      }
      private static class InsertItemAsyncTask extends AsyncTask<Item,Void,Void>{
        private ItemDao itemDao;
        private  InsertItemAsyncTask(ItemDao itemDao){
            this.itemDao=itemDao;
        }


          @Override
          protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
              return null;
          }
      }
    private static class UpdateItemAsyncTask extends AsyncTask<Item,Void,Void>{
        private ItemDao itemDao;
        private  UpdateItemAsyncTask(ItemDao itemDao){
            this.itemDao=itemDao;
        }


        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }
    private static class DeleteItemAsyncTask extends AsyncTask<Item,Void,Void>{
        private ItemDao itemDao;
        private  DeleteItemAsyncTask(ItemDao itemDao){
            this.itemDao=itemDao;
        }


        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }
//    private static class DelteAlltItemAsyncTask extends AsyncTask<Void,Void,Void>{
//        private ItemDao itemDao;
//        private  DelteAlltItemAsyncTask(ItemDao itemDao){
//            this.itemDao=itemDao;
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... Voids) {
//            itemDao.delete();
//            return null;
//        }
//    }

     }
