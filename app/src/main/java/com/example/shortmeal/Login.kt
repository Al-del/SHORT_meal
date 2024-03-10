package com.example.shortmeal

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
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
    val passwordVisibility = remember { mutableStateOf(false) } // Add this line
    LaunchedEffect(loginSuccessful.value) {
        if (loginSuccessful.value) {
            scope.launch {
                Log.d("kilo", "Username ${username.value}")
                val intent = Intent(context, Main::class.java)
                intent.putExtra("username", username.value)
                context.startActivity(intent)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {Image(
            painter = painterResource(id = R.drawable.logo_short_meal), // replace with your logo resource
            contentDescription = "App Logo",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { androidx.compose.material3.Text("Username") },
                colors = textFieldColors(Color(0xFFC6BBDD)) // Set the color of the TextField
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add a spacer

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { androidx.compose.material3.Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors(Color(0xFFC6BBDD)) // Set the color of the TextField
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

                colors = buttonColors(Color(0xFFEA85AF)) // Set the background color of the button
            ) {
                androidx.compose.material3.Text(
                    text = "Log in",
                    color = Color(0xFF363457),
                    fontSize = 23.sp // Increase the font size
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Add a spacer
            Text(
                text = "Go to Register",
                modifier = Modifier.clickable {
                    val intent = Intent(context, Paginadoi::class.java)
                    context.startActivity(intent)
                },
                color = (Color(0XFFC6BBDD)),
                fontSize = 20.sp, // Increase the font size
                fontWeight = FontWeight.Bold // Make the text bold
            )
            Spacer(modifier = Modifier.height(40.dp)) // Add a spacer
                Text(
                    text = "by ShortCircuiT",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color(0XFFFFAE03)
                    // modifier = Modifier.align(Alignment.BottomCenter)
                )
        }
    }
}

