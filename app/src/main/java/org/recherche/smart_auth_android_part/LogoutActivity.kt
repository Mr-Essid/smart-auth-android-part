package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme


class LogoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LogOutMainScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting9(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview9() {
    SmartauthandroidpartTheme {
        LogOutMainScreen()
    }
}


@Composable
fun LogOutMainScreen() {


    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            TopAppBarApplication(title = "Menu") {
                activity.finish()
            }
        }
    ) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(16.dp)
        ) {
            OutlinedButton(onClick = {
                val intent = Intent(activity, SignINActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
                SessionManagement(activity.application).removeToken()
                activity.finish()
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "LOGOUT")
            }
        }

    }


}