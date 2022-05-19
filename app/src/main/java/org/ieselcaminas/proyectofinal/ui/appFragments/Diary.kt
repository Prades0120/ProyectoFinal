package org.ieselcaminas.proyectofinal.ui.appFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.databinding.FragmentDiaryBinding
import org.ieselcaminas.proyectofinal.model.recyclerView.Item
import org.ieselcaminas.proyectofinal.model.recyclerView.RecyclerViewAdapter

class Diary : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private var _user: FirebaseUser? = null
    private val user get() = _user!!
    private var array: ArrayList<Item> = ArrayList(0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _user = Firebase.auth.currentUser
        _binding = FragmentDiaryBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.intent?.let {
            array = it.getParcelableArrayListExtra<Item>("array") as ArrayList<Item>
        }

        val recView = binding.recView

        if (array.isEmpty()) {
            array.add(Item("","No Data"))
        }

        recView.setHasFixedSize(true)
        val adapter = RecyclerViewAdapter(array)
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

}