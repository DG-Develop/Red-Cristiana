package com.david.redcristianauno;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.david.redcristianauno.Clases.Foto;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

public class noticiasFragment extends Fragment {
    private Button btnSubir, btnCargar;
    private ImageView iv_noticia;

    private StorageReference mStorageReference;

    private Bitmap imagen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        mStorageReference = FirebaseStorage.getInstance().getReference();

        btnSubir = view.findViewById(R.id.btnSubir);
        btnCargar = view.findViewById(R.id.btnCargar);
        iv_noticia = view.findViewById(R.id.iv_noticia);

        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirImagen();
            }
        });

        return view;
    }

    private void subirImagen() {

        BitmapDrawable drawable = (BitmapDrawable) iv_noticia.getDrawable();

        imagen = drawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        imagen.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        final byte [] foto = byteArrayOutputStream.toByteArray();

        final StorageReference filePath = mStorageReference.child("Fotos Noticias").child(getDateTime() + ".jpg");

        UploadTask uploadTask = filePath.putBytes(foto);

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(getContext()).build();

        dialog.show();

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.setMessage(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount() + " de 100");
            }
        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Foto foto1 = new Foto(task.getResult().toString());

                    FirebaseFirestore.getInstance().collection("Galeria").document().set(foto1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();

                            Toast.makeText(getContext(), "Foto Guardada Exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType("image/");

        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            Uri path = data.getData();

            iv_noticia.setImageURI(path);
        }
    }

    public String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

        Date date = new Date();

        String final_date = dateFormat.format(date);

        return final_date;
    }
}
