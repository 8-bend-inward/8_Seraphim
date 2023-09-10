package com.android.contactproject

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.android.contactproject.databinding.FragmentMyPageBinding
import com.bumptech.glide.Glide

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding
    private var selectedImageUri: Uri? = null
    private lateinit var v1: View

    private val REQUEST_CODE_PICK_IMAGE = 101

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    //위에 startActivityForResult로 시작된 액티비티가 종료 후 돌아올 때 호출
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            //이미지의 Uri를 가져온다.
            val uri = data?.data
            if (uri != null) {
                selectedImageUri = uri
                val setimage = v1.findViewById<ImageView>(R.id.dialog_image2)
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(setimage)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myImage.setImageResource(R.drawable.jisoo)
        binding.myName.text = "다민님바보"
        binding.myMobile.text = "010-1111-1111"
        binding.myEvent.text = "5분뒤 알림"

        binding.btnRevise.setOnClickListener {
            v1 = layoutInflater.inflate(R.layout.mypage_revise_dialog, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("내정보 수정")
                .setView(v1)
                .create()
            val setimage = v1.findViewById<ImageView>(R.id.dialog_image2)

            dialog.show()

            setimage.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT) //  다른 앱에서 저장한 파일을 열 수 있는 Intent
                intent.addCategory(Intent.CATEGORY_OPENABLE) // Intent를 처리할 수 있는 앱 중에서 파일 열기 또는 데이터 열기 기능을 제공하는 앱을 선별,
                // 사용자가 파일 선택기를 통해 파일을 선택
                intent.type = "image/*" //intent.type = " 이건 선택할 파일의 타입 "
                startActivityForResult(
                    intent,
                    REQUEST_CODE_PICK_IMAGE
                ) // startActivityForResult를 호출,
                // 아래 onActivityResult 함수에서 사용자가 선택한 이미지를 처리
            }

            val btnAccept = v1.findViewById<Button>(R.id.revise_acceptbtn)
            val btnCancel = v1.findViewById<Button>(R.id.revise_cancelbtn)
            val btnOff = v1.findViewById<Button>(R.id.off_btn)
            val btn5m = v1.findViewById<Button>(R.id.btn_5m)
            val btn10m = v1.findViewById<Button>(R.id.btn_10m)
            val btn30m = v1.findViewById<Button>(R.id.btn_30m)

            if (selectedImageUri != null) {
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.myImage)
            }

            val editTextName = v1.findViewById<EditText>(R.id.dialog_name2)
            val editTextPhoneNumber = v1.findViewById<EditText>(R.id.dialog_phone2)

            // 유효성 필드 완료 후 accept 버튼 활성화 시 필요
            var nameCheck = false
            var phoneCheck = false

            // name 텍스트 필드 유효성 검사
            editTextName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val englishPattern = Regex("^[a-zA-Z]{4,}\$")
                    val koreanPattern = Regex("^[가-힣]{2,}\$")
                    if (englishPattern.matches(editTextName.text) || koreanPattern.matches(
                            editTextName.text
                        )
                    ) {
                        editTextName.error = null
                        nameCheck = true
                    } else {
                        editTextName.error = "이름은 4글자 이상(영어) or 2글자 이상(한글) 입력"
                        nameCheck = false
                    }
                }
            })

            // 번호 필드 유효성 검사
            editTextPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val phonePattern = Regex("^\\d{11}$")
                    if (phonePattern.matches(editTextPhoneNumber.text)) {
                        editTextPhoneNumber.error = null
                        phoneCheck = true
                    } else {
                        editTextPhoneNumber.error = "올바르지 않은 전화번호 형식"
                        phoneCheck = false
                    }
                }
            })

            // event 필드 유효성 검사
            var selectedBtn: Button? = null

            btnOff.setOnClickListener {
                // 선택한 버튼이 선택된 버튼과 동일하지 않은 경우에 실행 (selectedBtn = null ,, btnOf = binding.btnOff)
                if (selectedBtn != btnOff) {
                    // 선택한 버튼이 이미 선택된 버튼일 경우, 선택 해제 상태
                    selectedBtn?.setBackgroundResource(R.drawable.normalbtn)
                    // 선택된 버튼의 배경색상을 선택 상태로 변경
                    btnOff.setBackgroundResource(R.drawable.changebtn)
                    // 선택한 버튼을 선택된 버튼으로 업데이트
                    selectedBtn = btnOff
                }
            }

            btn5m.setOnClickListener {
                if (selectedBtn != btn5m) {
                    selectedBtn?.setBackgroundResource(R.drawable.normalbtn)
                    btn5m.setBackgroundResource(R.drawable.changebtn)
                    selectedBtn = btn5m
                }
            }

            btn10m.setOnClickListener {
                if (selectedBtn != btn10m) {
                    selectedBtn?.setBackgroundResource(R.drawable.normalbtn)
                    btn10m.setBackgroundResource(R.drawable.changebtn)
                    selectedBtn = btn10m
                }
            }

            btn30m.setOnClickListener {
                if (selectedBtn != btn30m) {
                    selectedBtn?.setBackgroundResource(R.drawable.normalbtn)
                    btn30m.setBackgroundResource(R.drawable.changebtn)
                    selectedBtn = btn30m
                }
            }

            // 확인 버튼 클릭 시
            btnAccept.setOnClickListener {
                binding.myName.text = editTextName.text.toString()
                binding.myMobile.text = editTextPhoneNumber.text.toString()
                var newEvent = binding.myEvent.text

                if (selectedBtn == null) {
                    Toast.makeText(context, "알림 버튼을 설정해주세요 !", Toast.LENGTH_SHORT).show()
                }
                else if (nameCheck && phoneCheck) {
                    if (selectedBtn == btnOff) {
                        newEvent = "알림 OFF"
                    } else {
                        val inputName = editTextName.text.toString()
                        when (selectedBtn) {
                            // 5분일 경우
                            // btn5m -> handler.postDelayed({ reservationNotification(inputName) }, 5 * 60 * 1000)
                            // 시연을 위해 분이 아닌 초 단위로 변경함.
                            btn5m -> {
                                handler.postDelayed({ reservationNotification(inputName) }, 5000)
                                newEvent = "5분 뒤 알림"
                            }

                            btn10m -> {
                                handler.postDelayed({ reservationNotification(inputName) }, 10000)
                                newEvent = "10분 뒤 알림"
                            }

                            btn30m -> {
                                handler.postDelayed({ reservationNotification(inputName) }, 30000)
                                newEvent = "30분 뒤 알림"
                            }
                        }
                    }
                    dialog.dismiss()
                    binding.myEvent.text = newEvent
                    binding.myImage.setImageURI(selectedImageUri)
                }
            }
            //취소버튼 클릭 시
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }


    private fun reservationNotification(name: String) {
        val notificationManager =
            binding.root.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(binding.root.context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            binding.root.context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val notification = NotificationCompat.Builder(binding.root.context, "channel_id")
            .setContentTitle("\uD83D\uDEA8 8_Sheraphim 알림 \uD83D\uDEA8")
            .setContentText("$name 님이 연락을 기다리고 있어요...")
            .setSmallIcon(R.drawable.bell) // 알림 아이콘 설정
            // 알림 터치 시 제거
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // 알림 표시
        notificationManager.notify(1, notification) // 고유한 ID를 지정하여 알림을 구별
    }
}
