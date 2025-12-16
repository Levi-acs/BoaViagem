package com.example.boaviagem.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.boaviagem.AnotacaoListener;
import com.example.boaviagem.Constantes;
import com.example.boaviagem.provider.BoaViagemContract;


import java.util.Arrays;
import java.util.List;

public class ViagemListFragment extends ListFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private AnotacaoListener callback;
    private SimpleCursorAdapter adapter;


    @Override
    public void onStart(){
        super.onStart();
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1,null,
                new String[]{BoaViagemContract.Viagem.DESTINO},
                new int[]{android.R.id.text1},0);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id){
        long viagem = getListAdapter().getItemId(position);
        Bundle bundle = new Bundle();
        bundle.putLong(Constantes.VIAGEM_SELECIONADA,viagem);
        callback.viagemSelecionada(bundle);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        callback = (AnotacaoListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        Uri uri = BoaViagemContract.Viagem.CONTENT_URI;
        String[] projection = new String[]{BoaViagemContract.Viagem._ID, BoaViagemContract.Viagem.DESTINO};
        return new CursorLoader(getActivity(),uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        adapter.swapCursor(null);
    }
}
