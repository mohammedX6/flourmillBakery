package com.flourmillco.flourmill_1.SQLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.flourmillco.flourmill_1.Model.Basket;
import com.flourmillco.flourmill_1.Model.OrderProducts;

import java.util.ArrayList;
import java.util.HashMap;


public class BasketDB extends SQLiteOpenHelper {


    private final static String DATABASE_NAME = "MyBasket.db";
    private final static int DATABASE_V = 1;
    private final static String TABLE_NAME = "Basket";
    private final static String COLUMN_BasketId = "BasketId";
    private final static String COLUMN_BadgeName = "BadgeName";
    private final static String COLUMN_URL = "url";
    private final static String COLUMN_TotalBadges = "TotalBadges";
    private final static String COLUMN_Destination = "Destination";
    private final static String COLUMN_totalprice = "TotalPrice";
    private final static String COLUMN_ProductId = "Productid";
    private final static String TABLE_NAME2 = "OrderProducts";
    private final static String COLUMN_OrderProductId = "id";
    private final static String COLUMN_BName = "Badge";
    private final static String COLUMN_pic = "pic";
    private final static String COLUMN_price = "price";
    private final static String COLUMN_tons = "ton";
    private final static String OrderId = "orderid";
    private final SQLiteDatabase write = getWritableDatabase();
    private final SQLiteDatabase read = getReadableDatabase();

    public BasketDB(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_V);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {

        String query = "create table " + TABLE_NAME + "(" + COLUMN_BasketId + " integer primary key autoincrement," +
                COLUMN_BadgeName + " text," +
                COLUMN_URL + " text,"
                + COLUMN_TotalBadges + " text,"
                + COLUMN_Destination + " text,"
                + COLUMN_totalprice + " text,"
                + COLUMN_ProductId + " text);";

        String query2 = "create table " + TABLE_NAME2 + "(" + COLUMN_OrderProductId + " integer primary key autoincrement," +
                COLUMN_BName + " text," +
                COLUMN_price + " integer,"
                + COLUMN_pic + " text,"
                + COLUMN_tons + " text, " + OrderId + " integer, " + " FOREIGN KEY (" + OrderId + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_BasketId + "));";

        arg0.execSQL(query);
        Log.d("TAG", "...............table added");


        arg0.execSQL(query2);
        Log.d("TAG", "...............table 2 added");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


    }

    public void addBasket(Basket basket) {
        String q = "insert into " + TABLE_NAME + "(" +
                COLUMN_BadgeName + "," +
                COLUMN_URL + "," +
                COLUMN_TotalBadges + "," + COLUMN_Destination + "," + COLUMN_totalprice + "," + COLUMN_ProductId + ") values (" + "\"" + basket.getBadgeName() + "\"" + "," +
                "\"" + basket.getUrl() + "\"" + "," + "\"" +
                basket.getTotalBadges() + "\"" + "," + "\"" + basket.getDestination() + "\"" + "," + "\"" + basket.getTotalprice() + "\"" + "," + "\"" + basket.getId() + "\"" + ");";
        write.execSQL(q);

    }

    public ArrayList<HashMap<String, String>> getBaskets() {
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = read.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("BasketId", cursor.getString(cursor.getColumnIndex(COLUMN_BasketId)));
            user.put("BadgeName", cursor.getString(cursor.getColumnIndex(COLUMN_BadgeName)));
            user.put("url", cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
            user.put("TotalBadges", cursor.getString(cursor.getColumnIndex(COLUMN_TotalBadges)));
            user.put("Destination", cursor.getString(cursor.getColumnIndex(COLUMN_Destination)));
            user.put("TotalPrice", cursor.getString(cursor.getColumnIndex(COLUMN_totalprice)));
            user.put("Productid", cursor.getString(cursor.getColumnIndex(COLUMN_ProductId)));
            userList.add(user);
        }
        return userList;
    }


    public void deleteall() {
        write.execSQL("delete from " + TABLE_NAME);
        write.execSQL("delete from " + TABLE_NAME2);


    }

    public void deleteItem(String Postion) {
        getWritableDatabase().delete(TABLE_NAME, COLUMN_BasketId + "=" + Postion, null);
        Log.d("TAG", "Deleted......!!!!!!!!" + "");

    }

    public HashMap<String, String> getLastRecord() {

        String query = "SELECT * FROM Basket ORDER BY rowid DESC LIMIT 1;";
        HashMap<String, String> last = new HashMap<>();
        Cursor cursor = read.rawQuery(query, null);
        while (cursor.moveToNext()) {
            last.put("BasketId", cursor.getString(cursor.getColumnIndex(COLUMN_BasketId)));
            last.put("BadgeName", cursor.getString(cursor.getColumnIndex(COLUMN_BadgeName)));
            last.put("url", cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
            last.put("TotalBadges", cursor.getString(cursor.getColumnIndex(COLUMN_TotalBadges)));
            last.put("Destination", cursor.getString(cursor.getColumnIndex(COLUMN_Destination)));
            last.put("TotalPrice", cursor.getString(cursor.getColumnIndex(COLUMN_totalprice)));
            last.put("Productid", cursor.getString(cursor.getColumnIndex(COLUMN_ProductId)));

        }
        return last;

    }

    public void addOrderProduct(OrderProducts basket) {
        String q = "insert into " + TABLE_NAME2 + "(" +
                COLUMN_BName + "," +
                COLUMN_price + "," + COLUMN_pic + "," + COLUMN_tons + "," +
                OrderId + ") values (" + "\"" + basket.getBadgeName() + "\"" + "," +
                "\"" + basket.getPrice() + "\"" + "," + "\"" +
                basket.getUrl() + "\"" + "," + "\"" + basket.getTons() + "\"" + "," + "\"" + basket.getOrderId() + "\"" + ");";
        write.execSQL(q);

    }

    public ArrayList<HashMap<String, String>> getOrderProducts() {
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME2;
        Cursor cursor = read.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("id", cursor.getString(cursor.getColumnIndex(COLUMN_OrderProductId)));
            user.put("Badge", cursor.getString(cursor.getColumnIndex(COLUMN_BName)));
            user.put("pic", cursor.getString(cursor.getColumnIndex(COLUMN_pic)));
            user.put("price", cursor.getString(cursor.getColumnIndex(COLUMN_price)));
            user.put("tons", cursor.getString(cursor.getColumnIndex(COLUMN_tons)));
            user.put("orderid", cursor.getString(cursor.getColumnIndex(OrderId)));
            userList.add(user);
        }
        return userList;
    }

    public void deleteItem2(String Postion) {
        getWritableDatabase().delete(TABLE_NAME2, COLUMN_OrderProductId + "=" + Postion, null);
        Log.d("TAG", "Deleted......!!!!!!!!" + "");

    }


}
   			
	
	
    

