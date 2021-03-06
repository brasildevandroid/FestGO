package com.example.pinheiro.serfeliz.bancointerno;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinheiro.serfeliz.R;

public class NewUserActivity extends Activity {
	private Usuario usuario = new Usuario();
	private EditText nomeEt;
	private EditText emailEt;
	private EditText senhaEt;
	private Button salvarBt;
	private Button editarBt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		
		nomeEt = (EditText) findViewById(R.id.nome);
		emailEt = (EditText) findViewById(R.id.email);
		senhaEt = (EditText) findViewById(R.id.senha);
		salvarBt = (Button) findViewById(R.id.button1);
		editarBt = (Button) findViewById(R.id.button2);



		/*

		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				
				usuario.setId(bundle.getLong("id"));
				usuario.setNome(bundle.getString("nome"));
				usuario.setEmail(bundle.getString("email"));
				
				nomeEt.setText(usuario.getNome());
				emailEt.setText(usuario.getEmail());
				
				senhaEt.setVisibility(View.GONE);
				salvarBt.setVisibility(View.GONE);
				editarBt.setVisibility(View.VISIBLE);
			}
		}

		*/
	}
	
	
	public void salvarUsuario(View view){

		usuario.setNome(nomeEt.getText().toString());
		usuario.setEmail(emailEt.getText().toString());

		
		BD bd = new BD(this);
		bd.inserir(usuario);
		
		Toast.makeText(this, "Usuario inserido com sucesso!", Toast.LENGTH_SHORT).show();

        Log.d("usuario","dados usuario" + usuario);

	}

	
	
	public void editarUsuario(View view){
		usuario.setNome(nomeEt.getText().toString());
		usuario.setEmail(emailEt.getText().toString());
		
		BD bd = new BD(this);
		bd.atualizar(usuario);
		
		Toast.makeText(this, "Usuario \""+usuario.getNome()+"\" atuailizado com sucesso.", Toast.LENGTH_SHORT).show();
	}

}
