package com.example.ExemploCrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crudunip.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText endereco;
    private EditText telefone;
    private PessoaDao dao;
    private Pessoa pessoa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        endereco = findViewById(R.id.editEndereco);
        telefone = findViewById(R.id.editTelefone);
        dao =  new PessoaDao(this);

        Intent it = getIntent();
        if(it.hasExtra("pessoa")){
            pessoa = (Pessoa) it.getSerializableExtra("pessoa");
            nome.setText(pessoa.getNome());
            cpf.setText(pessoa.getCpf());
            endereco.setText(pessoa.getEndereco());
            telefone.setText(pessoa.getTelefone());
        }
    }
    public void salvar(View view){

        if(pessoa == null){
           pessoa = new Pessoa();
           pessoa.setNome(nome.getText().toString());
           pessoa.setCpf(cpf.getText().toString());
           pessoa.setEndereco(endereco.getText().toString());
           pessoa.setTelefone(telefone.getText().toString());
           long id = dao.inserir(pessoa);
           Toast.makeText(this, "Pessoa inserida com id: " + id, Toast.LENGTH_SHORT).show();
        }else{
            pessoa.setNome(nome.getText().toString());
            pessoa.setCpf(cpf.getText().toString());
            pessoa.setEndereco(endereco.getText().toString());
            pessoa.setTelefone(telefone.getText().toString());
            dao.atualizar(pessoa);
            Toast.makeText(this, "Pessoa foi atualizado", Toast.LENGTH_SHORT).show();
        }
    }

}