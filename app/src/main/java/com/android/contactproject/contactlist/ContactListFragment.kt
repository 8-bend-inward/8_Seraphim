package com.android.contactproject.contactlist

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.contactproject.AddContactDialogFragment
import com.android.contactproject.AddMemberData
import com.android.contactproject.FavoritesAdapter
import com.android.contactproject.R
import com.android.contactproject.SwipeToCall
import com.android.contactproject.databinding.ContactListFragmentBinding
import com.android.contactproject.detailPage.ContactDetailActivity


class ContactListFragment : Fragment() {

    //    private lateinit var listAdapter: ContactListFragmentAdapter
    private var listAdapter: ContactListFragmentAdapter? = null //위에꺼쓰면 null값처리못해서 플로팅버튼눌렀을떄 앱팅깁니다.
    private var list = arrayListOf<UserDataModel>()
    private val getDialogList = ArrayList<AddMemberData>()
    private var isContactDataLoaded = false

    private var _binding: ContactListFragmentBinding? = null
    private val binding get() = _binding!!
    val listArray = arrayListOf<UserDataModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentFragmentManager.setFragmentResultListener(
            "ToContactListKey",
            this
        ) { key, result ->
            val getFavorites = result.getParcelableArrayList<UserDataModel>("ToContactList")
            Log.d("ContactProjects", "Favorites에서 다시 받아온 데이터 : $getFavorites")
            getFavorites?.forEach { item ->
                val index = list.indexOfFirst { it.name == item.name }
                list[index].isLike = false
            }
//            listAdapter.notifyDataSetChanged()
            binding.contactListRe.adapter?.notifyDataSetChanged()
        }
        parentFragmentManager.setFragmentResultListener("FromDialogKey", this) { key, result ->
            val getDialog = result.getParcelableArrayList<AddMemberData>("FromDialog")
            Log.d("ContactProjects", "다이얼로그에서 다시 받아온 데이터 : ${getDialog}")
            if(getDialog !=null){
                getDialogList.addAll(getDialog)
            }
            binding.contactListRe2.apply {
                adapter = ContactListItemAdapter(getDialogList)
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                setHasFixedSize(true)
            }
        }


        _binding = ContactListFragmentBinding.inflate(inflater, container, false)
        list.apply {
            add(
                UserDataModel(
                    R.drawable.chaewon, "010-4165-8974",
                    "채원"
                )
            )
            add(
                UserDataModel(
                    R.drawable.eunchae, "010-2717-2038",
                    "은채"
                )
            )
            add(
                UserDataModel(
                    R.drawable.kazuha, "010-1230-4518",
                    "카즈하"
                )
            )
            add(
                UserDataModel(
                    R.drawable.sakura, "010-2468-9510",
                    "사쿠라"
                )
            )
            add(
                UserDataModel(
                    R.drawable.yunjin, "010-7833-2232",
                    "허윤진"
                )
            )
            add(
                UserDataModel(
                    R.drawable.gaeul, "010-4467-2892",
                    "가을"
                )
            )
            add(
                UserDataModel(
                    R.drawable.iseo, "010-6541-6672",
                    "이서"
                )
            )
            add(
                UserDataModel(
                    R.drawable.ray, "010-6315-7632",
                    "레이"
                )
            )
            add(
                UserDataModel(
                    R.drawable.wonyoung, "010-7777-7777",
                    "장원영"
                )
            )
            add(
                UserDataModel(
                    R.drawable.yujin, "010-6547-1466",
                    "안유진"
                )
            )
            add(
                UserDataModel(
                    R.drawable.winter, "010-9788-8564",
                    "윈터"
                )
            )
            add(
                UserDataModel(
                    R.drawable.karina, "010-5436-1592",
                    "카리나"
                )
            )
            add(
                UserDataModel(
                    R.drawable.eunbi, "010-6154-9511",
                    "권은비"
                )
            )
            add(
                UserDataModel(
                    R.drawable.miyeon, "010-7750-0987",
                    "미연"
                )
            )
            add(
                UserDataModel(
                    R.drawable.ugi, "010-6543-7782",
                    "우기"
                )
            )
            add(
                UserDataModel(
                    R.drawable.yena, "010-2137-8224",
                    "최예나"
                )
            )
            add(
                UserDataModel(
                    R.drawable.yuna, "010-3357-6669",
                    "유나"
                )
            )

        }
        val sort_Lesserafim = ArrayList(list.sortedBy { it.name })
        binding.contactListRe.layoutManager =
            LinearLayoutManager(context)
        UpdataContact(list, FavoritesAdapter.listViewType)
        binding.contactSelect.setOnClickListener {
            Log.d("ContactProjects", "버튼눌려지고있냐?")
            val menu = PopupMenu(context, it)
            menu.menuInflater.inflate(R.menu.contactmenu, menu.menu)
            if (list != null) {
                menu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.contact_ListView -> {
                            binding.contactListRe.layoutManager =
                                LinearLayoutManager(context)
                            UpdataContact(list, FavoritesAdapter.listViewType)
                            true
                        }

                        R.id.contact_GridView -> {
                            binding.contactListRe.layoutManager =
                                GridLayoutManager(context, 3)
                            UpdataContact(list, FavoritesAdapter.gridViewType)
                            true
                        }

                        R.id.contact_Sort -> {
                            binding.contactListRe.layoutManager =
                                LinearLayoutManager(context)
                            UpdataContact(sort_Lesserafim, FavoritesAdapter.listViewType)
                            true
                        }
                        R.id.contact_PhoneBook ->{
                            if (!isContactDataLoaded) {
                                Log.d("contact", "btnaddmember isContactDataLoaded = $isContactDataLoaded")
                                requestContactsPermission()

                            } else {
                                refreshContactList()
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
            menu.show()
        }

        binding.btnaddmember.setOnClickListener {

            val popUp = AddContactDialogFragment()
            popUp.show((activity as AppCompatActivity).supportFragmentManager, "popUp")
        }
//        initView()

        return binding.root
    }
//    private fun initView() {
//        binding.contactListRe.layoutManager = LinearLayoutManager(context)
//        binding.contactListRe.adapter = listAdapter
//        listAdapter.replace(list)
//        binding.contactListRe.setHasFixedSize(true)
//
//        // ItemTouchHelper를 초기화하고 RecyclerView에 연결
//        val itemTouchHelper = ItemTouchHelper(SwipeToCall(requireContext(), listAdapter))
//        itemTouchHelper.attachToRecyclerView(binding.contactListRe)
//    }

    fun UpdataContact(list: ArrayList<UserDataModel>, viewType: Int) {
        binding.contactListRe.apply {
            adapter = ContactListFragmentAdapter(list, viewType).apply {
                itemClick = object : ContactListFragmentAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        val item = list[position]
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("즐겨찾기")
                        builder.setMessage("즐겨찾기를 하시겠읍니까?")
                        Log.d("ContactProjects", "되돌아왔을떄 데이터 : ${item}")
                        val listener = object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        item.isLike = !item.isLike
                                        if (item.isLike) {
                                            list.removeAt(position)
                                            list.add(0, item)
                                            if (!listArray.contains(item)) {
                                                listArray.add(item)
                                            }
                                        } else {
                                            listArray.remove(item)
                                        }
                                        notifyDataSetChanged()
                                        val bundle = Bundle()
                                        bundle.putParcelableArrayList("ToFavorites", listArray)
                                        setFragmentResult("ToFavoritesKey", bundle)
                                    }

                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        dialog?.dismiss()
                                    }
                                }
                            }
                        }
                        builder.setPositiveButton("확인", listener)
                        builder.setNegativeButton("취소", listener)

                        builder.show()
                    }


//              override fun onImageLongClick(view: View, position: Int) {
//                  val bundle = Bundle()
//                  val item = list[position]
//                  bundle.putParcelable("UserData", item)
//                  val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                  val contactDetailFragment = ContactDetailFragment()
//                 contactDetailFragment.arguments = bundle
//                  transaction.replace(R.id.main_layout, contactDetailFragment)
//                  transaction.commit()
//              }

                    override fun onImageLongClick(view: View, position: Int) {
                        val item = list[position]

                        // 데이터를 Intent에 추가
                        val intent = Intent(requireContext(), ContactDetailActivity::class.java)
                        intent.putExtra("userData", item)

                        // ContactDetailActivity를 시작
                        startActivity(intent)
                    }
                }
            }
            val itemTouchHelper = when (binding.contactListRe.adapter) {
                is ContactListFragmentAdapter -> ItemTouchHelper(
                    SwipeToCall(
                        requireContext(),
                        binding.contactListRe.adapter as ContactListFragmentAdapter
                    )
                )
                else -> null
            }

            if (itemTouchHelper != null) {
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    //주소록 읽기 권한 요청
    private fun requestContactsPermission() {
        val permission = Manifest.permission.READ_CONTACTS
        val requestCode = 101

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 이미 권한이 허용된 경우 주소록 정보 가져오기
            fetchContacts()

        } else {
            // 권한 요청
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    // 주소로 데이터 가져오기
    private fun fetchContacts() {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor: Cursor? = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.use { cursor ->
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneNumberIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            val updatedList = mutableListOf<UserDataModel>() // 업데이트된 리스트 생성

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                val phoneNumber = cursor.getString(phoneNumberIndex)

                val userData = UserDataModel(R.drawable.jisu, phoneNumber, name, false)

                updatedList.add(userData) // 업데이트된 리스트에 아이템 추가
            }

            list.addAll(0,updatedList) // 새로운 데이터로 업데이트

            // 데이터를 불러왔으므로 플래그 업데이트
            isContactDataLoaded = true

            // 새로고침 수행
            refreshContactList()
        }
    }

    private fun refreshContactList() {
        // RecyclerView 갱신 로직 추가
        binding.contactListRe.adapter?.notifyDataSetChanged()
    }

    //권한 요청 다이얼로그 응답시 수행하는코드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("contact", "requestCode = $requestCode")
        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("contact", "PERMISSION_GRANTED")// 사용자가 권한을 허용한 경우 주소록 정보 가져오기
                    fetchContacts()
                    refreshContactList() // 화면 새로고침
                } else {
                    //거부하면 메세지 출력
                    Toast.makeText(requireContext(), "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    private fun initView() = with(binding) {
//
//        contactListRe.layoutManager = LinearLayoutManager(context)
//        contactListRe.adapter = listAdapter
//        listAdapter.replace(list)
//        contactListRe.setHasFixedSize(true)
//        // ItemTouchHelper를 초기화하고 RecyclerView에 연결
//        val itemTouchHelper = ItemTouchHelper(SwipeToCall(requireContext(), listAdapter))
//        itemTouchHelper.attachToRecyclerView(contactListRe)
//    }
//private fun initView() {
//    binding.contactListRe.layoutManager = LinearLayoutManager(context)
//    binding.contactListRe.adapter = listAdapter
//    listAdapter.replace(list)
//    binding.contactListRe.setHasFixedSize(true)
//    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}