package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.runtime.remember
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
import org.recherche.smart_auth_android_part.componets.SuccessAlert
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.SignUPViewModel

class SignUPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignUPScreen()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SmartauthandroidpartTheme {
        SignUPScreen()
    }
}

@Composable
fun SignUPScreen(signUPViewModel: SignUPViewModel = viewModel()) {


    val activity = LocalContext.current as Activity

    var isShowPassword by remember {
        mutableStateOf(false)
    }


    val stateRequest = signUPViewModel.stateRequest.observeAsState()
    val errorState = signUPViewModel.isError.observeAsState()
    val addedResponse = signUPViewModel.isAdded.observeAsState()



    AnimatedVisibility(visible = errorState.value!!.isNotEmpty()) {


        if (errorState.value == "400") {
            ErrorAlert(
                title = "BAD REQUEST",
                body = "Check Your Fileds Are Fully Complite You have a missing filed"
            ) {
                print("HelloFromHere")
                signUPViewModel.isError.postValue("")

//                viewmodel.err.postValue("")
            }
        }

        if (errorState.value == "409") {
            ErrorAlert(
                title = "CONFLICT",
                body = "Email Already Exists, Check Your Email Address."
            ) {
                signUPViewModel.isError.postValue("")

                print("HelloFromHere")
            }

        }

        if (errorState.value == "-1") {
            ErrorAlert(
                title = "Username Incorrect",
                body = "Check Your Username the length at least 4 character"
            ) {
                print("HelloFromHere")
                signUPViewModel.isError.postValue("")

//                viewmodel.err.postValue("")
            }

        }
        if (errorState.value == "-2") {
            ErrorAlert(
                title = "Password Incorrect",
                body = "Check Your Password the length at least 4 character"
            ) {
                print("HelloFromHere")
                signUPViewModel.isError.postValue("")

//                viewmodel.err.postValue("")
            }
        }

        if (errorState.value == "-3") {
            ErrorAlert(
                title = "Email Invalid",
                body = "Your Email Invalid Check It Before Send."
            ) {
                print("HelloFromHere")
                signUPViewModel.isError.postValue("")

//                viewmodel.err.postValue("")
            }

        }

        if (errorState.value == "-4") {
            ErrorAlert(
                title = "CONFIRMATION ERROR",
                body = "Password Bad Confirmation."
            ) {
                print("HelloFromHere")
                signUPViewModel.isError.postValue("")

            }
        }


    }

    if (addedResponse.value == true) { // == true : cause smart cast
        SuccessAlert(
            title = "REQUEST SEND SUCCESSFULLY",
            body = "your request send to admin you have to wait until admin approve your request, you'll get email"
        ) {
            val intent = Intent()
            intent.putExtra("added", true)
            activity.setResult(1, intent)
            activity.finish()
        }
    }

    var isShowConfirmation by remember {
        mutableStateOf(false)
    }
    val mainSpacer = @Composable { Spacer(modifier = Modifier.height(16.dp)) }
    val secondSpacer = @Composable { Spacer(modifier = Modifier.height(8.dp)) }


    val mainPadding = 16.dp
    val secondPadding = 8.dp

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

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.admin_add),
                            contentDescription = "image user",
                            modifier = Modifier.size(100.dp),
                        )
                    }
                    mainSpacer()
                    OutlinedTextField(
                        value = signUPViewModel.username.value,
                        onValueChange = { signUPViewModel.username.value = it },
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
                        value = signUPViewModel.email.value,
                        onValueChange = { signUPViewModel.email.value = it },
                        placeholder = {
                            Text(
                                text = "EMAIL",
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
                                imageVector = Icons.Outlined.Email,
                                contentDescription = "image view",
                                Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
                            )
                        }

                    )
                    secondSpacer()
                    OutlinedTextField(
                        value = signUPViewModel.password.value,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        suffix = {
                            Box(modifier = Modifier.clickable {
                                isShowPassword = !isShowPassword
                            }) {

                                if (!isShowConfirmation) {
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
                        onValueChange = { signUPViewModel.password.value = it },
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
                    OutlinedTextField(
                        value = signUPViewModel.passwordConfirmation.value,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        suffix = {
                            Box(modifier = Modifier.clickable {
                                isShowConfirmation = !isShowConfirmation
                            }) {

                                if (!isShowConfirmation) {
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
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "lock password",
                                modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
                            )
                        },
                        onValueChange = { signUPViewModel.passwordConfirmation.value = it },
                        placeholder = {
                            Text(
                                text = "CONFIRMATION",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        visualTransformation = if (!isShowConfirmation) PasswordVisualTransformation() else VisualTransformation.None,
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
                                signUPViewModel.stateRequest.postValue(1)
                                signUPViewModel.signup()
                            },
                            Modifier.width(100.dp)
                        ) {
                            Text(text = "SEND")
                        }
                        Spacer(modifier = Modifier.width(secondPadding))
                        OutlinedButton(onClick = {
                            signUPViewModel.clean()
                        }, Modifier.width(100.dp)) {
                            Text(text = "RESET")
                        }
                    }

                    mainSpacer()
                    if (stateRequest.value != 1)
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
                                append("your already have one, ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontSize = TextUnit(12f, TextUnitType.Sp),
                                    color = Color.Blue,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append("login")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activity.finish()
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
