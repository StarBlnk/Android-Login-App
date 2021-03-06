package com.example.LoginApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LoginApp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity {

    DatabaseHelper myDb = new DatabaseHelper(this);
    Button sign1;
    EditText user, pass, confirmpass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        myDb = new DatabaseHelper(this);

        sign1 = (Button) findViewById(R.id.signup_button1);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        confirmpass = findViewById(R.id.editTextTextPassword);



        sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = 0;

                if(user.getText().toString().equals("") || pass.getText().toString().equals("")){
                    Toast.makeText(Signup.this, "You must have enter a username and password", Toast.LENGTH_SHORT).show();
                }
                else if (!confirmpass.getText().toString().equals(pass.getText().toString())){
                    Toast.makeText(Signup.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
                else{

                    Cursor res = myDb.getAllData();
                    while (res.moveToNext()) {
                        //checking if username already exists
                        String USER = res.getString(1);
                        if(user.getText().toString().equals(USER)){
                            Toast.makeText(Signup.this, "This username already exists", Toast.LENGTH_SHORT).show();
                            num++;
                        }
                    }

                    if(num == 0){

                        //Entering data into the SQLite database
                        myDb.insertData(user.getText().toString(), pass.getText().toString());
                        boolean isInserted = myDb.insertData(user.getText().toString(), pass.getText().toString());

                        if(isInserted == true){
                            Toast.makeText(Signup.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Signup.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                        }

                        JSONObject obj = new JSONObject();

                        try{
                            obj.put("user", user.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences sharedPref = getSharedPreferences("SharedPrefs", getBaseContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("USERSHARE", user.getText().toString());
                        editor.apply();


                        Intent intent = new Intent(Signup.this, Welcome_page.class);
                        intent.putExtra("json", obj.toString());
                        startActivity(intent);

                    }

                }

            }
        });

    }
}
