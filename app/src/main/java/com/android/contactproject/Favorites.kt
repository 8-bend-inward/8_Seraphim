package com.android.contactproject

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.contactproject.contactlist.UserDataModel
import com.android.contactproject.databinding.FragmentFavoritesBinding

class Favorites : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentFragmentManager.setFragmentResultListener("ToFavoritesKey",this){key, result ->
           val lesserafim = result.getParcelableArrayList<UserDataModel>("ToFavorites")
            if(lesserafim !=null){
                Log.d("ContactProject","Favorites에서 받는 데이터: ${lesserafim}")
                UpdataFavorites(lesserafim)
            }
        }

        val searchView = binding.favoritesSearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.favoritesSearchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                filterList(newText)
                return true
            }
        })

        return binding.root
    }
    fun UpdataFavorites(lesserafim : ArrayList<UserDataModel>){
        binding.favoritesRecyclerview.apply {
            adapter = FavoritesAdapter(lesserafim).apply {
                itemClick = object : FavoritesAdapter.ItemClick {
                    override fun onFavoritesClick(view: View, position: Int) {
                        if (position in 0 until lesserafim.size) {
                            val item = lesserafim[position]

                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("즐겨찾기 해제")
                            builder.setMessage("즐겨찾기를 해제 하시겠읍니까?")

                            val listener = object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            if (item != null) {
                                                item.isLike = !item.isLike
                                                notifyDataSetChanged()
                                            }
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
                    }
                }
            }
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }
//    private fun fliterList(query : String?): Boolean{
//        if(query != null){
//
//        }
//    }
}
