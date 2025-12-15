package com.example.boaviagem.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;

import java.util.Arrays;
import java.util.List;

public class ViagemListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public void onStart(){
        super.onStart();
        List<String> viagens = Arrays.asList("campo grande", "São Paulo", "Miami");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,viagens);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id){

    }
}
