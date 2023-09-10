package com.android.contactproject

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.contactproject.contactlist.UserDataModel
import com.android.contactproject.databinding.FragmentFavoritesBinding

class Favorites : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val lesserafimList = arrayListOf<UserDataModel>()
    private var sort_Lesserafim = arrayListOf<UserDataModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentFragmentManager.setFragmentResultListener("ToFavoritesKey", this) { key, result ->
            val lesserafim = result.getParcelableArrayList<UserDataModel>("ToFavorites")

            if (lesserafim != null) {
                sort_Lesserafim = ArrayList(lesserafim.sortedBy { it.name })
                binding.favoritesRecyclerview.layoutManager =
                    LinearLayoutManager(context)
                UpdataFavorites(lesserafim, FavoritesAdapter.listViewType)
                Log.d("ContactProjects", "Favorites에서 받는 데이터: ${lesserafim}")
                binding.favoritesSelect.setOnClickListener {
                    val menu = PopupMenu(context, it)
                    menu.menuInflater.inflate(R.menu.menu, menu.menu)
                    menu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.ListView -> {
                                binding.favoritesRecyclerview.layoutManager =
                                    LinearLayoutManager(context)
                                UpdataFavorites(lesserafim, FavoritesAdapter.listViewType)
                                true
                            }

                            R.id.GridView -> {
                                binding.favoritesRecyclerview.layoutManager =
                                    GridLayoutManager(context, 3)
                                UpdataFavorites(lesserafim, FavoritesAdapter.gridViewType)
                                true
                            }
                            R.id.Sort -> {
                                binding.favoritesRecyclerview.layoutManager =
                                    LinearLayoutManager(context)
                                UpdataFavorites(sort_Lesserafim,FavoritesAdapter.listViewType)
                                true
                            }
                            else -> false
                        }
                    }
                    menu.show()
                }

            }
        }


//        val searchView = binding.favoritesSearchView
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.favoritesSearchView.clearFocus()
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
////                filterList(newText)
//                return true
//            }
//        })

        return binding.root
    }

    fun UpdataFavorites(lesserafim: ArrayList<UserDataModel>, viewType: Int) {
        binding.favoritesRecyclerview.apply {
            adapter = FavoritesAdapter(lesserafim, viewType).apply {
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
                                                lesserafimList.clear()
                                                lesserafimList.add(lesserafim[position])
                                                lesserafim.removeAt(position)
                                                sort_Lesserafim = ArrayList(lesserafim.sortedBy { it.name })
                                                notifyItemRemoved(position)

                                                val bundle = Bundle()
                                                bundle.putParcelableArrayList(
                                                    "ToContactList",
                                                    lesserafimList
                                                )
                                                setFragmentResult("ToContactListKey", bundle)
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
            setHasFixedSize(true)
        }
    }
//    private fun fliterList(query : String?): Boolean{
//        if(query != null){
//
//        }
//    }
}
