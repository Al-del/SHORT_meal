package com.example.shortmeal

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.shortmeal.ui.theme.SHORTMealTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Show_recepies : ComponentActivity() {
    private val foodListState = mutableStateOf<List<FoodPair>?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username= intent.getStringExtra("username")
        setContent {
            SHORTMealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    foodListState.value?.let { FoodList(it,lifecycleScope) }
                }
            }
        }

        lifecycleScope.launch {
            val list = get_list_of_foods(username.toString())
            // Update the state with the loaded list
            foodListState.value = list
        }
    }
}
suspend fun get_list_of_foods(userName: String): MutableList<FoodPair>? = suspendCoroutine { continuation ->
    // Get a reference to the Firebase database
    val database = FirebaseDatabase.getInstance()

    // Get a reference to the "users" node
    val usersRef = database.getReference("Register")

    // Query the database for the user with the specified username
    val query = usersRef.orderByChild("username").equalTo(userName)
    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                // The user exists in the database
                for (userSnapshot in dataSnapshot.children) {
                    // Get the user data and store it in the user variable
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
                    Log.d("kilo", "Value is: ${user?.list}")
                    var e:List<FoodPair>?=null
                    e=user?.list
                    Log.d("kilo", "Value is: ${e}")
                    continuation.resume(e as MutableList<FoodPair>?)
                }
            } else {
                continuation.resume(null)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle possible errors.
            continuation.resumeWithException(databaseError.toException())
        }
    })
}
@Composable
fun FoodList(foodList: List<FoodPair>, lifecycleScope: LifecycleCoroutineScope) {
    val recipeUrlState = remember { mutableStateOf<String?>(null) }

    LazyColumn {
        items(foodList) { item ->
            Card(modifier = Modifier.padding(8.dp)) {
                Text(text = "${item.first}", modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        lifecycleScope.launch {
                            val recipeUrl = withContext(Dispatchers.IO) {
                                getEdamamRecipes(
                                    "eaa2c4366d4e55b3088a580edfaa5bd7",
                                    "179c5048",
                                    item.first
                                )
                            }
                            recipeUrlState.value = recipeUrl
                        }
                    }, style = MaterialTheme.typography.displayMedium)
                LoadImageFromUrl(url = item.second)
            }
        }
    }

    recipeUrlState.value?.let { WebViewScreen(it) }
}

 suspend fun getEdamamRecipes(apiKey: String, appId: String, food: String, numResults: Int = 5): String? {
    val baseUrl = "https://api.edamam.com/search"

    val client = OkHttpClient()

    // Set up parameters for the API request
    val url = "$baseUrl?q=$food&app_id=$appId&app_key=$apiKey&to=$numResults"

    // Make the API request
    val request = Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()

    // Transform the response into a JSON
    val jsonData = JSONObject(response.body?.string())

    // Get the recipe URL
    val hits = jsonData.getJSONArray("hits")
    if (hits.length() > 0) {
        val firstHit = hits.getJSONObject(0)
        val recipe = firstHit.getJSONObject("recipe")
        val recipeUrl = recipe.getString("url")

        return recipeUrl
    } else {
        return null  // No recipes found for the specified food
    }
}
@Composable
fun WebViewScreen(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { view ->
            view.loadUrl(url)
        }
    )
}