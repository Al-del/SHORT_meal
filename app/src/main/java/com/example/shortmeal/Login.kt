package com.example.shortmeal

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = Firebase.database
            val myRef = database.getReference()
            LoginScreen_(myRef,this)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen_(databaseReference: DatabaseReference,context:Context) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginSuccessful = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(loginSuccessful.value) {
        if (loginSuccessful.value) {
            scope.launch {
                Log.d("kilo", "Username ${username.value}")
                val intent = Intent(context, Profilus::class.java)
                intent.putExtra("username", username.value)
                context.startActivity(intent)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                colors = textFieldColors(Color(0xFF90FFDC)) // Set the color of the TextField
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add a spacer

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors(Color(0xFF90FFDC)) // Set the color of the TextField
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add a spacer

            Button(
                onClick = {
                    // Query the database to check if the entered username and password exist
                    databaseReference.child("Register").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for (userSnapshot in dataSnapshot.children) {
                               Log.d(TAG, "Value is: ${userSnapshot.value}")
                                //Get the user object from the snapshot
                                //val user = userSnapshot.getValue(USR::class.java)

                                val user = userSnapshot?.let {
                                    val username = it.child("username").getValue(String::class.java) ?: ""
                                    val password = it.child("password").getValue(String::class.java) ?: ""
                                    val listSnapshot = it.child("list")
                                    val list = mutableListOf<FoodPair>()
                                    for (pairSnapshot in listSnapshot.children) {
                                        val first = pairSnapshot.child("first").getValue(String::class.java) ?: ""
                                        val second = pairSnapshot.child("second").getValue(String::class.java) ?: ""
                                        list.add(FoodPair(first, second))
                                    }
                                    USR(username, password, list)
                                }
                                Log.d(TAG, "Value is: ${user?.username}")
                                if(user?.username == username.value ){
                                    // If the username and password match, navigate to the next screen
                                    // For now, we will just print a message
                                    if(user?.password == password.value)
                                    {
                                        Log.d(TAG, "Login successful")
                                        loginSuccessful.value = true

                                    }
                                    else{
                                        // If the username and password do not match, show an error message
                                        Log.d(TAG, "Login failed")
                                    }
                                }

                                // user contains the data from the snapshot
                                // You can access the fields of the user object here
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException())
                        }
                    })
                },
                modifier = Modifier
                    .height(50.dp) // Set the height of the button
                    .width(200.dp), // Set the width of the button

                    colors = buttonColors(Color(0xFF90FFDC)) // Set the background color of the button
            ) {
                Text(
                    text = "Login",
                    color = Color(0xFF8AC4FF)
                )
            }
        }
    }
}

