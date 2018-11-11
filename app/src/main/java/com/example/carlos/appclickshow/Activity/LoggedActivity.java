package com.example.carlos.appclickshow.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.appclickshow.Entidades.Phototeste;
import com.example.carlos.appclickshow.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LoggedActivity extends AppCompatActivity {

    private Button selecionarImg;
    private StorageReference storage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog barraDeProgresso;

    private FirebaseDatabase mDb;
    private DatabaseReference mRef;
    private ImageView mImageView;
    private ListView listVdados;
    private FirebaseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged2);

        storage = FirebaseStorage.getInstance().getReference();
        selecionarImg = (Button) findViewById(R.id.selectImage);
        listVdados = (ListView) findViewById(R.id.listVdados);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mDb = FirebaseDatabase.getInstance();
        mRef = mDb.getReference();

        barraDeProgresso = new ProgressDialog(this);

        //Capturar as imagens do banco
        Query query = mRef.child("Phototeste");
        FirebaseListOptions<Phototeste> options = new FirebaseListOptions.Builder<Phototeste>()
                .setLayout(R.layout.phototeste)
                .setQuery(query,Phototeste.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {

                ImageView url = v.findViewById(R.id.imageView);
                Phototeste photo = (Phototeste) model;
                Picasso.get().load(photo.getUrl().toString()).fit().centerCrop().into(url);

            }
        };

        listVdados.setAdapter(adapter);
        //Fim da captura de imagens no banco

        //Chamada da função do botão de upload
        selecionarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

    }

    //OnStart e OnStop são responsáveis pela verificação de novas imagens no banco
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            barraDeProgresso.setMessage("Fazendo Upload...");
            barraDeProgresso.show();

            Uri uri = data.getData();
            final StorageReference filePath = storage.child("Fotos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    barraDeProgresso.dismiss();

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            //Armazenar no banco a URL
                            String url = downloadUrl.toString();
                            mRef.child("Phototeste").push().child("url").setValue(url);

                        }
                    });

                    Toast.makeText(LoggedActivity.this, "Upload Concluído", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Caso falhe o Upload!!

                }
            });

        }

    }
}
