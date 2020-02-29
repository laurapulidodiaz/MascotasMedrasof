package com.medrasoft.pruebamascotas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DataBaseDefinition implements IDataBaseDefinition {
    String id = "id";
    String table = null;
    String[] columns = null;

    public DataBaseDefinition(){
        super();
        getTable();
        getColumns();
    }

    @Override
    public String getTable() {
        if(table == null) {
            try {
                Field field = this.getClass().getField("TABLE");
                table = field.get(null) + "";
            } catch (NoSuchFieldException ex) {
                System.out.println(ex.getMessage());
                table = "NO_TABLE_DEFINED_ON_" + this.getClass().getName();
            } catch (IllegalAccessException aex) {
                System.out.println(aex.getMessage());
                table = "TABLE_INACCESSIBLE_" + this.getClass().getName();
            }
        }
        return table;
    }

    @Override
    public String getIdColumn() {
        return id;
    }

    public String startInsertStatement(){ return "";}

    public void afterCreateQuery(SQLiteDatabase db){ }

    public String[] getColumns(){
        if(this.columns==null) {
            Field[] fields = this.getClass().getDeclaredFields();
            this.columns = new String[fields.length - 1];
            int i = 0;
            for (Field field : fields) {
                if (!field.getName().equals("TABLE")) {
                    columns[i] = field.getName().toLowerCase();
                    i++;
                }
            }
        }
        return columns;
    }

    public String dropStatement(){
        return "DROP TABLE IF EXISTS "+getTable();
    }

    public int getNextId(Context context, SQLiteDatabase db) {
        if(db==null){
            db = DatabaseManager.getActiveConection().getReadableDatabase();
        }
        Cursor res = db.rawQuery("select CASE WHEN max("+getIdColumn()+") IS NULL THEN 1 ELSE max("+getIdColumn()+") END AS "+getIdColumn()+" from "+getTable(),null);
        if(res.getCount()<0){
            return 1;
        } else {
            res.moveToFirst();
            return (res.getInt(0)+1);
        }
    }

    public boolean insertData(Context context, HashMap<String,String> params) {
        try {
            SQLiteDatabase db = DatabaseManager.getWritableDatabaseManaged(context);
            ContentValues contentValues = new ContentValues();
            contentValues.put(getIdColumn(),getNextId(context,db));
            for(int i=0; i<getColumns().length; i++){
                contentValues.put(getColumns()[i], params.get(getColumns()[i]));
            }
            db.insertOrThrow(getTable(), null, contentValues);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int getNextId(SQLiteDatabase db) {
        if(db==null){
            db = DatabaseManager.getActiveConection().getReadableDatabase();
        }
        Cursor res = db.rawQuery("select CASE WHEN max("+getIdColumn()+") IS NULL THEN 1 ELSE max("+getIdColumn()+") END AS "+getIdColumn()+" from "+getTable(),null);
        if(res.getCount()<0){
            return 1;
        } else {
            res.moveToFirst();
            return (res.getInt(0)+1);
        }
    }

    public boolean insertData(HashMap<String,String> params, SQLiteDatabase db) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(getIdColumn(),getNextId(db));
            for(int i=0; i<getColumns().length; i++){
                contentValues.put(getColumns()[i], params.get(getColumns()[i]));
            }
            db.insertOrThrow(getTable(), null, contentValues);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<?> getAllDataObjects(Context context, Class klazz, String condition, String[] params) {
        List objects = new ArrayList();
        Cursor cursor = null;
        if(condition == null) {
            cursor = getAllData(context);
        } else {
            cursor = getData(context, condition, params);
        }
        try {
            if(cursor.moveToNext()) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Constructor constructor = klazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object object = klazz.newInstance();
                    set(object, getIdColumn(), Integer.parseInt(cursor.getString(cursor.getColumnIndex(getIdColumn()))));
                    for (String column : getColumns()) {
                        final String fieldName = columnToCamelCase(column);
                        Field field = object.getClass().getDeclaredField(fieldName);
                        Type type = field.getType();
                        if (type == String.class) {
                            set(object, fieldName, cursor.getString(cursor.getColumnIndex(column)));
                        } else if (type == Double.class || type == double.class) {
                            set(object, fieldName, cursor.getDouble(cursor.getColumnIndex(column)));
                        } else if (type == Integer.class || type == int.class) {
                            set(object, fieldName, cursor.getInt(cursor.getColumnIndex(column)));
                        } else if (type == Short.class || type == short.class) {
                            set(object, fieldName, cursor.getShort(cursor.getColumnIndex(column)));
                        } else if (type == Long.class || type == long.class) {
                            set(object, fieldName, cursor.getLong(cursor.getColumnIndex(column)));
                        } else {
                            set(object, fieldName, cursor.getString(cursor.getColumnIndex(column)));
                        }
                    }
                    objects.add(object);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return objects;
    }



    public int getAllDataCount(Context context, String condition, String[] params) {
        int response = 0;
        Cursor cursor = getDataCount(context, condition, params);
        try {
            if(cursor.moveToNext()) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    response = cursor.getInt(0);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return response;
    }

    public List<?> getAllDataObjects(Context context, Class klazz, String sql) {
        List objects = new ArrayList();
        Cursor cursor = getData(context, sql);
        try {
            if(cursor.moveToNext()) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Constructor constructor = klazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object object = klazz.newInstance();
                    set(object, getIdColumn(), Integer.parseInt(cursor.getString(cursor.getColumnIndex(getIdColumn()))));
                    for (String column : getColumns()) {
                        final String fieldName = columnToCamelCase(column);
                        Field field = object.getClass().getDeclaredField(fieldName);
                        Type type = field.getType();
                        if (type == String.class) {
                            set(object, fieldName, cursor.getString(cursor.getColumnIndex(column)));
                        } else if (type == Double.class || type == double.class) {
                            set(object, fieldName, cursor.getDouble(cursor.getColumnIndex(column)));
                        } else if (type == Integer.class || type == int.class) {
                            set(object, fieldName, cursor.getInt(cursor.getColumnIndex(column)));
                        } else if (type == Short.class || type == short.class) {
                            set(object, fieldName, cursor.getShort(cursor.getColumnIndex(column)));
                        } else if (type == Long.class) {
                            set(object, fieldName, cursor.getLong(cursor.getColumnIndex(column)));
                        } else {
                            set(object, fieldName, cursor.getString(cursor.getColumnIndex(column)));
                        }
                    }
                    objects.add(object);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return objects;
    }

    public Cursor getData(Context context, String sql) {
        SQLiteDatabase db = DatabaseManager.getReadableDatabaseManaged(context);
        return db.rawQuery(sql, null);
    }

    public Cursor getData(Context context, String condition, String[] params) {
        SQLiteDatabase db = DatabaseManager.getReadableDatabaseManaged(context);
        return db.rawQuery("select * from "+getTable()+" WHERE "+condition, params);
    }

    public Cursor getDataCount(Context context, String condition, String[] params) {
        SQLiteDatabase db = DatabaseManager.getReadableDatabaseManaged(context);
        return db.rawQuery(condition, params);
    }

    public Cursor getAllData(Context context) {
        SQLiteDatabase db = DatabaseManager.getReadableDatabaseManaged(context);
        return db.rawQuery("select * from "+getTable(),null);
    }

    public boolean updateData(Context context, HashMap<String,String> params) {
        try {
            SQLiteDatabase db = DatabaseManager.getWritableDatabaseManaged(context);
            ContentValues contentValues = new ContentValues();
            for(int i=0; i<getColumns().length; i++){
                contentValues.put(getColumns()[i], params.get(getColumns()[i]));
            }
            db.update(getTable(), contentValues, getIdColumn() + " = ?",new String[] {params.get(getIdColumn())});
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Integer deleteData(Context context, String condition, String[] params) {
        SQLiteDatabase db = DatabaseManager.getWritableDatabaseManaged(context);
        return db.delete(getTable(), condition, params);
    }

    public Integer deleteData(Context context, String id) {
        SQLiteDatabase db = DatabaseManager.getWritableDatabaseManaged(context);
        return db.delete(getTable(), getIdColumn() + " = ?", new String[] {id});
    }

    public static boolean set(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public String columnToCamelCase(String column){
        if(column.indexOf("_")>=0) {
            StringBuffer columnname = new StringBuffer();
            char[] letters = column.toCharArray();
            boolean up = false;
            for (char c : letters) {
                if (c == '_') {
                    up = true;
                } else {
                    if (up) {
                        String val = c + "";
                        columnname.append(val.toUpperCase());
                        up = false;
                    } else {
                        columnname.append(c);
                    }
                }
            }
            column = columnname.toString();
        }
        return column;
    }
}