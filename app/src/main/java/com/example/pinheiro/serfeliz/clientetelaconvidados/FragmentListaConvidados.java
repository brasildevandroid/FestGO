package com.example.pinheiro.serfeliz.clientetelaconvidados;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pinheiro.serfeliz.R;
import com.example.pinheiro.serfeliz.bancointerno.BD;
import com.example.pinheiro.serfeliz.bancointerno.Usuario;
import com.example.pinheiro.serfeliz.ex16_fragments.HotelDetalheActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListaConvidados extends Fragment {

    AdapterListaConvidados adapterListaContatos;
    private static ArrayList<Convidado> listaConvidados;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    //static  private LineAdapterLojas mAdapter;
    static private FirebaseFirestore mFirestore;
    static private FirebaseUser mUser;
    TextView txtTotalConvidado,txtTotalConfirmados;
    BD bd;

    public FragmentListaConvidados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_lista_convidados,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerListaConvidados);
        txtTotalConvidado = (TextView)view.findViewById(R.id.txt_Total_Convidados);
        txtTotalConfirmados = (TextView) view.findViewById(R.id.txt_Convidados_Confirmados);


        bd = new BD(getContext());
        List<Usuario> list = bd.buscar();
        Usuario user =  (Usuario) list.get(0);
        String confirmado = user.getConfirmados();
        txtTotalConfirmados.setText(confirmado);
        buildRecyclerView();


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();


        buildRecyclerView();


    }

    private void buildRecyclerView() {


        listaConvidados = new ArrayList<>();

        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String userUid = mUser.getUid();


           adapterListaContatos = new AdapterListaConvidados(listaConvidados);


        mFirestore.collection("cliente")
                .document(userUid)
                .collection("convidados")

                //.whereEqualTo("status", "aberta")


                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("documentos", document.getId() + " => " + document.getData());

                                Convidado convidado = document.toObject(Convidado.class);
                                listaConvidados.add(convidado);

                                txtTotalConvidado.setText(String.valueOf(listaConvidados.size()));

                                // Configurando o gerenciador de layout para ser uma lista.
                                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                                        2, GridLayoutManager.VERTICAL, false));


                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                mRecyclerView.setLayoutManager(layoutManager);

                                // Adiciona o adapter que irá anexar os objetos à lista.
                                // Está sendo criado com lista vazia, pois será preenchida posteriormente.

                                mLayoutManager = new LinearLayoutManager(getContext());

                                mRecyclerView.setHasFixedSize(true);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(adapterListaContatos);


                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });


        adapterListaContatos.setOnItemClickListener(new AdapterListaConvidados.OnItemClickListener() {
            @Override
            public void onMensagemCliente(int position) {

                mostraDetalhesConvidado(position);
            }

            @Override
            public void onLigacaoCliente(int position) {

            }

            @Override
            public void onAlterarStatusColeta(int position) {

            }

            @Override
            public void onNavegarCliente(int position) {

            }
        });

    }

    private void mostraDetalhesConvidado(int position) {


        Convidado convidado = (Convidado) listaConvidados.get(position);

        Toast.makeText(getContext(), "detalhes " + convidado.getCelularConvidado(), Toast.LENGTH_SHORT).show();

        Intent it = new Intent(getContext(), ConvidadoDetalheActivity.class);
        it.putExtra(ConvidadoDetalheActivity.EXTRA_CONTATO,convidado);
        startActivity(it);


        adapterListaContatos.notifyDataSetChanged();

    }

}
