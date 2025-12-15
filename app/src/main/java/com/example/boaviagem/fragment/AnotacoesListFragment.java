package com.example.boaviagem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.ListFragment;

import com.example.boaviagem.Constantes;
import com.example.boaviagem.R;
import com.example.boaviagem.domain.Anotacao;

import java.util.ArrayList;
import java.util.List;

public class AnotacoesListFragment  extends ListFragment
implements AdapterView.OnItemClickListener, View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanteState){
        return inflater.inflate(R.layout.lista_anotacoes,container,false);
    }

    @Override
    public void onStart(){
        super.onStart();
        Button button = (Button) getActivity().findViewById(R.id.nova_anotacao);
        button.setOnClickListener(this);
        getListView().setOnItemClickListener(this);
        listarAnotacoesPorViagem(getArguments());
    }
public void listarAnotacoesPorViagem(Bundle bundle){
        if(bundle != null && bundle.containsKey(Constantes.VIAGEM_SELECIONADA)){

            List<Anotacao> anotacoes = listarAnotacoes();

            ArrayAdapter<Anotacao> adapter =
                    new ArrayAdapter<Anotacao>(getActivity(),
                            android.R.layout.simple_list_item_1,
                            anotacoes);

            setListAdapter(adapter);
        }
}
    private List<Anotacao> listarAnotacoes(){
        List<Anotacao> anotacoes = new ArrayList<Anotacao>();

        for	(int	i	=	1;	i	<=	20;	i++)	{
            Anotacao	anotacao	=	new	Anotacao();
            anotacao.setDia(i);
            anotacao.setTitulo("Anotacao	"	+	i);
            anotacao.setDescricao("Descrição	"	+	i);
            anotacoes.add(anotacao);
        }
        return anotacoes;
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view, int position,long id){

    }

    @Override
    public void onClick(View view){

    }
}
