package com.example.shortmeal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shortmeal.ui.theme.SHORTMealTheme

class animatius   : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            SHORTMealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFE56399)
                ) { Spacer(modifier = Modifier.height(130.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Welcome to SHORTMEAL", fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.icoico),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp) // Set the size of the circle
                                .clip(CircleShape) // Clip the image to a circle

                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        val context = LocalContext.current
                        Text(
                            text = "Log in",
                            modifier = Modifier.clickable {
                                val intent = Intent(context, Login::class.java)
                                context.startActivity(intent)
                            },
                            color = Color.Blue,
                            fontSize = 40.sp, // Increase the font size
                            fontWeight = FontWeight.Bold // Make the text bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "No account?", color = Color.Blue,
                            fontSize = 30.sp, // Increase the font size
                            fontWeight = FontWeight.Bold )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            val intent = Intent(this@animatius, Paginadoi::class.java)
                            startActivity(intent)
                        }) {
                            Text("Register", color = Color.Blue,
                                fontSize = 30.sp, // Increase the font size
                                fontWeight = FontWeight.Bold )
                        }
                        Text(text = "Or enter as a guest", color = Color.Blue,
                            fontSize = 30.sp, // Increase the font size
                            fontWeight = FontWeight.Bold )
                        Spacer(modifier = Modifier.height(170.dp))
                        Text(
                            text = "by ShortCircuiT",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,

                            // modifier = Modifier.align(Alignment.BottomCenter)
                        )

                    }
                }
            }
        }
    }
}
