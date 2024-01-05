package org.recherche.smart_auth_android_part.view_models

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.services.EmployerService
import retrofit2.http.Header
import java.io.File
import java.net.URI

class AddEmployerViewModel(application: Application) : AndroidViewModel(application) {
    private val token = SessionManagement(application).getToken()
    private val expTime = SessionManagement(application).getExpTimeAsString()
    private val employerService = EmployerService()
    var imageBytes: ByteArray? = null

    var isThereIsOneAdd = false
    var isRequestSent = MutableLiveData(false)
    var employerIdentifier = mutableStateOf("")
    var employerName = mutableStateOf("")
    var employerLastname = mutableStateOf("")
    var employerEmail = mutableStateOf("")
    val error = MutableLiveData("")
    val isAdded = MutableLiveData(false)


    fun addEmployer() {

        if (
            employerIdentifier.value.isEmpty() ||
            !employerIdentifier.value.contains("emp")
        ) {
            isRequestSent.postValue(false)
            error.postValue("-1")
            return
        } else if (
            employerName.value.length < 4 ||
            employerLastname.value.length < 4
        ) {
            isRequestSent.postValue(false)
            error.postValue("-2")
            return

        } else if (
            employerEmail.value.length < 4 ||
            !employerEmail.value.contains('@')
        ) {
            isRequestSent.postValue(false)
            error.postValue("-3")

            return
        }
        if (imageBytes == null) {
            isRequestSent.postValue(false)
            error.postValue("-4")
            return
        }


//
//        val body = MultipartBody.Part.create(requestBody)
//        val employerNameBody =
//            MultipartBody.Part.create(employerName.value.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
//        val employerEmailBody =
//            MultipartBody.Part.create(employerEmail.value.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
//        val employerLastNameBody = employerLastname.value.toRequestBody("multipart/form-data".toMediaTypeOrNull()
//
////        val employerIdentifierBody = employerIdentifier.value.toRequestBody("multipart/form-data".toMediaTypeOrNull()


        val body = imageBytes!!.toRequestBody(
            "application/octet-stream".toMediaType()
        )
        val header = Headers.Builder()

        val multipartBody = MultipartBody.Part.createFormData("file", "filename.png", body)


        val name = employerName.value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val lastname =
            employerLastname.value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val email = employerEmail.value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val identifier =
            employerIdentifier.value.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelScope.launch {
            val response = employerService.addEmployer(
                token,
                multipartBody,
                name,
                lastname,
                email,
                identifier
            )
            isRequestSent.value = false
            if (response.code() == 200 && response.body() != null) {
                isThereIsOneAdd = true
                isAdded.postValue(true)
            } else {
                error.postValue(response.code().toString())
                println("response is ${response.code()}")
            }
        }
    }

}


//@SuppressLint("Recycle")
//@Preview
//@Composable
//fun GetImage() {
//    val context = LocalContext.current as Activity
//    var fileAsByteArray: ByteArray
//
//
//    val picker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = {
//            it!!.let { uri ->
//                println("uri is $uri")
//                val inputStream = context.contentResolver.openInputStream(uri)
//                println(inputStream!!.available().toString() + "Hello World")
//                fileAsByteArray = ByteArray(inputStream.available())
//                inputStream.read(fileAsByteArray)
//
//            }
//        })
//
//    Button(onClick = {
//        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }) {
//        Text(text = "get image")
//    }
//
//
//}