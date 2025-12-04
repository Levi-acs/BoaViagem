package com.example.boaviagem.provider;

import static com.example.boaviagem.provider.BoaViagemContract.AUTHORITY;
import static com.example.boaviagem.provider.BoaViagemContract.GASTO_PATH;
import static com.example.boaviagem.provider.BoaViagemContract.VIAGEM_PATH;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.boaviagem.DatabaseHelper;

public class BoaViagemProvider  extends ContentProvider {

    private DatabaseHelper helper;
    private static final int VIAGENS = 1;
    private static final int VIAGEM_ID = 2;
    private static final int GASTOS = 3;
    private static final int GASTO_ID = 4;
    private static final int GASTOS_VIAGEM_ID = 5;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,VIAGEM_PATH,VIAGENS);

        uriMatcher.addURI(AUTHORITY,
                VIAGEM_PATH + "/#",
                VIAGEM_ID);

        uriMatcher.addURI(AUTHORITY,
                GASTO_PATH,
                GASTOS);

        uriMatcher.addURI(AUTHORITY,
                GASTO_PATH + "/#",
                GASTO_ID);

        uriMatcher.addURI(AUTHORITY,
                GASTO_PATH + "/"+ VIAGEM_PATH + "/#",
                GASTOS_VIAGEM_ID);
    }

    @Override
    public boolean onCreate(){
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,String sortOrder){
        SQLiteDatabase database = helper.getReadableDatabase();

        switch (uriMatcher.match(uri)){
            case VIAGENS:
                return database.query(VIAGEM_PATH,projection,selection,selectionArgs,null,null,sortOrder);
            case VIAGEM_ID:
                selection = DatabaseHelper.Viagem._ID + "= ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return database.query(VIAGEM_PATH,projection,
                        selection,selectionArgs,null,null,sortOrder);
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        return 0;
    }

    @Override
    public	String	getType(Uri	uri) {
        return null;
    }



}
