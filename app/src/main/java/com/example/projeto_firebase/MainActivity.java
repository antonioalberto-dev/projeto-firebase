package com.example.projeto_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projeto_firebase.modelo.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtMatricula;
    ListView listV_dados;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Aluno> list_Aluno = new ArrayList<Aluno>();
    private ArrayAdapter<Aluno> arrayAdapterAluno;

    Aluno alunoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNome = (EditText) findViewById(R.id.editNome);
        edtMatricula = (EditText) findViewById(R.id.editMatricula);
        listV_dados = (ListView) findViewById(R.id.listV_dados);

        inicializarFirebase();
        eventoDatabase();

        listV_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alunoSelecionado = (Aluno) adapterView.getItemAtPosition(i);
                edtNome.setText(alunoSelecionado.getName());
                edtMatricula.setText(alunoSelecionado.getRegistration());
            }
        });
    }

    private void eventoDatabase(){
        databaseReference.child("Aluno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_Aluno.clear();
                for (DataSnapshot objSnapshot:snapshot.getChildren()){
                    Aluno aluno = objSnapshot.getValue(Aluno.class);
                    list_Aluno.add(aluno);
                }
                arrayAdapterAluno = new ArrayAdapter<Aluno>(MainActivity.this,
                        android.R.layout.simple_list_item_1, list_Aluno);

                arrayAdapterAluno.sort(null);
                listV_dados.setAdapter(arrayAdapterAluno);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Context context = getApplicationContext();
        String mensagemToast = "";
        int id = item.getItemId();
        if(id == R.id.menu_novo){
            if (!edtNome.getText().toString().isEmpty() && !edtMatricula.getText().toString().isEmpty()) {
                if (!registrationExists(edtMatricula.getText().toString())){
                    Aluno al = new Aluno();
                    al.setUid(UUID.randomUUID().toString());
                    al.setName(edtNome.getText().toString());
                    al.setRegistration(edtMatricula.getText().toString());
                    databaseReference.child("Aluno").child(al.getUid()).setValue(al);
                    limparCampos();
                }else mensagemToast = "Erro! Matrícula já existente!";
            }else{
                mensagemToast = "Erro ao inserir nome com campos vazios.";
            }

            Toast.makeText(context, mensagemToast, Toast.LENGTH_SHORT).show();
        }else if (id == R.id.menu_atualiza) {
            if (alunoSelecionado != null) {
                Aluno a = new Aluno();
                a.setUid(alunoSelecionado.getUid());
                a.setName(edtNome.getText().toString().trim());
                a.setRegistration(edtMatricula.getText().toString().trim());
                databaseReference.child("Aluno").child(a.getUid()).setValue(a);
                limparCampos();
                Toast.makeText(context, "Registro atualizado!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Selecione um registro para atualizar!", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.menu_deleta) {
            Aluno a = new Aluno();
            a.setUid(alunoSelecionado.getUid());
            databaseReference.child("Aluno").child(a.getUid()).removeValue();
            limparCampos();
        }
        return true;
    }

    private void limparCampos(){
        edtNome.setText("");
        edtMatricula.setText("");
    }

    private boolean registrationExists(String registration){
        for (Aluno a : list_Aluno){
            if (a.getRegistration().equals(registration))
                return true;
        }
        return false;
    }

}