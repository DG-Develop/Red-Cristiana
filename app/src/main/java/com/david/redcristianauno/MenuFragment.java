package com.david.redcristianauno;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.david.redcristianauno.Clases.Foto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class MenuFragment extends Fragment {
    private ViewFlipper vfliper;
    private Button ibizq, ibder;

    ArrayList<Foto> arrayListFotos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        vfliper = view.findViewById(R.id.slideNoticias);
        ibizq = view.findViewById(R.id.ibizq);
        ibder = view.findViewById(R.id.ibder);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference reference = db.collection("Galeria");

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            ImageView imageView;

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                    Foto foto = documentSnapshot.toObject(Foto.class);

                    arrayListFotos.add(foto);
                }

                for (int i = 0; i < arrayListFotos.size(); i++){
                    imageView = new ImageView(getActivity());

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //lp.setMargins(0, -30, 0, 0);
                    imageView.setLayoutParams(lp);

                    Picasso.get()
                            .load(arrayListFotos.get(i).getRuta_foto())
                            .resize(1080,1200)
                            .centerCrop()
                            .into(imageView);

                    vfliper.addView(imageView);
                }
            }
        });

        fliperImagesNoticias();

        ibder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vfliper.showNext();
            }
        });

        ibizq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vfliper.showPrevious();
            }
        });

        return view;
    }

    public void fliperImagesNoticias(){

        vfliper.setFlipInterval(4000);
        vfliper.setAutoStart(true);
        vfliper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Soy una imagen", Toast.LENGTH_SHORT).show();

            }
        });

        //animation
        vfliper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        //vfliper.setInAnimation(getActivity(), android.R.anim.slide_out_right);

    }

}
