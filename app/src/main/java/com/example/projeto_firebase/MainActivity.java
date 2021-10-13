package com.example.projeto_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.projeto_firebase.modelo.Aluno;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtMatricula;
    ListView listV_dados;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Aluno> list_Aluno = new ArrayList<Aluno>();
    private ArrayAdapter<Aluno> arrayAdapterAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNome = (EditText) findViewById(R.id.editNome);
        edtMatricula = (EditText) findViewById(R.id.editMatricula);
        listV_dados = (ListView) findViewById(R.id.listV_dados);

        inicializarFirebase();
        eventoDatabase();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){
            Aluno al = new Aluno();
            al.setUid(UUID.randomUUID().toString());
            al.setName(edtNome.getText().toString());
            al.setRegistration(edtMatricula.getText().toString());
            databaseReference.child("Aluno").child(al.getUid()).setValue(al);
            limparCampos();
        }
        return true;
    }

    private void limparCampos(){
        edtNome.setText("");
        edtMatricula.setText("");
    }
}