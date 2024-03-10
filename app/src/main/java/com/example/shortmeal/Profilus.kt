package com.example.shortmeal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.shortmeal.ui.theme.SHORTMealTheme
import kotlinx.coroutines.launch

class Profilus : ComponentActivity() {
    var obj=Short_Meal_obj()
    var a = profil()

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user_name= intent.getStringExtra("username")
        val score=intent.getStringExtra("score")
        setContent {
            SHORTMealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (score != null) {
                        a.scor = score.toInt()
                    }
                    a.name = user_name.toString()
                    a.posts = 10
                    a.imagine = R.drawable.cuza
                    obj.profilepage(a, navController = rememberNavController() )

                }
            }
            if (user_name!= null) {
                obj.ModalDrawerExample(
                    context = this@Profilus,
                    navController = rememberNavController(),
                    username= user_name.toString(),
                    activityContentScope = { state, scope ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            BackHandler(
                                enabled = state.currentValue != DrawerValue.Open,
                                onBack = {
                                    scope.launch {
                                        if (state.currentValue == DrawerValue.Open) {
                                            state.close()
                                        } else {
                                            state.open()
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
            }

        }
    }
}