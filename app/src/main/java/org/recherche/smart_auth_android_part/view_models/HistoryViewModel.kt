package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.recherche.smart_auth_android_part.R
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.models.HistoryResponse
import org.recherche.smart_auth_android_part.services.EmployerService
import org.recherche.smart_auth_android_part.services.HistoryService
import retrofit2.Response
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.time.LocalDate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    val token = "Bearer ${SessionManagement(application).getToken()}"
    val expTimeAsString = SessionManagement(application).getExpTimeAsString()
    val employersSearched = MutableLiveData<List<EmployerResponse>>(emptyList())
    val historyList = MutableLiveData<List<HistoryResponse>?>(null)
    val isThereNewOne = MutableLiveData(false)
    val mqttclient = MqttAndroidClient(
        application,
        "YOUR-SECRET-BROKER-URI",
        "android-client"
    )

    private val clientCallbacks = object : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            println("Connection Lost")

        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            if (topic == "/history") {
                historyList.postValue(null)

                loadData()
            }

        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d("TAG", "deliveryComplete: token")
        }

    }
    val cert = application.resources?.openRawResource(R.raw.server1)

    val error = MutableLiveData("")
    val historyService = HistoryService()
    val employerService = EmployerService()

    init {
        loadData()
        mqttConfig()
    }

    fun loadData(id: Int? = null, date: LocalDate? = null) {
        viewModelScope.launch {
            val response = if (id != null)
                historyService.getHistoryOfEmployer(token, id)
            else if (date != null)
                historyService.getHistoryByDate(token, date)
            else
                historyService.getAllHistory(token)



            if (response.body() != null || response.code() == 200) {
                historyList.postValue(response.body())
            } else
                error.postValue(response.code().toString())
        }
    }

    fun searchEmployer(keyword: String) {

        if (keyword.isNotEmpty())
            viewModelScope.launch {
                val response = employerService.searchEmployerByName(token, keyword)
                if (response.body() != null && response.code() == 200) {
                    employersSearched.postValue(response.body())
                    println("Your Response ${response.body()}")
                } else
                    error.postValue(response.code().toString())
            }
        else
            employersSearched.postValue(emptyList())
    }


    fun mqttConfig() {
        // setup certification

        val cf = CertificateFactory.getInstance("X.509")
        // certification with not existe in the code herarchie but you can setup yours through create a cloud mqtt and get there certifiaction

        val ca = cf.generateCertificate(cert)
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val tmfAlgo = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgo)
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)

        //sslContext.socketFactory

        val params_ = MqttConnectOptions()
        params_.userName = "YOUR-USERNAME"
        params_.password = "BROKER-PASSWORD".toCharArray()
        params_.socketFactory = sslContext.socketFactory

        val token = mqttclient.connect(params_)


        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                println("Connected")
                mqttclient.addCallback(clientCallbacks)
                mqttclient.subscribe("/history", 0).actionCallback =
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            println("Action Subscribe Approve")
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            println("There Problem Of SUB")
                        }

                    }
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                println("Connection Lost")
            }

        }


    }


    override fun onCleared() {
        mqttclient.disconnect()
        super.onCleared()
    }

}