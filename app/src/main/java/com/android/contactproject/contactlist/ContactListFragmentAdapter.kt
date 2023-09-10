package com.android.contactproject.contactlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.contactproject.FavoritesAdapter
import com.android.contactproject.R
import com.android.contactproject.databinding.ContactListItemBinding
import com.android.contactproject.databinding.ContactListItemTypeBinding
import com.android.contactproject.databinding.GridTypeItemBinding


class ContactListFragmentAdapter(val list: ArrayList<UserDataModel>,private var viewType: Int) : RecyclerView
.Adapter<RecyclerView.ViewHolder>
    () {

    interface ItemClick {
        fun onClick(view: View, position: Int)
        fun onImageLongClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    companion object {
        const val CALL_PHONE_PERMISSION_REQUEST_CODE = 123
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
           ViewType.leftType -> {
               val binding = ContactListItemBinding.inflate(LayoutInflater.from(parent.context),
                   parent,false)
               ViewHolder(binding)
           }
            ViewType.rightType ->{
                val binding = ContactListItemTypeBinding.inflate(LayoutInflater.from(parent
                    .context),parent,false)
                ViewHolder2(binding)
            }
            3 -> {
                val binding = GridTypeItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent,false)
                GridHolder(binding)
            }
            else -> throw IllegalArgumentException("잘못된 viewType 입니다.")
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when(holder){
            is ViewHolder ->{
               holder.bind(item)
            }
            is ViewHolder2 ->{
                holder.bind(item)
            }
            is GridHolder ->{
                holder.bind(item)
            }
        }


    }

    inner class ViewHolder(
        private val binding: ContactListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserDataModel) = with(binding) {
            // 데이터
            userName.text = item.name
            phNumber.text = item.ph
            profileImage.setImageResource(item.userImage)

            itemView.setOnLongClickListener {
                itemClick?.onImageLongClick(it, position)
                true
            }
            like.setOnClickListener {
                itemClick?.onClick(it, position)
            }
            if (list[position].isLike) {
                binding.mainLike.setImageResource(R.drawable.painted_heart)
            } else {
                binding.mainLike.setImageResource(R.drawable.heart)
            }

        }


        val profile = binding.profileImage
        val like = binding.mainLike

    }

    inner class ViewHolder2(private val binding: ContactListItemTypeBinding) : RecyclerView
    .ViewHolder
        (binding.root) {
        fun bind(item: UserDataModel) {
            binding.apply {
                typeProfileImage.setImageResource(item.userImage)
                typeUserName.text = item.name
                typePhNumber.text = item.ph

                itemView.setOnLongClickListener {
                    itemClick?.onImageLongClick(it, position)
                    true
                }
                like.setOnClickListener {
                    itemClick?.onClick(it, position)
                }
                if (list[position].isLike) {
                    binding.typeMainLike.setImageResource(R.drawable.painted_heart)
                } else {
                    binding.typeMainLike.setImageResource(R.drawable.heart)
                }
            }
        }

        val like = binding.typeMainLike
    }

    inner class GridHolder(private val binding: GridTypeItemBinding): RecyclerView.ViewHolder
        (binding.root){
            fun bind(item:UserDataModel){
                binding.apply {
                    GridProfile.setImageResource(item.userImage)
                    GridName.text = item.name
                }
            }
        }

    fun replace(newList: MutableList<UserDataModel>) {
        notifyDataSetChanged()
    }

    fun makePhoneCall(context: Context, position: Int) {
        val phoneNumber = list[position].ph
        // 전화를 걸기 위한 Intent를 생성
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")

        // CALL_PHONE 권한이 있는지 확인 DialogInterface.BUTTON_POSITIVE
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            context.startActivity(intent) // 전화 걸기
        } else {
            // 권한이 없는 경우 권한 요청 로직
            ActivityCompat.requestPermissions(
                context as Activity, // context를 Activity로 캐스팅
                arrayOf(android.Manifest.permission.CALL_PHONE),
                CALL_PHONE_PERMISSION_REQUEST_CODE
            )
        }
    }



    override fun getItemViewType(position: Int): Int {
        if (viewType == FavoritesAdapter.listViewType) {
            return if (position % 2 == 0) {
                ViewType.leftType
            } else {
                ViewType.rightType
            }
        } else {
            return 3
        }
    }
}