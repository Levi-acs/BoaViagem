package com.example.boaviagem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.boaviagem.R;
import com.example.boaviagem.domain.Anotacao;

public class Anotacaofragment extends Fragment implements View.OnClickListener {

    private EditText dia, titulo, descricao;
    private Button botaoSalvar;
    private Anotacao anotacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.anotacao,container,false);
    }

    @Override
    public void onStart(){
        super.onStart();

        dia = (EditText) getActivity().findViewById(R.id.dia);
        titulo = (EditText) getActivity().findViewById(R.id.titulo);
        descricao = (EditText) getActivity().findViewById(R.id.descricao);
        botaoSalvar = (Button) getActivity().findViewById(R.id.salvar);
        botaoSalvar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

    }

}

