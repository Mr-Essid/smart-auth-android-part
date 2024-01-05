package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.recherche.smart_auth_android_part.componets.ErrorAlert
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.EmployerViewModel

class EmployerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmployerScreen()
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun GreetingPreview4() {
    SmartauthandroidpartTheme {
        val employer = EmployerResponse(
            name_employer = "Amine",
            email_employer = "essid@go.com",
            lastname_employer = "Essid",
            identifier_employer = "emp01",
            create_at = "08/12/2002",
            id_employer = 2,
            is_active = false
        )

        ItemEmployer(
            employerResponse = employer
        ) {
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEmployer(employerResponse: EmployerResponse, onClickListener: ((id: Int) -> Unit)? = null) {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerScreen(viewmodel: EmployerViewModel = viewModel()) {

    val employers = viewmodel.listEmployers.observeAsState()
    val isError = viewmodel.error.observeAsState(initial = "")
    val employerSearched = viewmodel.searchedEmployers.observeAsState()
    val activity = LocalContext.current as Activity
    var isShowBottomSheet by remember {
        mutableStateOf(false)
    }

    var query by remember {
        mutableStateOf("")
    }

    var isSearchBarActive by remember {
        mutableStateOf(false)
    }
    val listOfStates = listOf("Active", "InActive", "All")
    var selectedIndex by remember {
        mutableIntStateOf(2)
    }
    val sheetState = rememberModalBottomSheetState()

    var filterIconChange by remember {
        mutableStateOf(false)
    }

    var searchIconChange by remember {
        mutableStateOf(false)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val data = it.data!!.getBooleanExtra("updated", false)
            println("We Are Here")
            if (data) {
                viewmodel.loadData()
                println("Data Loaded")
            }
        }
    )

    AnimatedVisibility(visible = isError.value!!.isNotEmpty()) {
        if (isError.value!! == "401")
            ErrorAlert(
                title = "UNAUTHORIZED REQUEST",
                body = "token expired you have to login again!"
            ) {
                activity.finish()
            }
    }


    Scaffold(
        topBar = {
            TopAppBarApplication(
                title = "Employers",
                actions = {
                    IconButton(onClick = {
                        isSearchBarActive = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "search employers"
                        )
                    }
                    if (!filterIconChange)
                        IconButton(onClick = {
                            isShowBottomSheet = true
                            filterIconChange = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = "filter employers",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    else
                        IconButton(onClick = {
                            filterIconChange = false
                            if (selectedIndex != 2) {
                                viewmodel.loadData()
                                selectedIndex = 2
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "remove filter"
                            )
                        }

                }
            ) {
                activity.finish()
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Intent(activity, AddEmployerActivity::class.java).also {
                    launcher.launch(it)
                }
            }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add Employer")
            }
        }
    ) {
        if (isShowBottomSheet)
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isShowBottomSheet = false },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "STATE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .padding(16.dp, 8.dp, 16.dp, 32.dp)
                        .fillMaxWidth()
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(listOfStates) { index, item ->
                            ElevatedAssistChip(onClick = {
                                selectedIndex = index
                                when (index) {
                                    0 -> {
                                        viewmodel.filterByState(true)
                                    }

                                    1 -> {
                                        viewmodel.filterByState(false)
                                    }

                                    else -> {
                                        viewmodel.loadData()
                                    }
                                }
                            }, label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = item, style = MaterialTheme.typography.labelSmall)
                                    if (selectedIndex == index) {
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = "icon checked"
                                        )
                                    }
                                }
                            }
                            )
                        }
                    }
                }
                Divider()
            }

        if (isSearchBarActive)
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Icons Search"
                    )
                },
                placeholder = {
                    Text(text = "Search")
                },
                onQueryChange = { query_ ->
                    query = query_; viewmodel.searchByName(query_); println(
                    "query $query_"
                )
                },
                onSearch = {},
                active = isSearchBarActive,
                onActiveChange = { isActiveNew ->
                    isSearchBarActive = isActiveNew
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewmodel.searchByName("")
                        query = ""
                        isSearchBarActive = false
                    }) {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = "")
                    }
                }

            ) {

                LazyColumn(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
                    itemsIndexed(employerSearched.value!!) { _, item ->
                        CardItemSearch(employerResponse = item) {
                            isSearchBarActive = false

                            Intent(activity, EmployerDetails::class.java).also { intent ->
                                intent.putExtra("id", it)
                                launcher.launch(intent)
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

            }
        if (employers.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else
            LazyColumn(
                Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(viewmodel.listEmployers.value!!) { _, item ->
                    ItemEmployer(employerResponse = item) {
                        Intent(activity, EmployerDetails::class.java).also { intent ->
                            intent.putExtra("id", it)
                            launcher.launch(intent)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
    }

}
