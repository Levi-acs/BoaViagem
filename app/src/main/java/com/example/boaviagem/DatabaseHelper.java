package com.example.boaviagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.PortUnreachableException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS =   "BoaViagem";
    private static int VERSAO = 1;

    public static class Viagem{
        public static final String TABELA = "VIAGEM";
        public static final String _ID = "_ID";
        public static final String DESTINO = "DESTINO";
        public static final  String DATA_CHEGADA = "DATA_CHEGADA";
        public  static final String DATA_SAIDA = "DATA_SAIDA";
        public  static final String ORCAMENTO = "ORCAMENTO";
        public static final String QUANTIDADE_PESSOAS = "QUANTIDADE_PESSOAS";
        public static final String  TIPO_VIAGEM = "TIPO_VIAGEM";

        public static final String[] COLUNAS = new String[]{
                _ID, DESTINO, DATA_CHEGADA, DATA_SAIDA, TIPO_VIAGEM
                , ORCAMENTO, QUANTIDADE_PESSOAS
        };
    }

    public static class Gasto{
        public static final String TABELA = "GASTO";
        public static final String _ID = "_ID";
        public static final String VIAGEM_ID = "VIAGEM_ID";
        public static final String CATEGORIA = "CATEGORIA";
        public static final String DATA =  "DATA";
        public static final String DESCRICAO = "DESCRICAO";
        public static final String VALOR = "VALOR";
        public static final String LOCAL = "LOCAL";


        public static final String[] COLUNAS = new String[]{
                _ID, VIAGEM_ID, CATEGORIA, DATA, DESCRICAO, VALOR, LOCAL
        };
    }
    public DatabaseHelper(Context context){
        super(context,BANCO_DADOS,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE viagem(_id INTEGER PRIMARY KEY,"+" destino TEXT, tipo_viagem INTEGER," +
                "data_chegada DATE, data_saida DATE," +
                " orcamento DOUBLE, quantidade_pessoas INTEGER);");

        db.execSQL("CREATE TABLE gasto (_id INTEGER PRIMARY KEY," +
                "categoria TEXT, data DATE, valor DOUBLE," +
                "descricao TEXT, local TEXT, viagem_id INTEGER," +
                "FOREIGN KEY(viagem_id)REFERENCES viagem(_id))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

}
