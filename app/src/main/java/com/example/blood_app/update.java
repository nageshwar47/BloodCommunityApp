package com.example.blood_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link update#} factory method to
 * create an instance of this fragment.
 */
public class update extends Fragment {

    Button btn_next;
    EditText A, a, B, b, O, o, AB, ab;
    private FirebaseAuth mauth;
    String name, address, lag, lat, loc, number,A1,B1,AB1,O1,o1,a1,ab1,b1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_update, container, false);

        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mref = database.getReference("donor");



        btn_next = view.findViewById(R.id.btn_update);
        A = view.findViewById(R.id.et_A);
        B = view.findViewById(R.id.et_B);
        O = view.findViewById(R.id.et_O);
        AB = view.findViewById(R.id.et_AB);
        a = view.findViewById(R.id.et_a);
        b = view.findViewById(R.id.et_b);
        o = view.findViewById(R.id.et_o);
        ab = view.findViewById(R.id.et_ab);


        // accessing the details

        mref.child(mauth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Map map = (Map) snapshot.getValue();

                    //get data from map
                    name = map.get("name").toString();
                    lat = map.get("latitude").toString();
                    lag = map.get("logitude").toString();
                    loc = map.get("location").toString();
                    number = map.get("number").toString();
                    A1 = map.get("A").toString();
                    B1= map.get("B").toString();
                    O1 = map.get("O").toString();
                    AB1 = map.get("AB").toString();
                    a1 = map.get("a").toString();
                    b1 = map.get("b").toString();
                    o1 = map.get("o").toString();
                    ab1 = map.get("ab").toString();

                    // set text to the edit text
                    A.setText(A1);
                    B.setText(B1);
                    O.setText(O1);
                    AB.setText(AB1);
                    a.setText(a1);
                    b.setText(b1);
                    o.setText(o1);
                    ab.setText(ab1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });





        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            A1=A.getText().toString();
            B1=B.getText().toString();
            o1=O.getText().toString();
            AB1=AB.getText().toString();
            a1=a.getText().toString();
            b1=b.getText().toString();
            o1=o.getText().toString();
            ab1=ab.getText().toString();

           // Toast.makeText(this,name,)





                //add value in hashmap
                HashMap<String, Object> m = new HashMap<>();  //hashmap object for personal details
                m.put("name", name);
                m.put("location", address);
                m.put("latitude", lat);
                m.put("logitude", lag);
                m.put("number", number);
                m.put("A", A1);
                m.put("B", B1);
                m.put("O", O1);
                m.put("AB", AB1);
                m.put("a", a1);
                m.put("b", b1);
                m.put("o", o1);
                m.put("ab",ab1);

                mref.child(mauth.getUid()).setValue(m);



            }
        });
        return view;


    }
}