package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    // create object of DatabaseReference class to access firebase's RealtimeDatabase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://learnoset-d2297-default-rtdb.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullname = findViewById(R.id.fullname);
        final EditText email = findViewById(R.id.email);
        final  EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final  EditText conPassword = findViewById(R.id.conPassword);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from EditTexts into String variables
                final  String fullnameTxt = fullname.getText().toString();
                final  String emailTxt = email.getText().toString();
                final  String phoneTxt = phone.getText().toString();
                final  String passwordTxt = password.getText().toString();
                final  String conPasswordTxt = conPassword.getText().toString();

                // check if user fill all the  fields before sending data to firebase

                if (fullnameTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Register.this,"Please fiil all fields", Toast.LENGTH_SHORT).show();
                }

                //check if passwords are matching with each other
                 // if not matching with eqch other then show toast message
                else if(!passwordTxt.equals(conPasswordTxt)){
                    Toast.makeText(Register.this,"Password are not matching", Toast.LENGTH_SHORT).show();
                }

                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if phone is not registered before
                            if (snapshot.hasChild(phoneTxt)){
                                Toast.makeText(Register.this,"Phone is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //sending data to firebase Realyime Database
                                // we are using phone number as unique identity of every user
                                // so all the other details of user comes under phone number
                                databaseReference.child("users").child(phoneTxt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("users").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);

                                // show a success message then finish activity
                                Toast.makeText(Register.this,"User registered succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}