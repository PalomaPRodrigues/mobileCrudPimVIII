package com.example.ExemploCrud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.crudunip.R;

import java.util.ArrayList;
import java.util.List;

public class ListarPessoasActivity extends AppCompatActivity {

    private ListView listView;
    private  PessoaDao dao;
    private List<Pessoa> pessoas;
    private final List<Pessoa> pessoasFiltradas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pessoas);

        listView= findViewById(R.id.lista_pessoas);
        dao = new PessoaDao(this);
        pessoas = dao.obterTodos();
        pessoasFiltradas.addAll(pessoas);
        ArrayAdapter<Pessoa> adaptador = new ArrayAdapter<Pessoa>(this, android.R.layout.simple_list_item_1, pessoasFiltradas);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_procurar).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraPessoa(s);
                return false;
            }
        });

        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal,menu);
    }

     public void procuraPessoa(String nome){
        pessoasFiltradas.clear();
        for(Pessoa p: pessoas){
            if(p.getNome().toLowerCase().contains(nome.toLowerCase())){
                pessoasFiltradas.add(p);
            }
        }
        listView.invalidateViews();
  }

    public void excluir(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

      final Pessoa pessoaExcluir = pessoasFiltradas.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir a pessoa ?")
                .setNegativeButton("NÃO", null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       pessoasFiltradas.remove(pessoaExcluir);
                       pessoas.remove(pessoaExcluir);
                       dao.excluir(pessoaExcluir);
                       listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void cadastrar(MenuItem item){
        Intent it = new Intent(this, CadastroActivity.class);
        startActivity(it);
    }

    public void atualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Pessoa pessoaAtualizar = pessoasFiltradas.get(menuInfo.position);
        Intent it = new Intent(this, CadastroActivity.class);
        it.putExtra("pessoa", pessoaAtualizar);
        startActivity(it);


    }
    @Override
    public void onResume(){
        super.onResume();
        pessoas = dao.obterTodos();
        pessoasFiltradas.clear();
        pessoasFiltradas.addAll(pessoas);
        listView.invalidateViews();
    }
}