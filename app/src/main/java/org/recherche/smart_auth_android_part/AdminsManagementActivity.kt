package org.recherche.smart_auth_android_part

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.recherche.smart_auth_android_part.componets.ErrorAlert
import org.recherche.smart_auth_android_part.componets.SuccessAlert
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.componets.WarningAlert
import org.recherche.smart_auth_android_part.models.AdminResponse
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.AdminManagementViewModel

class AdminsManagementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AdminsManagementScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting8(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview8() {
    SmartauthandroidpartTheme {
        AdminManagementItem(adminResponse = AdminResponse(
            name_admin = "Amine",
            create_at = "90/90/200",
            email_admin = "email@email.com",
            id_admin = 3,
            is_active = false,
            password_admin = "Hello World",
            authority = 3
        ), onRefuse = { /*TODO*/ }) {
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManagementItem(
    adminResponse: AdminResponse,
    onRefuse: (id: Int) -> Unit,
    onAccept: (id: Int) -> Unit
) {
    var checkAdmin by remember {
        mutableStateOf(adminResponse.is_active)
    }



    OutlinedCard(onClick = {}) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("ID: ")
                        }
                        append(adminResponse.id_admin.toString())
                    }
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("Full Name: ")
                        }
                        append(adminResponse.name_admin)
                    }
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("Email: ")
                        }
                        append(adminResponse.email_admin)
                    }
                )

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    "state",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )

                Switch(
                    checked = checkAdmin,
                    onCheckedChange = {
                        checkAdmin = it
                        if (checkAdmin)
                            onAccept(adminResponse.id_admin)
                        else
                            onRefuse(adminResponse.id_admin)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    thumbContent = {
                    }
                )

            }


        }
    }

}


@Composable
fun AdminsManagementScreen(viewModel: AdminManagementViewModel = viewModel()) {

    val activity = LocalContext.current as Activity
    var idOfSelectedUser by remember {
        mutableIntStateOf(0)
    }
    val isUpdate = viewModel.isUpdated.observeAsState()
    var state: Boolean = true
    val listObserveAsState = viewModel.adminLists.observeAsState()
    val error = viewModel.error.observeAsState()
    val isBlocked = viewModel.isAdminBlocked.observeAsState()
    val isActive = viewModel.isAdminActive.observeAsState()
    var showApproveAlert by remember {
        mutableStateOf(false)
    }

    var showAlert by remember {
        mutableStateOf(false)
    }


    AnimatedVisibility(visible = isUpdate.value == true) {

        SuccessAlert(title = "UPDATE SUCCESSFULLY", body = "operation change context made with!") {
            viewModel.isUpdated.postValue(false)
        }

    }





    AnimatedVisibility(visible = error.value != null) {
        if (viewModel.error.value != null)
            ErrorAlert(title = error.value!!.title, body = error.value!!.message) {
                viewModel.error.postValue(null)
            }

    }

    Scaffold(
        topBar = {
            Column {
                TopAppBarApplication(title = "Admins Management") {
                    activity.finish()
                }
                if (listObserveAsState.value == null) {  // not mistake it's smart cast we can not put isRequestSent!!
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

        }
    ) {
        Box(Modifier.padding(it)) {

            if (listObserveAsState.value != null)
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    itemsIndexed(
                        listObserveAsState.value!!
                    ) { _, item ->

                        AdminManagementItem(adminResponse = item,
                            onRefuse = { id ->
                                viewModel.changeStateOfAdmin(id, false)
                                showAlert = true
                            }) { id ->
                            viewModel.changeStateOfAdmin(id, true)
                            showAlert = true
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCard(employerResponse: EmployerResponse, onClickListener: ((id: Int) -> Unit)? = null) {
    OutlinedCard(onClick = {
        if (onClickListener != null) {
            onClickListener(employerResponse.id_employer)
        }
    }, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("ID: ")
                        }
                        append(employerResponse.identifier_employer)
                    }
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("Name: ")
                        }
                        append(employerResponse.name_employer)
                    }
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("Email: ")
                        }
                        append(employerResponse.email_employer)
                    }
                )
            }
            Icon(
                imageVector = if (employerResponse.is_active) Icons.Outlined.CheckCircle else Icons.Outlined.Close,
                contentDescription = "Icon State",
                tint = if (employerResponse.is_active) Color.Green else Color.Red
            )
        }
    }
}