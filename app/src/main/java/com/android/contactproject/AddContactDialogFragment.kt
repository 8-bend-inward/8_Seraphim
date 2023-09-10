package com.android.contactproject

import android.app.Activity.RESULT_OK
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.android.contactproject.databinding.FragmentAddContactDialogBinding
import com.bumptech.glide.Glide

class AddContactDialogFragment : DialogFragment() {
    private val binding by lazy { FragmentAddContactDialogBinding.inflate(layoutInflater) }
    lateinit var addMemberResult: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    private var inputName: String = ""

    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 유효성 필드 완료 후 accept 버튼 활성화 시 필요
        var imageCheck = false
        var nameCheck = false
        var phoneCheck = false
        var addressCheck = false
        var inputname: String

        val lesserafimList = ArrayList<AddMemberData>()
        binding.apply {
            imageButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                addMemberResult.launch(intent)
            }
        }
        addMemberResult = registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                uri = it.data!!.data

                Glide.with(this)
                    .load(uri)
                    .into(binding.dialogImage)

                imageCheck = true
            }
        }

        // imageView, EditText 선언
        val name = binding.dialogName
        val phone = binding.dialogPhone
        val address = binding.dialogAddress
        val btnOff = binding.offBtn
        val btn5m = binding.btn5m
        val btn10m = binding.btn10m
        val btn30m = binding.btn30m

        // name 텍스트 필드 유효성 검사
        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val englishPattern = Regex("^[a-zA-Z]{4,}\$")
                val koreanPattern = Regex("^[가-힣]{2,}\$")
                if (englishPattern.matches(name.text) || koreanPattern.matches(name.text)) {
                    name.error = null
                    nameCheck = true
                } else {
                    name.error = "이름은 4글자 이상(영어) or 2글자 이상(한글) 입력"
                    nameCheck = false
                }
            }
        })

        // 번호 필드 유효성 검사
        phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phonePattern = Regex("^\\d{11}$")
                if (phonePattern.matches(phone.text)) {
                    phone.error = null
                    phoneCheck = true
                } else {
                    phone.error = "올바르지 않은 전화번호 형식"
                    phoneCheck = false
                }
            }
        })

        // 이메일 필드 유효성 검사
        address.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailPattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3}+)$")

                if (emailPattern.matches(address.text)) {
                    address.error = null
                    addressCheck = true
                } else {
                    address.error = ("올바르지 않는 이메일 형식")
                    addressCheck = false
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
        // 취소 버튼 클릭 시
        binding.dialogCancelbtn.setOnClickListener {
            // dialog 닫기
            dismiss()
        }
        // 확인 버튼 클릭 시
        binding.dialogAcceptbtn.setOnClickListener {
            if (!imageCheck || selectedBtn == null) {
                if (!imageCheck)
                    Toast.makeText(context, "사진을 추가해주세요 !", Toast.LENGTH_SHORT).show()
                if (selectedBtn == null)
                    Toast.makeText(context, "알림 버튼을 설정해주세요 !", Toast.LENGTH_SHORT).show()
            }
            else if (imageCheck && nameCheck && phoneCheck && addressCheck) {
                if (selectedBtn != btnOff) {
                    inputName = name.text.toString()
                    val inputPhone = phone.text.toString()
                    val inputAddress = address.text.toString()
                    val inputProfile = uri ?: return@setOnClickListener
                    val lesserafim = AddMemberData(inputProfile,inputName, inputPhone,inputAddress)
                    lesserafimList.add(lesserafim)
                    Log.d("ContactProjects", "다이얼로그에서 넘기는 데이터 ${ lesserafimList}")
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("FromDialog", lesserafimList)
                    setFragmentResult("FromDialogKey",bundle)
                    when (selectedBtn) {
                        // 5분일 경우
                        // btn5m -> handler.postDelayed({ reservationNotification(inputName) }, 5 * 60 * 1000)

                        // 시연을 위해 분이 아닌 초 단위로 변경함.
                        btn5m -> handler.postDelayed({ reservationNotification(inputName) }, 5000)
                        btn10m -> handler.postDelayed({ reservationNotification(inputName) }, 10000)
                        btn30m -> handler.postDelayed({ reservationNotification(inputName) }, 30000)
                    }

                }
                dismiss()
            } else {
                Toast.makeText(context, "형식에 맞지 않은 정보가 존재합니다.", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
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
