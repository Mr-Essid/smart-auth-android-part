package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AssistChip
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.recherche.smart_auth_android_part.componets.ErrorAlert
import org.recherche.smart_auth_android_part.componets.SuccessAlert
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.componets.WarningAlert
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.EmployerDetailViewModel

class EmployerDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmployerDetailsScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting6(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview7() {
    SmartauthandroidpartTheme {
        EmployerDetailsScreen()
    }
}

@Composable
fun EmployerDetailsScreen(employerDetailViewModel: EmployerDetailViewModel = viewModel()) {

    val mainPadding = 16.dp
    val secondPadding = 8.dp
    val mainSpacer = @Composable { Spacer(modifier = Modifier.height(16.dp)) }
    val secondSpacer = @Composable { Spacer(modifier = Modifier.height(8.dp)) }
    val activity = LocalContext.current as Activity
    val idEmployer = activity.intent.getIntExtra("id", -1)

    if (idEmployer == -1) {
        activity.finish()
    }

    LaunchedEffect(Unit) {
        employerDetailViewModel.getEmployerById(idEmployer)
    }


    val colors_ = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.secondary,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        disabledBorderColor = Color.Transparent,
        disabledTextColor = MaterialTheme.colorScheme.primary,
        disabledPlaceholderColor = MaterialTheme.colorScheme.primary,
        disabledPrefixColor = Color.Black
    )

    val currentEmployer = employerDetailViewModel.employer.observeAsState()
    val readEmployerData = employerDetailViewModel.isEmployerInSearch.observeAsState()
    val error = employerDetailViewModel.error.observeAsState()
    val delete = employerDetailViewModel.isDeleted.observeAsState()
    val isUpdated = employerDetailViewModel.isUpdate.observeAsState()
    var popDisActivation by remember {
        mutableStateOf(false)
    }

    var isDataUpdatable by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    AnimatedVisibility(visible = error.value != null) {
        ErrorAlert(title = error.value!!.title, body = error.value!!.message) {
            employerDetailViewModel.clearError()
        }
    }


    AnimatedVisibility(visible = isUpdated.value == true) {
        SuccessAlert(title = "EMPLOYER UPDATE", body = "Employer Updated Successfully") {
            employerDetailViewModel.isUpdate.postValue(false)
            isDataUpdatable = false
        }
    }

    AnimatedVisibility(visible = delete.value == true) {
        SuccessAlert(title = "EMPLOYER DELETED", body = "Employer Deleted Successfully") {
            employerDetailViewModel.isDeleted.postValue(false)
        }
    }

    AnimatedVisibility(visible = popDisActivation) {
        WarningAlert(
            title = "CONFIRMATION",
            body = "Are Your Sure You Wanna Delete This Employer!",
            {
                popDisActivation = false
            }
        ) {
            employerDetailViewModel.inActiveEmployer()
            popDisActivation = false
        }
    }


    Scaffold(
        topBar = {
            Column {
                TopAppBarApplication(title = "Employer Details") {
                    activity.setResult(
                        1,
                        Intent().also {
                            it.putExtra(
                                "updated",
                                employerDetailViewModel.isUpdatedBackResponse
                            )
                        })
                    activity.finish()
                }
                if (readEmployerData.value == true) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    )
    {
        Surface(modifier = Modifier.padding(it))
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                contentAlignment = Alignment.Center
            ) {
                if (currentEmployer.value != null)
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
                                painter = painterResource(id = R.drawable.admin),
                                contentDescription = "image user",
                                modifier = Modifier.size(100.dp),
                            )
                        }
                        mainSpacer()
                        OutlinedTextField(
                            value = employerDetailViewModel.employer.value!!.identifier_employer,
                            enabled = false,
                            onValueChange = {
                            },
                            placeholder = {
                                Text(
                                    text = "ID",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = colors_,
                            modifier = Modifier.fillMaxWidth(),
                            prefix = {
                                Icon(
                                    painter = painterResource(id = R.drawable.membership),
                                    contentDescription = "image view",
                                    Modifier
                                        .padding(0.dp, 2.dp, 4.dp, 0.dp)
                                        .size(24.dp)
                                )
                            },
                        )
                        secondSpacer()
                        OutlinedTextField(
                            value = employerDetailViewModel.employer.value!!.email_employer,
                            onValueChange = { },
                            placeholder = {
                                Text(
                                    text = "EMAIL",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = colors_,
                            enabled = false,
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
                            value = employerDetailViewModel.employerName.value,
                            onValueChange = { newName ->
                                employerDetailViewModel.employerName.value = newName
                            },
                            placeholder = {
                                Text(
                                    text = "Name",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = colors_,
                            enabled = isDataUpdatable,
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
                            value = employerDetailViewModel.employerLastName.value,
                            onValueChange = { newName ->
                                employerDetailViewModel.employerLastName.value = newName
                            },
                            placeholder = {
                                Text(
                                    text = "Lastname",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = colors_,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isDataUpdatable,
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
                        secondSpacer()
                        if (currentEmployer.value!!.is_active)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                AssistChip(
                                    onClick = {
                                        isDataUpdatable = !isDataUpdatable
                                    },
                                    label = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "UPDATE",
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            if (isDataUpdatable) {
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Icon(
                                                    imageVector = Icons.Outlined.Check,
                                                    contentDescription = "update button",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            } else {
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Icon(
                                                    imageVector = Icons.Outlined.Edit,
                                                    contentDescription = "update button",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                AssistChip(
                                    onClick = {
                                        popDisActivation = true
                                    },
                                    label = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            if (currentEmployer.value!!.is_active) {
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Icon(
                                                    imageVector = Icons.Outlined.Check,
                                                    contentDescription = "update button",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                )
                            }

                        mainSpacer()

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                        }


                        secondSpacer()
                        if (isDataUpdatable)
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        employerDetailViewModel.updateEmployer()
                                    },
                                    Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "SUBMIT")
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