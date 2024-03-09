package com.example.shortmeal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.shortmeal.ui.theme.SHORTMealTheme

class Recepie_idk : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val ingredients = intent.getStringExtra("ingredients")
        val video = intent.getStringExtra("video")
        val instruction= intent.getStringExtra("instrutions")

        setContent {
            SHORTMealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title.toString(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        if (image != null) {
                            Image(
                                painter = rememberImagePainter(image),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            )
                        }
                        if(ingredients!=null){
                            Text(
                                text = "Ingredients:",
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(
                                text = ingredients.toString(),
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        if(instruction!=null){
                            Text(
                                text = "Instructions:",
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(
                                text =instruction.toString(),
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        if(video!=null){
                            Button(
                                onClick ={
                                    //Open the video
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video))
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.setPackage("com.google.android.youtube")
                                    startActivity(intent)

                                },
                                modifier = Modifier
                                    .testTag("PlayButton")
                                    .padding(vertical = 16.dp)
                            ) {
                                Text("Play Video")
                            }
                        }
                    }
                }
            }
        }
    }
}