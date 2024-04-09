package com.example.ecoariumapp

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

public fun sendRegisterRequest(activity: Activity, username: String, password: String, passwordVerification: String, nickname: String) {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        json.put("password_verification", passwordVerification)
        json.put("nickname", nickname)

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://192.168.93.42:8000/auth/joinMobile")
            .post(body)
            .build()

        val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 네트워크 오류 처리
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            println("원본 = " + response.body)
            val responseBody = response.body?.string()


            // 로그인 성공 시 HomeActivity로 이동
            if (responseBody == "true") {
                Handler(Looper.getMainLooper()).post {
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
            } else {
                // 로그인 실패 시 토스트 메시지 표시
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(activity, responseBody, Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}