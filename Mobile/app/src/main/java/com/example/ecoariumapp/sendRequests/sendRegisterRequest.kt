package com.example.ecoariumapp.sendRequests

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.ecoariumapp.IpConfig
import com.example.ecoariumapp.activities.LoginActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

// 회원가입 요청 보내기
fun sendRegisterRequest(activity: Activity, username: String, password: String, passwordVerification: String, nickname: String, email: String) {
    // JSON 객체 생성
    val json = JSONObject()
    json.put("username", username)
    json.put("password", password)
    json.put("password_verification", passwordVerification)
    json.put("nickname", nickname)
    json.put("email", email)

    // 미디어 타입 설정
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val body = json.toString().toRequestBody(mediaType)

    // 요청 객체 생성
    val request = Request.Builder()
        .url("http://${IpConfig.serverIp}:8000/auth/joinMobile")
        .post(body)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 네트워크 오류 처리
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()

            // 회원가입 성공 시 LoginActivity로 이동
            if (responseBody == "true") {
                Handler(Looper.getMainLooper()).post {
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
            } else {
                // 회원가입 실패 시 토스트 메시지 표시
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(activity, responseBody, Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity", responseBody.toString())
                }
            }
        }
    })
}