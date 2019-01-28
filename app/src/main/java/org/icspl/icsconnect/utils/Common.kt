package org.icspl.icsconnect.utils

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.icspl.icsconnect.callbacks.ApiService
import org.icspl.icsconnect.networking.RetrofitClient
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Common {

    companion object {
        private val BASE_URL = "http://192.168.0.243:99/Home/"
        //private val BASE_URL = "http://icspl.org:5011/Home/"

        public fun getAPI(): ApiService {
            return RetrofitClient.getClient(BASE_URL).create(ApiService::class.java)
        }
    }

    fun convertMultipart(imagePath: String?, paramName: String): MultipartBody.Part {
        val mRequestBody: RequestBody
        val body5: MultipartBody.Part
        if (imagePath != null)
            if (!imagePath.equals("NA", ignoreCase = true)) {
                val mFile = File(imagePath)
                mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
                body5 = MultipartBody.Part.createFormData(paramName, mFile.name, mRequestBody)
                Log.i("Multipart body", "convertMultipart: " + mFile.name)
            } else {
                mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
                body5 = MultipartBody.Part.createFormData(paramName, "NA", mRequestBody)
            }
        else {
            mRequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            body5 = MultipartBody.Part.createFormData(paramName, "NA", mRequestBody)
        }
        return body5
    }

    fun getFormatedDate(date: String): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        val sdate = dateFormat.parse(date)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val newDate = formatter.format(sdate)

        return newDate
    }

    public fun getDateFormat(): String {
        val mSDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return mSDF.format(Date())
    }

    public fun getTime(): String {
        val mSDF = SimpleDateFormat("HH:mm", Locale.getDefault())
        return mSDF.format(Date())
    }

}
