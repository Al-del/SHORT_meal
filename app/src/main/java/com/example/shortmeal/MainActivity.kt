package com.example.shortmeal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = Firebase.database
            val myRef = database.getReference("Register")
            LoginScreen(myRef)
            GoToLoginText()
        }
    }
}
data class FoodPair(
    var first: String = "",
    var second: String = ""
)
data class USR(
    // on below line creating variables
    // for employee name, contact number
    // and address
    var username: String?="",
    var password: String?="",
    //List of string
    var list: List<FoodPair>?
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(databaseReference: DatabaseReference) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Register",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    colors = textFieldColors(Color(0xFF90FFDC)) // Set the color of the TextField

                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add a spacer

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = textFieldColors(Color(0xFF90FFDC)) // Set the color of the TextField
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add a spacer

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        // Read from the database
                        var list: List<FoodPair>? = null
                        //add an element to the list
                        list = listOf(FoodPair("null", "nullus"))
                       var ok= USR(username.value, password.value, list)
                        val userRef = databaseReference.child(username.value)
                        userRef.setValue(ok)
                              },
                    modifier = Modifier
                        .height(50.dp) // Set the height of the button
                        .width(200.dp), // Set the width of the button
                    colors = buttonColors(Color(0xFF90FFDC)) // Set the background color of the button

                ) {
                    Text(
                        text = "Username",
                        color = Color(0xFF8AC4FF)
                    )
                }

            }
        }
    }
}
@Composable
fun GoToLoginText() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Text(
            text = "Go to Login",
            modifier = Modifier.clickable {
                val intent = Intent(context, Login::class.java)
                context.startActivity(intent)
            },
            color = Color.Blue,
            fontSize = 20.sp, // Increase the font size
            fontWeight = FontWeight.Bold // Make the text bold
        )
    }
}
