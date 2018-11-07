package com.example.carlos.appclickshow.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carlos.appclickshow.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LoggedActivity extends AppCompatActivity {

    private Button selecionarImg;
    private StorageReference storage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog barraDeProgresso;

    //final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged2);

        storage = FirebaseStorage.getInstance().getReference();
        selecionarImg = (Button) findViewById(R.id.selectImage);

        barraDeProgresso = new ProgressDialog(this);

        selecionarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            barraDeProgresso.setMessage("Fazendo Upload...");
            barraDeProgresso.show();

            mRef = FirebaseDatabase.getInstance().getReference();

            Uri uri = data.getData();
            final StorageReference filePath = storage.child("Fotos").child(uri.getLastPathSegment());

            //DatabaseReference fotosRef = mRef.child("fotos");

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

                            Toast.makeText(LoggedActivity.this, downloadUrl.toString(),Toast.LENGTH_SHORT).show();

                        }
                    });

                   /* filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            //Armazenar no banco a URL

                            DatabaseReference fotosRef = mRef.child("fotos");
                            fotosRef.child("url").setValue(downloadUrl.toString());

                        }
                    });*/

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
