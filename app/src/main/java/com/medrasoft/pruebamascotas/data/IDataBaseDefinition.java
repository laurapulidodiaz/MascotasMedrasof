package com.medrasoft.pruebamascotas.data;

import android.database.sqlite.SQLiteDatabase;

public interface IDataBaseDefinition {
    public abstract String getTable();
    public abstract String getIdColumn();
    public abstract String startInsertStatement();
    public abstract void afterCreateQuery(SQLiteDatabase db);
}
