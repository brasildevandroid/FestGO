package com.example.pinheiro.serfeliz;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pinheiro.serfeliz.R;
import com.example.pinheiro.serfeliz.bancointerno.BD;
import com.example.pinheiro.serfeliz.bancointerno.Usuario;
import com.example.pinheiro.serfeliz.clientetelaconvidados.TelaConvidado;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

Button btnAdicionarContato;
TextView textView;
Cursor c;
ArrayList<String> contacts;
ImageView imgFotoCapaResumo,imgFotoPerfil;

LinearLayout liResumoConvidados;

    private String EVENT_DATE_TIME = "19-04-19 10:30:00";
    private String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private String DATE_FORMAT_DATA = "dd-MM-yyyy";

    private TextView txtDias, txtHoras, txtMinutos, txtSegundos,txtOrcamento,txtDataFesta,txtMensagemFesta,txtResumoConvidadosConfirmados;
    private Handler handler = new Handler();
    private Runnable runnable;
    BD bd;
    private static final int PICK_IMAGE = 100;



    private static final int  PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);


        Usuario usuario = new Usuario();

        txtDias = (TextView) view.findViewById(R.id.txt_Dias);
        txtHoras = (TextView) view.findViewById(R.id.txt_Horas);
        txtMinutos = (TextView) view.findViewById(R.id.txt_Minutos);
        txtSegundos = (TextView) view.findViewById(R.id.txt_Segundos);
        txtOrcamento= (TextView) view.findViewById(R.id.txt_orcamento);
        txtDataFesta = (TextView)view.findViewById(R.id.txt_Data_Festa);
        txtMensagemFesta = (TextView)view.findViewById(R.id.txt_Mensagem_Festa);
        txtResumoConvidadosConfirmados = (TextView)view.findViewById(R.id.txt_Resumo__Convidados_Confirmados);
        imgFotoCapaResumo  =(ImageView) view.findViewById(R.id.foto_Capa_Resumo);
        imgFotoPerfil = (ImageView)view.findViewById(R.id.foto_Perfil);

        imgFotoCapaResumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               inserirImagemCapa();
            }
        });

        imgFotoCapaResumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthUI.getInstance()
                        .signOut(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                startActivity(new Intent(getContext(),TelaEspera.class));
                                getActivity().finish();

                                // ...
                            }
                        });

            }
        });


        txtMensagemFesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(),TelaEditarMensagem.class));
            }
        });

        atualizarResumo();
        countDownStart();


        /* copiar o seguinte exemplo
                 * https://www.youtube.com/watch?v=IXs9zo687qA
                 *
                 * */

        liResumoConvidados  = (LinearLayout)view.findViewById(R.id.li_Resumo_Convidados);

        liResumoConvidados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),TelaConvidado.class));


            }

        });


        return view;
    }

    // link de referência desse trecho de código
    // https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android

    private void countDownStart() {

        runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    handler.postDelayed(this, 1000);


                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date;
                    current_date = new Date();



                    if (!current_date.after(event_date)) {

                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;


                        txtDias.setText(String.format("%02d", Days));
                        txtHoras.setText(String.format("%02d", Hours));
                        txtMinutos.setText(String.format("%02d", Minutes));
                        txtSegundos.setText(String.format("%02d", Seconds));

                    } else {


                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);

    }


    public void onPause() {
        super.onPause();

        countDownStart();
        atualizarResumo();
    }

    private void atualizarResumo() {



            try{

                bd = new BD(getContext());

                List<Usuario> list = bd.buscar();

                Usuario user =  (Usuario) list.get(0);

                txtOrcamento.setText(user.getOrcamento());

                EVENT_DATE_TIME = user.getDataFestaRegressiva();

                txtDataFesta.setText(EVENT_DATE_TIME);

                txtMensagemFesta.setText(user.getMensagem());

                txtResumoConvidadosConfirmados.setText(user.getConfirmados()  + " confirmados");
                //  Toast.makeText(getContext(),String.valueOf(user.getImgCapa()),Toast.LENGTH_SHORT).show();


            }catch (Exception e){

            e.printStackTrace();
        }


    }


    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }





    public void inserirImagemCapa(){


        Random rand = new Random();
        int aleatorio = 500000000;
        int  n = rand.nextInt((aleatorio) + 1);

        Intent intent
                 = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));

            startActivityForResult(intent,PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE){

            try{

                bd = new BD(getContext());

                List<Usuario> list = bd.buscar();

                Usuario user =  (Usuario) list.get(0);


                Uri uri = data.getData();
                String x  = getPatch(uri);
               // bd.inserirImagens(user,x);
                Toast.makeText(getContext(),x,Toast.LENGTH_SHORT).show();

                Glide.with(getContext()).load(x).into(imgFotoCapaResumo);


            }catch (Exception e){
                e.printStackTrace();

            }


        }
    }

    public String getPatch(Uri uri){

        if (uri == null)return  null;

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor =
                 getActivity().managedQuery(uri,projection,null,null,null);

        if (cursor != null){

            int column_index = cursor
                               .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();

    }

}
