package org.recherche.smart_auth_android_part

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.MutableBoolean
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisConfig
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import org.recherche.smart_auth_android_part.models.DayInfo
import org.recherche.smart_auth_android_part.ui.theme.SmartauthandroidpartTheme
import org.recherche.smart_auth_android_part.view_models.DashboardViewModel
import java.time.LocalDate

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartauthandroidpartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SessionManagement(this.application).removeToken()
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogFrom() {
    AlertDialog(onDismissRequest = { /*TODO*/ }) {
        Column {
            OutlinedTextField(
                value = "info",
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "ip address",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
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
        }
    }
}


@Composable
fun DashboardScreen(viewmodel: DashboardViewModel = viewModel()) {

    val mainPadding = 16.dp
    val currentAdmin = viewmodel.currentAdmin.observeAsState()
    val activity = LocalContext.current
    var showDialogStream by remember {
        mutableStateOf(false)
    }

    val dataOfBar = viewmodel.lastFiveDaysData.observeAsState()

    if (showDialogStream) {
        DialogStream({
            showDialogStream = false
        }) { ip, port ->
            Intent(activity, StreamActivity::class.java).also {
                it.putExtra("ipaddress", ip)
                it.putExtra("port", port)
                activity.startActivity(it)
                showDialogStream = false
            }
        }
    }

    if (currentAdmin.value == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else
        Scaffold { it ->
            Box(
                Modifier
                    .alpha(if (showDialogStream) 0.6f else 1f)
                    .padding(it)
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.3f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            0.6f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.9f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            1f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    )
            ) {

                Column {
                    HeaderPre(username = viewmodel.currentAdmin.value!!.name_admin.split(' ')[0]) {
                        println("Clicked")
                    }


                    Column(Modifier.padding(mainPadding)) {


                        if (viewmodel.currentAdmin.value!!.authority == 0) {
                            ItemsOfBean("Admins", R.drawable.administrator) {
                                activity.startActivity(
                                    Intent(
                                        activity,
                                        AdminsManagementActivity::class.java
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(mainPadding / 2))
                        }
                        ItemsOfBean("History", R.drawable.file) {
                            activity.startActivity(
                                Intent(
                                    activity,
                                    HistoryActivity::class.java
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Employers", R.drawable.admin_add) {
                            Intent(activity, EmployerActivity::class.java).also { intent ->
                                activity.startActivity(intent)
                            }
                        }
                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Live", R.drawable.live_streaming) {
                            showDialogStream = true
                        }

                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Settings", R.drawable.settings) {
                            Intent(activity, LogoutActivity::class.java).also { intent ->
                                activity.startActivity(intent)
                            }
                        }
                    }


                }

            }
        }

}


@Composable
fun HeaderPre(username: String, listener: () -> Unit) {


    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEndPercent = 16, bottomStartPercent = 16))
                .background(MaterialTheme.colorScheme.background)
                .height(110.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            Text(
                text = "Welcome, $username",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = listener) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "setting",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsOfBean(text: String, id_: Int, listener: () -> Unit) {


    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            listener()
        }

//        onClick = {
//            if (text == "Users"){
//                val intent = Intent(context, UserActivity::class.java)
//                context.startActivity(intent)
//            }else if (text == "Admins") {
//
//
//            }else if (text == "Cars") {
//                context.startActivity(Intent(context, CarsActivity::class.java))
//
//            }else if (text == "History") {
//                val intent = Intent(context, HistoryActivity::class.java)
//                context.startActivity(intent)
//
//            }else if (text == "Setting") {
//                val intent = Intent(context, SettingActivity::class.java)
//                context.startActivity(intent)
//            }else {
//                Log.d("TAG", "ItemsOfBean: $text")
//            }
//
//        }

    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = id_),
                contentDescription = "Hello World",
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "active",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.Green
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "show more")
            }

        }
    }

}


@Composable
fun DialogStream(dissmis_: () -> Unit, open: (ip: String, port: Long) -> Unit) {
    var ipAddress by remember {
        mutableStateOf("")
    }
    var portNumber by remember {
        mutableStateOf("")
    }
    Dialog(
        onDismissRequest = {
            dissmis_()
        }
    ) {

        Card(
            modifier = Modifier, colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Icon(
                    painter = painterResource(id = R.drawable.livestreaming),
                    contentDescription = "open stream",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "OPEN STREAM",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                    },
                    placeholder = {
                        Text(
                            text = "ip address",
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = {
                        Icon(
                            painter = painterResource(id = R.drawable.ip_address),
                            contentDescription = "image view",
                            Modifier
                                .padding(0.dp, 0.dp, 4.dp, 0.dp)
                                .size(24.dp)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = portNumber,
                    onValueChange = {
                        portNumber = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                    placeholder = {
                        Text(
                            text = "port",
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = {
                        Icon(
                            painter = painterResource(id = R.drawable.application),
                            contentDescription = "image view",
                            Modifier
                                .padding(0.dp, 0.dp, 4.dp, 0.dp)
                                .size(24.dp)
                        )
                    }

                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { dissmis_() }) {
                        Text(text = "cancel")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { open(ipAddress, portNumber.toLong()) }) {
                        Text(text = "agree")
                    }
                }
            }
        }


    }
}


@Composable
fun BarChartDataFromDB(daysInfo: List<DayInfo>) {

    val configuration = LocalConfiguration.current
    val colors = listOf(Color.Black, Color.Yellow, Color.Cyan, Color.Green, Color.LightGray)
    val ourList = mutableListOf<BarData>()
    var maxNumber = 0
    for (i in daysInfo.indices) {
        val bar = BarData(
            Point(
                i.toFloat(),
                daysInfo[i].howMany.toFloat(),
            ),
            label = daysInfo[i].day.toString(),
            color = colors[i]
        )
        if (daysInfo[i].howMany > maxNumber) {
            maxNumber = daysInfo[i].howMany
        }
        ourList.add(bar)
    }


    val xAxisData = AxisData.Builder()
        .axisStepSize(400.dp)
        .axisConfig(AxisConfig())

        .steps(daysInfo.size)
        .labelData { index -> daysInfo[index].day.split("-").last() }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(maxNumber / 10)
        .axisStepSize(30.dp)
        .labelAndAxisLinePadding(20.dp)
        .labelData { index -> (index * (10)).toString() }
        .build()


    val barChartDataOne = BarChartData(
        chartData = ourList,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
    )


    BarChart(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        barChartData = barChartDataOne
    )

}


@Preview(showSystemUi = true)
@Composable
fun GetAllData() {
    BarChartDataFromDB(
        daysInfo =
        listOf(
            DayInfo("32/32/323", 10),
            DayInfo("43234/3423/43234", 50),
            DayInfo("32/32/323", 50),
            DayInfo("32/32/323", 30),
            DayInfo("32/32/323", 60),
        )
    )
}