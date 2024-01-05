package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.recherche.smart_auth_android_part.componets.ErrorAlert
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.SignINViewModel

class SignINActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignInScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    SmartauthandroidpartTheme {
        SignInScreen()
    }
}


@Composable
fun SignInScreen(viewmodel: SignINViewModel = viewModel()) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == 200) {
                print("Ok Man You Are In The DB")
            }
        })

    val context = LocalContext.current as Activity


    var isShowPassword by rememberSaveable {
        mutableStateOf(false)
    }

    val errorObserver = viewmodel.err.observeAsState()
    val isDataLoad = viewmodel.isDataLoad.observeAsState()
    val isAuthenticated = viewmodel.isAuthenticated.observeAsState()


    val mainPadding = 16.dp
    val secondPadding = 8.dp
    val mainSpacer = @Composable { Spacer(modifier = Modifier.height(16.dp)) }
    val secondSpacer = @Composable { Spacer(modifier = Modifier.height(8.dp)) }

    if (isAuthenticated.value == true) {
        context.startActivity(Intent(context, DashboardActivity::class.java))
        context.finish()
    }


    AnimatedVisibility(visible = errorObserver.value!!.isNotEmpty()) {
        if (errorObserver.value == "401") {
            println("We Are Here")
            ErrorAlert(
                title = "UNAUTHORIZED",
                body = "username or password incorrect, check your credentials or reset your password if your forget it."
            ) {
                print("HelloFromHere")
                viewmodel.err.postValue("")
            }
        }

        if (errorObserver.value == "403") {
            ErrorAlert(
                title = "FORBIDDEN",
                body = "account currently not active call your administrator to active your account."
            ) {
                print("HelloFromHere")
                viewmodel.err.postValue("")
            }
        }

        if (errorObserver.value == "500") {
            ErrorAlert(
                title = "INTERNAL SERVER ERROR",
                body = "server currently in maintenance status, call your administrator from more info."
            ) {
                print("HelloFromHere")
                viewmodel.err.postValue("")
            }
        }

    }



    Scaffold { it_ ->

        Surface(
            modifier = Modifier
                .padding(it_)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.3f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            0.6f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.9f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            1f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .padding(mainPadding),
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.password),
                            contentDescription = "image user",
                            modifier = Modifier.size(100.dp),
                        )
                    }
                    mainSpacer()
                    OutlinedTextField(
                        value = viewmodel.username.value,
                        onValueChange = { viewmodel.username.value = it },
                        placeholder = {
                            Text(
                                text = "USERNAME",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "image view",
                                Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
                            )
                        }

                    )
                    secondSpacer()
                    OutlinedTextField(
                        value = viewmodel.password.value,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        suffix = {
                            Box(modifier = Modifier.clickable {
                                isShowPassword = !isShowPassword
                            }) {

                                if (!isShowPassword) {
                                    Image(
                                        painter = painterResource(id = R.drawable.visibility),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.visible),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            }

                        },
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "lock password",
                                modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
                            )
                        },
                        onValueChange = { viewmodel.password.value = it },
                        placeholder = {
                            Text(
                                text = "PASSWORD",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        visualTransformation = if (!isShowPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    secondSpacer()

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                val resp = viewmodel.LoginAdmin(); if (resp > 0) {
                                viewmodel.isDataLoad.postValue(1)
                            }
                            },
                            Modifier.width(100.dp)
                        ) {
                            Text(text = "SEND")
                        }
                        Spacer(modifier = Modifier.width(secondPadding))
                        OutlinedButton(onClick = {
                            viewmodel.username.value = ""; viewmodel.password.value = ""
                        }, Modifier.width(100.dp)) {
                            Text(text = "RESET")
                        }
                    }

                    mainSpacer()
                    if (isDataLoad.value != 1)
                        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                    else
                        LinearProgressIndicator(
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth(),
                            strokeCap = StrokeCap.Round
                        )
                    mainSpacer()

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = TextUnit(12f, TextUnitType.Sp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append("if you haven't account, ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontSize = TextUnit(12f, TextUnitType.Sp),
                                    color = Color.Blue,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append("create one")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(context, SignUPActivity::class.java)
                                launcher.launch(intent)
                            },
                        textAlign = TextAlign.Center,
                    )
                }


//                Divider(modifier = Modifier
//                    .height(2.dp)
//                    .fillMaxWidth(), color = MaterialTheme.colorScheme.primary, thickness = 2.dp)

            }


        }
    }
}