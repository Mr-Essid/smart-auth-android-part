package org.recherche.smart_auth_android_part

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ChipBorder
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
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
import kotlinx.coroutines.MainScope
import okhttp3.MediaType
import org.recherche.smart_auth_android_part.componets.ErrorAlert
import org.recherche.smart_auth_android_part.componets.SuccessAlert
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.AddEmployerViewModel
import org.recherche.smart_auth_android_part.view_models.SignUPViewModel
import java.io.File
import kotlin.math.acos

class AddEmployerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddEmployerScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    SmartauthandroidpartTheme {
    }
}


@SuppressLint("Recycle")
@Composable
fun AddEmployerScreen(viewModel: AddEmployerViewModel = viewModel()) {

    val context = LocalContext.current as Activity


    var isAddedFaceImage by remember {
        mutableStateOf(false)
    }

    var imageArrayBytes: ByteArray? = null


    val isAdded = viewModel.isAdded.observeAsState()
    val errorState = viewModel.error.observeAsState()


//    AnimatedVisibility(visible = errorState.value!!.isNotEmpty()) {
//
//
//        if (errorState.value == "400") {
//            ErrorAlert(
//                title = "BAD REQUEST",
//                body = "Check Your Fileds Are Fully Complite You have a missing filed"
//            ) {
//                print("HelloFromHere")
//                signUPViewModel.error.postValue("")
//
////                viewmodel.err.postValue("")
//            }
//        }
//
//        if (errorState.value == "409") {
//            ErrorAlert(
//                title = "CONFLICT",
//                body = "Email Already Exists, Check Your Email Address."
//            ) {
//                signUPViewModel.error.postValue("")
//
//                print("HelloFromHere")
//            }
//
//        }
//
//        if (errorState.value == "-1") {
//            ErrorAlert(
//                title = "Username Incorrect",
//                body = "Check Your Username the length at least 4 character"
//            ) {
//                print("HelloFromHere")
//                signUPViewModel.error.postValue("")
//
////                viewmodel.err.postValue("")
//            }
//
//        }
//        if (errorState.value == "-2") {
//            ErrorAlert(
//                title = "Password Incorrect",
//                body = "Check Your Password the length at least 4 character"
//            ) {
//                print("HelloFromHere")
//                signUPViewModel.error.postValue("")
//
////                viewmodel.err.postValue("")
//            }
//        }
//
//        if (errorState.value == "-3") {
//            ErrorAlert(
//                title = "Email Invalid",
//                body = "Your Email Invalid Check It Before Send."
//            ) {
//                print("HelloFromHere")
//                signUPViewModel.error.postValue("")
//
////                viewmodel.err.postValue("")
//            }
//
//        }
//
//        if (errorState.value == "-4") {
//            ErrorAlert(
//                title = "CONFIRMATION ERROR",
//                body = "Password Bad Confirmation."
//            ) {
//                print("HelloFromHere")
//                signUPViewModel.error.postValue("")
//
//            }
//        }
//
//
//    }

    AnimatedVisibility(visible = isAdded.value == true) {

        SuccessAlert(title = "EMPLOYER ADDED", body = "Employer Added Successfully.") {
            Intent().putExtra("updated", true).also {
                context.setResult(1, it)
                context.finish()
            }
        }

    }

    AnimatedVisibility(visible = errorState.value!!.isNotEmpty()) {
        if (errorState.value == "400") {
            ErrorAlert(
                title = "BAD REQUEST",
                body = "error within your data you have send,\ncheck your image are correctly have one unique clear face"
            ) {
                viewModel.error.value = ""
            }
        }

        if (errorState.value == "409") {
            ErrorAlert(
                title = "IDENTIFIER EXIST",
                body = "Check Your Email, Email Already Exits"
            ) {

                viewModel.error.value = ""

            }
        }

        if (errorState.value == "-1") {
            ErrorAlert(
                title = "IDENTIFIER INVALID",
                body = "identifier is invalid, identifier must contain emp"
            ) {
                viewModel.error.value = ""

            }
        }

        if (errorState.value == "-2") {
            ErrorAlert(
                title = "INVALID USERNAME",
                body = "username or lastname is invalid, both must at least 4 chars"
            ) {
                viewModel.error.value = ""

            }


        }

        if (errorState.value == "-3") {
            ErrorAlert(
                title = "INVALID EMAIL",
                body = "email invalid check your email"
            ) {
                viewModel.error.value = ""

            }


        }


        if (errorState.value == "-4") {
            ErrorAlert(
                title = "IMAGE NOT FOUND",
                body = "pick your face image for this request"
            ) {
                viewModel.error.value = ""

            }


        }
    }


    val launcherGetImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                val fileAsArrayBytes =
                    context.contentResolver.openInputStream(it)
                imageArrayBytes = ByteArray(fileAsArrayBytes!!.available())
                fileAsArrayBytes.read(imageArrayBytes)
                viewModel.imageBytes = imageArrayBytes
                isAddedFaceImage = true
            }
        }
    )


    var isShowConfirmation by remember {
        mutableStateOf(false)
    }

    var isRequestSent = viewModel.isRequestSent.observeAsState()

    val scrollState = rememberScrollState()
    val mainSpacer = @Composable { Spacer(modifier = Modifier.height(16.dp)) }
    val secondSpacer = @Composable { Spacer(modifier = Modifier.height(8.dp)) }


    val mainPadding = 16.dp
    val secondPadding = 8.dp

    Scaffold(
        topBar = {
            Column {
                TopAppBarApplication(title = "Add Employer") {
                    context.finish()
                }
                if (isRequestSent.value == true) {  // not mistake it's smart cast we can not put isRequestSent!!
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

        }
    ) { it_ ->
        Surface(
            modifier = Modifier
                .padding(it_)
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
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
                            painter = painterResource(id = R.drawable.adduser),
                            contentDescription = "image user",
                            modifier = Modifier.size(100.dp),
                        )
                    }
                    mainSpacer()
                    OutlinedTextField(
                        value = viewModel.employerIdentifier.value,
                        onValueChange = { viewModel.employerIdentifier.value = it },
                        placeholder = {
                            Text(
                                text = "ID",
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
                                painter = painterResource(id = R.drawable.membership),
                                contentDescription = "image view",
                                Modifier
                                    .padding(0.dp, 0.dp, 4.dp, 0.dp)
                                    .size(24.dp)
                            )
                        }

                    )
                    secondSpacer()
                    OutlinedTextField(
                        value = viewModel.employerEmail.value,
                        onValueChange = { viewModel.employerEmail.value = it },
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
                                Modifier
                                    .padding(0.dp, 0.dp, 4.dp, 0.dp)
                                    .size(24.dp)
                            )
                        }

                    )
                    secondSpacer()


                    OutlinedTextField(
                        value = viewModel.employerName.value,
                        onValueChange = { viewModel.employerName.value = it },
                        placeholder = {
                            Text(
                                text = "Name",
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
                        value = viewModel.employerLastname.value,
                        onValueChange = { viewModel.employerLastname.value = it },
                        placeholder = {
                            Text(
                                text = "Lastname",
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
                                painterResource(id = R.drawable.family),
                                contentDescription = "view image",
                                Modifier
                                    .padding(0.dp, 0.dp, 4.dp, 0.dp)
                                    .size(24.dp)
                            )
                        }
                    )

                    mainSpacer()

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AssistChip(
                            onClick = {
                                imageArrayBytes = null
                                isAddedFaceImage = false
                                viewModel.imageBytes = null
                                launcherGetImage.launch(PickVisualMediaRequest())
                            },
                            label = { Text(text = "Face Image") },
                            trailingIcon = {
                                if (isAddedFaceImage && imageArrayBytes != null) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "Added"
                                    )
                                }

                            }
                        )
//
//                        if (!isAddedFaceImage) {
//                            Box(
//                                modifier = Modifier
//                                    .size(130.dp)
//                                    .border(BorderStroke(0.3.dp, Color.Black)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(text = "Preview")
//                            }
//                        } else
                        if (isAddedFaceImage && imageArrayBytes != null) {
                            val bitmap = BitmapFactory.decodeByteArray(
                                imageArrayBytes,
                                0,
                                imageArrayBytes!!.size
                            )
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Current Image",
                                modifier = Modifier.size(130.dp)
                            )
                        }
                    }

                    secondSpacer()

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.isRequestSent.postValue(true)
                                viewModel.addEmployer()
                            },
                            Modifier.width(100.dp)
                        ) {
                            Text(text = "SEND")
                        }
                        Spacer(modifier = Modifier.width(secondPadding))
                        OutlinedButton(onClick = {
                        }, Modifier.width(100.dp)) {
                            Text(text = "RESET")
                        }
                    }
                    mainSpacer()
                }


//                Divider(modifier = Modifier
//                    .height(2.dp)
//                    .fillMaxWidth(), color = MaterialTheme.colorScheme.primary, thickness = 2.dp)

            }


        }
    }
}
