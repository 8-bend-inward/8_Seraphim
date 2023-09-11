package com.android.contactproject.detailPage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.android.contactproject.R
import com.android.contactproject.contactlist.UserDataModel
import com.android.contactproject.databinding.ActivityContactDetailBinding
import com.android.contactproject.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

class ContactDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
val ImageView = findViewById<ImageView>(R.id.character)
        // 데이터를 수신
        val userData = intent.getParcelableExtra<UserDataModel>("userData")

        if (userData != null) {
            // userData를 사용하여 필요한 작업을 수행
            binding.name2.text = "${userData.name}"
            binding.number.text = "${userData.ph}"
            Glide.with(this)
                .load(userData.userImage)//userDate에서 이미지뷰 가져오기
                .placeholder(R.drawable.man) // 기본 이미지 설정
                .error(R.drawable.man) // 로드 실패 시 이미지 설정
                .into(ImageView) // characterImageView에 이미지 설정
        }
        binding.back.setOnClickListener {
            finish()
        }
        binding.call.setOnClickListener { // 전화 걸기 Intent 생성
            val phoneNumber = userData?.ph // 전화번호 가져오기

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))

            //전화 앱 열기
            startActivity(intent)

        }
        binding.message.setOnClickListener {
            val phoneNumber = userData?.ph // 전화번호 가져오기

            val intent = Intent(Intent.ACTION_VIEW)// 메시지 보내기 화면 열기
            intent.data = Uri.parse("sms:$phoneNumber") // sms: 뒤에 수신자 전화번호 설정

            // 메시지 앱 열기
            startActivity(intent)
        }
    }
}


