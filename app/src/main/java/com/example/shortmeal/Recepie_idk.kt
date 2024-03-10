package com.example.shortmeal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
        val instruction= intent.getStringExtra("instruction")

        setContent {
            SHORTMealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = title.toString(), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.onSurface)
                        Spacer(modifier = Modifier.height(16.dp))
                        if (image != null) {
                            Image(
                                painter = rememberImagePainter(image),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(200.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Ingredients", style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onSurface)
                        Text(text = ingredients.toString(), style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onSurface)
                        Spacer(modifier = Modifier.height(16.dp))
                        if(instruction!=null){
                            Text(text = "Instructions", style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onSurface)
                            Text(text = instruction.toString(), style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onSurface)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if(video!=null){
                            Button(onClick ={
                                //Open the video
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.setPackage("com.google.android.youtube")
                                startActivity(intent)

                            },
                                modifier = Modifier.testTag("PlayButton")
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