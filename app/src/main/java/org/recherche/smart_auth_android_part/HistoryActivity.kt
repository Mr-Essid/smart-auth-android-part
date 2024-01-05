package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.recherche.smart_auth_android_part.componets.TopAppBarApplication
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.models.HistoryResponse
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.HistoryViewModel
import java.time.LocalDate
import java.time.LocalDateTime


class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HistoryScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview5() {
    SmartauthandroidpartTheme {
//        HistoryItem(HistoryResponse("2000-02-01T15:12:49.24779,", "2", 1, false))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(historyResponse: HistoryResponse) {

    val activity = LocalContext.current
    val datetime = LocalDateTime.parse(historyResponse.date_employer_enter)
    val date = datetime.toLocalDate()
    val time = datetime.toLocalTime()


    OutlinedCard(onClick = {
        Intent(activity, HistoryDetailsActivity::class.java).also {
            it.putExtra("time", time.toString())
            it.putExtra("date", date.toString())
            it.putExtra("idEmployer", historyResponse.id_employer)
            activity.startActivity(it)
        }
    }) {

        Row(
            Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Employer: ${historyResponse.id_employer}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "date: $date",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraLight
                )

                Text(
                    text = "time: $time",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraLight
                )
                Text(
                    text = "id : ${historyResponse.id_history}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraLight
                )
            }
            Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "Open History")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewmodel: HistoryViewModel = viewModel()) {

    val historyList = viewmodel.historyList.observeAsState()
    val employerSearched = viewmodel.employersSearched.observeAsState()
    val isNewOne = viewmodel.isThereNewOne.observeAsState()


    val calenderState = UseCaseState()
    var isThereIsFilterByDate by remember {
        mutableStateOf(false)
    }
    var isThereIsSearch by remember {
        mutableStateOf(false)
    }
    var query by remember {
        mutableStateOf("")
    }
    var isActiveSearchBar by remember {
        mutableStateOf(false)
    }

    var showCalendar by remember {
        mutableStateOf(false)
    }

    CalendarDialog(state = calenderState, selection =
    CalendarSelection.Date { date ->
        viewmodel.loadData(date = date)
    })

    if (isNewOne.value == true) {
        viewmodel.loadData()
        viewmodel.isThereNewOne.postValue(false)
    }


    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            TopAppBarApplication(
                title = "History",
                actions = {
                    if (!isThereIsSearch)
                        IconButton(onClick = { isActiveSearchBar = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Filter Action"
                            )
                        }
                    else {
                        IconButton(onClick = {
                            isThereIsSearch = false
                            viewmodel.loadData()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "Clear Search"
                            )
                        }
                    }

                    if (!isThereIsFilterByDate) {
                        IconButton(onClick = {
                            showCalendar = true
                            isThereIsFilterByDate = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "Filter By Date"
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            viewmodel.loadData()
                            isThereIsFilterByDate = false
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "Clear Filter By Date"
                            )
                        }
                    }
                }
            ) {
                activity.finish()
            }
        }
    ) {


        if (historyList.value == null) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            if (showCalendar) {
                calenderState.show()
                showCalendar = false
            }
            if (isActiveSearchBar)
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
                        query = query_; viewmodel.searchEmployer(query_)
                    },
                    onSearch = {},
                    active = isActiveSearchBar,
                    onActiveChange = {
                        isActiveSearchBar = it
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            isActiveSearchBar = false; query = ""; viewmodel.searchEmployer("")
                        }) {
                            Icon(imageVector = Icons.Outlined.Clear, contentDescription = "")
                        }
                    }

                ) {

                    LazyColumn(
                        modifier = Modifier.padding(
                            horizontal = 4.dp,
                            vertical = 8.dp
                        )
                    ) {
                        itemsIndexed(employerSearched.value!!) { _, item ->
                            CardItemSearch(employerResponse = item) { id ->
                                viewmodel.loadData(id)
                                query = ""
                                isActiveSearchBar = false
                                isThereIsSearch = true
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }

                }

            LazyColumn(
                Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (historyList.value != null)
                    itemsIndexed(historyList.value!!) { _, item ->
                        HistoryItem(historyResponse = item)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItemSearch(employerResponse: EmployerResponse, listener: (id_: Int) -> Unit) {

    Card(
        onClick = { listener(employerResponse.id_employer) }, modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "hello word")

            Column(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(
                    text = employerResponse.name_employer,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(9f, TextUnitType.Sp)
                        ),
                    ) {
                        append("ID: ")
                    }
                    append(employerResponse.identifier_employer)
                }, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)

            }

        }
    }
}
