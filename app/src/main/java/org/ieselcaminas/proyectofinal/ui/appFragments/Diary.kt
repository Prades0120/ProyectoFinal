package org.ieselcaminas.proyectofinal.ui.appFragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.databinding.FragmentDiaryBinding
import org.ieselcaminas.proyectofinal.model.recyclerView.Item
import org.ieselcaminas.proyectofinal.model.recyclerView.RecyclerViewAdapter
import org.ieselcaminas.proyectofinal.ui.CreatePage

class Diary : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private var _user: FirebaseUser? = null
    private val user get() = _user!!
    private var array: ArrayList<Item> = ArrayList(0)
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var i: Intent

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
        val intent = activity?.intent
        if (intent!=null) {
            intent.let {
                array = it.getParcelableArrayListExtra<Item>("array") as ArrayList<Item>
            }
            i = Intent(context, CreatePage::class.java)
            i.putExtra("array", intent.getSerializableExtra("array"))
            val getResult =
                registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    if (it.resultCode == Activity.RESULT_OK) {
                        val value = it.data?.getSerializableExtra("array")
                        intent.putExtra("array", value)
                    }
                }
            this.getResult = getResult
        }
    }

    override fun onResume() {
        recViewConstruction()
        super.onResume()
    }

    private fun recViewConstruction() {
        val recView = binding.recView

        val item = Item("","No Data")
        if (array.isEmpty()) {
            array.add(item)
        } else {
            for (i in array) {
                if (i.date == "No Data") {
                    array.remove(i)
                }
            }
        }

        recView.setHasFixedSize(true)
        val adapter = RecyclerViewAdapter(array)
        adapter.onClick = {
            i.putExtra("text",array[recView.getChildAdapterPosition(it)].text)
            i.putExtra("date",array[recView.getChildAdapterPosition(it)].date)
            getResult.launch(i)
        }
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }
}