package org.recherche.smart_auth_android_part

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.recherche.smart_auth_android_part.componets.ErrorAlert
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

class StreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                val context = LocalContext.current as Activity
                val address = context.intent.getStringExtra("ipaddress")
                val port = context.intent.getLongExtra("port", -1).toInt()

                if (port == -1 || address == null) {
                    context.finish()
                }

                Scaffold(modifier = Modifier,
                    topBar = {
                        TopAppBarApplication(title = "Stream") {
                            context.finish()
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        println("Data is Here $port $address")
                        ImageFromSocket(ipAddress = address!!, port = port)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting7(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SmartauthandroidpartTheme {
        Greeting7("Android")
    }
}


@Composable
fun ImageFromSocket(ipAddress: String, port: Int) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val context = LocalContext.current as Activity
    var isThereError by remember {
        mutableStateOf(false)
    }
    var cutConnection by remember {
        mutableStateOf(false)
    }

    var showAlert by remember {
        mutableStateOf(false)
    }


    AnimatedVisibility(visible = showAlert) {
        ErrorAlert(
            title = "STREAMING ERROR",
            body = "Small Error In The Stream, The Stream Will Cut Currently"
        ) {
            showAlert = false
        }
    }

    if (!isThereError)
        LaunchedEffect(ipAddress, port) {
            withContext(Dispatchers.IO) {
                var socket: Socket? = null
                try {
                    socket = Socket(ipAddress, port)
                } catch (e: IOException) {
                    e.printStackTrace()
                    showAlert = true
                    isThereError = true
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                    showAlert = true
                    isThereError = true
                }


                if (socket != null) {

                    val outputStream = socket.getOutputStream()
                    outputStream!!.write("CL".toByteArray())
                    val inputStream = DataInputStream(socket.getInputStream())

                    try {
                        while (true) {
                            if (cutConnection) {
                                socket.close()
                                break
                            }
                            val size_ = inputStream.readInt()
                            Log.d("TAG", "ImageFromSocket: $size_")
                            if (size_ < 0 || size_ > 50000) {
                                continue
                            }
                            val arrayBytes = ByteArray(size_)
                            inputStream.readFully(arrayBytes)
                            Log.d(
                                "TAG",
                                "ImageFromSocket: This is The len Of The frame ${arrayBytes.size}"
                            )
                            val bitmap = BitmapFactory.decodeByteArray(arrayBytes, 0, size_)
                            imageBitmap = bitmap.asImageBitmap()
                        }
                    } catch (e: Exception) {
                        // Handle exceptions
                        e.printStackTrace()
                    } finally {
                        socket.close()
                    }
                }

            }
        }
    else
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    32.dp
                ), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "HOST UNREACHABLE",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The Stream Currently Not Available, Make Sure Your Mention The Correct Address",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

    // Display the ImageBitmap using the Image composable


    Column(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
        Text(
            text = "Currently Streaming",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null, // Provide a content description if needed
                modifier = Modifier.fillMaxWidth()
            )
        }


    }
}
