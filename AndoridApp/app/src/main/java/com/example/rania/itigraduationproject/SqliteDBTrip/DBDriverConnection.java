package com.example.rania.itigraduationproject.SqliteDBTrip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rania.itigraduationproject.model.Trip;

import java.util.ArrayList;

public class DBDriverConnection extends SQLiteOpenHelper {
    public static final String db_name="driverTrip.sql";

    public DBDriverConnection(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS driverTrip (id INTEGER primary key," +
                " idTrip INTEGER,tripPast TEXT,tripName TEXT,tripFrom TEXT,tripTo TEXT,toLongtiude DOUBLE , toLatitude DOUBLE , fromLatitude DOUBLE , fromLongtiude DOUBLE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table IF EXISTS driverTrip");
        onCreate(db);

    }


    public void insertIntoTrip(Integer tripId, String tripName , String tripFrom , String tripTo , Double toLongtiude,
                               Double toLatitude , Double fromLatitude , Double fromLongtiude ){

        SQLiteDatabase query=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("idTrip",tripId);
        values.put("tripPast","f");
        values.put("tripName",tripName);
        values.put("tripFrom",tripFrom);
        values.put("tripTo",tripTo);
        values.put("toLongtiude",toLongtiude);
        values.put("toLatitude",toLatitude);
        values.put("fromLatitude",fromLatitude);
        values.put("fromLongtiude",fromLongtiude);
        System.out.println("a*AAAAAAAAAAAAA*AAAAAAAAAAA*AAAAAAAAAA"+toLatitude);
        query.insert("driverTrip",null,values);
        Log.i("test","row inserted");

    }

    public ArrayList<Trip> readFromTripDriverRecent(){
        //trips=new TripInfo();
        ArrayList<Trip> tripS=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        String query ="select * from driverTrip where tripPast=?";
        String past="f";
        Cursor result=db.rawQuery(query,new String[]{past});

        result.moveToFirst();
        int i =0;

        while (result.isAfterLast()==false){
            Trip trip = new Trip();
            trip.setTripName(result.getString(result.getColumnIndex("tripName")));
            trip.setIdTrip(result.getInt(result.getColumnIndex("idTrip")));
            tripS.add(i,trip);
            result.moveToNext();
            System.out.println("***"+i+"****IN****"+tripS.get(i).getTripName());
            i++;
        }
        return  tripS;
    }
    public ArrayList<Trip> readFromTripPast(){
        //trips=new TripInfo();
        ArrayList<Trip> trips=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        String query ="select * from driverTrip where tripPast=?";
        String past="t";
        Cursor result=db.rawQuery(query,new String[]{past});

        result.moveToFirst();
        int i =0;
        Trip tripName = new Trip();
        while (result.isAfterLast()==false){
            tripName.setTripName(result.getString(result.getColumnIndex("tripName")));
            tripName.setIdTrip(result.getInt(result.getColumnIndex("idTrip")));
            trips.add(i,tripName);
            result.moveToNext();
            i++;
        };
        return  trips;
    }

    public Trip getTrip(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query ="select * from driverTrip where idTrip=?";
        Cursor result=db.rawQuery(query,new String[]{""+id});

        result.moveToFirst();
        Trip trip = new Trip();
        trip.setTripName(result.getString(result.getColumnIndex("tripName")));
        trip.setIdTrip(result.getInt(result.getColumnIndex("idTrip")));
        trip.setFrom(result.getString(result.getColumnIndex("tripFrom")));
        trip.setTo(result.getString(result.getColumnIndex("tripTo")));
        trip.setEndlatitude(result.getDouble(result.getColumnIndex("toLatitude")));
        trip.setEndlongtiude(result.getDouble(result.getColumnIndex("toLongtiude")));
        trip.setStartlongtiude(result.getDouble(result.getColumnIndex("fromLongtiude")));
        trip.setStartlatitude(result.getDouble(result.getColumnIndex("fromLatitude")));

        System.out.println("AAAAAAAAAAAAAAAAAA*****************"+trip.getStartlatitude()+"kkkk"+id);

       return trip;
    }

    public void setTripToBePast(int id) {
        SQLiteDatabase query=this.getWritableDatabase();
        ContentValues  values=new ContentValues();
        values.put("tripPast","t");
        query.update("driverTrip",values,"idTrip="+id,null);
        Log.i("test","row upated");
    }
}
