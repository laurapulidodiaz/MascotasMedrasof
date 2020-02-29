package com.medrasoft.pruebamascotas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseManager extends SQLiteOpenHelper {
    public static int NEW_VERSION = 2;
    public static final String DATABASE_NAME = "mascota.db";
    public static DatabaseManager activeConection;
    public static SQLiteDatabase dbWritable;
    public static SQLiteDatabase dbRedable;
    private final static String DB_PATH = "/data/data/com.medrasoft.pruebamascotas/databases/";

    Context context;
    File dbFile;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, NEW_VERSION);
        this.context = context;
        dbFile= new File(DB_PATH + DATABASE_NAME);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {

        if(!dbFile.exists()){
            dbWritable = super.getWritableDatabase();
            dbWritable.disableWriteAheadLogging();
            copyDataBase(dbWritable.getPath());
        }
        return super.getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if(!dbFile.exists()){
            dbRedable = super.getReadableDatabase();
            dbRedable.disableWriteAheadLogging();
            copyDataBase(dbRedable.getPath());
        }
        return super.getReadableDatabase();
    }

    private void copyDataBase(String dbPath){
        try{
            InputStream assestDB = context.getAssets().open(DATABASE_NAME);
            OutputStream appDB = new FileOutputStream(DB_PATH,false);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = assestDB.read(buffer)) > 0) {
                appDB.write(buffer, 0, length);
            }
            appDB.flush();
            appDB.close();
            assestDB.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


   /* public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, NEW_VERSION);
    }*/

    public static DatabaseManager getActiveConection(Context context){
        if(activeConection==null){
            activeConection = new DatabaseManager(context);
        }
        return activeConection;
    }

    public static DatabaseManager getActiveConection(){
        return activeConection;
    }

    public static SQLiteDatabase getWritableDatabaseManaged(Context context){
        if(dbWritable==null){
            dbWritable = DatabaseManager.getActiveConection(context).getWritableDatabase();
            dbWritable.disableWriteAheadLogging();
        }
        return dbWritable;
    }

    public static SQLiteDatabase getWritableDatabaseManaged(){
        if(dbWritable==null){
            dbWritable = DatabaseManager.getActiveConection().getWritableDatabase();
            dbWritable.disableWriteAheadLogging();
        }
        return dbWritable;
    }

    public static SQLiteDatabase getReadableDatabaseManaged(Context context){
        if(dbRedable==null){
            dbRedable = DatabaseManager.getActiveConection(context).getReadableDatabase();
            dbRedable.disableWriteAheadLogging();
        }
        return dbRedable;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.disableWriteAheadLogging();
        database.execSQL("DROP TABLE " + PropietarioDB.TABLE + "");
        database.execSQL(PropietarioDB.createStatement());
        database.execSQL("DROP TABLE " + MascotaDB.TABLE + "");
        database.execSQL(MascotaDB.createStatement());
        database.execSQL("DROP TABLE " + VacunaDB.TABLE + "");
        database.execSQL(VacunaDB.createStatement());
    }

    private void executeStatement(SQLiteDatabase database, StringBuffer sb){
        try {
            database.execSQL(sb.toString());
        }catch (Exception e){
            System.out.println("ERROR AL CREAR LA BASE: "+e.getMessage());
        }
    }
    @Override
    public void onDowngrade(SQLiteDatabase database, int version, int newVersion) {
        this.onUpgrade(database, version, version+1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version, int newVersion) {
        if(version != newVersion){
            onCreate(database);
        }
    }
}
