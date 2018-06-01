package com.example.rania.itigraduationproject.SqliteDBTrip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rania.itigraduationproject.model.Trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DBconnection  extends SQLiteOpenHelper{

    public static final String db_name="trip.sql";

    public DBconnection(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS trip (id INTEGER primary key," +
                " trip_name TEXT NOT NULL, trip_start_place TEXT, trip_end_place TEXT,trip_time TEXT," +
                " day  DATETIME DEFAULT CURRENT_TIMESTAMP,start_lat TEXT,start_alt TEXT,end_lat TEXT" +
                ",end_alt TEXT,Past TEXT,details TEXT,userID INTEGER,driverID INTEGER,driverName TEXT , tripCost FLOAT ," +
                " carID TEXT,carColor TEXT ,carBrand TEXT,carModel TEXT,tripId INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table IF EXISTS trip");
        onCreate(db);

    }


    public void insertIntoTrip(String name , String start, String end,
                               String tripdate,String time,String start_lat,
                               String start_alt,String end_lat,String end_alt,
                               String past , String details,Integer driverId,
                               String driverName,Float tripCost,String CarID,
                               String carColor,String carBrand, String carModel,
                               Integer userID,Integer tripId){

        SQLiteDatabase query=this.getWritableDatabase();

        ContentValues  values=new ContentValues();

        values.put("trip_name",name);
        values.put("trip_time",time);
        values.put("trip_start_place",start);
        values.put("trip_end_place",end);
        values.put("day",tripdate);
        values.put("past",past);
        values.put("start_lat",start_lat);
        values.put("start_alt",start_alt);
        values.put("end_lat",end_lat);
        values.put("end_alt",end_alt);
        values.put("details",details);
        values.put("driverID",driverId);
        values.put("userID",userID);
        values.put("driverName",driverName);
        values.put("tripCost",tripCost);
        values.put("CarID",CarID);
        values.put("carColor",carColor);
        values.put("carBrand",carBrand);
        values.put("carModel",carModel);
        values.put("tripId",tripId);

        query.insert("trip",null,values);
        Log.i("test","row inserted");

    }

    public ArrayList<String> readFromTripRecent(){
        //trips=new TripInfo();
        ArrayList<String> tripsNames=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        String query ="select * from trip where past=?";
        String past="f";
        Cursor result=db.rawQuery(query,new String[]{past});

        result.moveToFirst();
        int i =0;
        String tripName ;
        while (result.isAfterLast()==false){
            tripName =result.getString(result.getColumnIndex("trip_name"));
            tripsNames.add(tripName);
            result.moveToNext();
            i++;
        };
        return  tripsNames;
    }
    public ArrayList<String> readFromTripPast(){
        //trips=new TripInfo();
        ArrayList<String> tripsNames=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        String query ="select * from trip where past=?";
        String past="t";
        Cursor result=db.rawQuery(query,new String[]{past});

        result.moveToFirst();
        int i =0;
        String tripName ;
        while (result.isAfterLast()==false){
            tripName =result.getString(result.getColumnIndex("trip_name"));
            tripsNames.add(tripName);
            result.moveToNext();
            i++;
        };
        return  tripsNames;
    }

    public Trip returnTrip(int userId)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query = "select * from trip where userID =?";
        Cursor result=db.rawQuery(query,new String[]{String.valueOf(userId)});
        result.moveToFirst();
        Trip t = new Trip();

        t.setFrom(result.getString(result.getColumnIndex("trip_start_place")));
        t.setCost(result.getFloat(result.getColumnIndex("tripCost")));
        t.setDetails(result.getString(result.getColumnIndex("details")));
        t.setIdTrip(result.getInt(result.getColumnIndex("tripId")));
        t.setTime(result.getString(result.getColumnIndex("trip_time")));
        t.setTripName(result.getString(result.getColumnIndex("trip_name")));
        t.setTo(result.getString(result.getColumnIndex("trip_end_place")));
        t.setDay(result.getString(result.getColumnIndex("day")));
        return t;
    }


}
