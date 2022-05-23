package org.ieselcaminas.proyectofinal.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.ActivityCreatePageBinding
import org.ieselcaminas.proyectofinal.model.recyclerView.Item

class CreatePage : AppCompatActivity() {

    private var _binding: ActivityCreatePageBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private var _user: FirebaseUser? = null
    private val user get() = _user!!
    private var clicked = false

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val translateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_right_anim) }
    private val translateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_right_anim) }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        _user = Firebase.auth.currentUser
        _binding = ActivityCreatePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fabDelete.visibility = View.INVISIBLE
        binding.fabSave.visibility = View.INVISIBLE
        binding.fabIgnore.visibility = View.INVISIBLE

        val text = intent.getStringExtra("text")
        val date = intent.getStringExtra("date")

        if (text!=null){
            binding.editTextTextMultiLine.text.append(text)
        }

        binding.floatingActionButton.setOnClickListener {
            onAddButtonClicked()
        }

        binding.fabIgnore.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.fabDelete.setOnClickListener {
            var array: ArrayList<Item>
            intent.let {
                array = it.getParcelableArrayListExtra<Item>("array") as ArrayList<Item>
            }
            if (date!=null) {
                val updates = hashMapOf<String,Any>(
                    date to FieldValue.delete()
                )
                Firebase.firestore.collection("docs").document(user.email!!).update(updates).addOnCompleteListener {
                    for (i in array) {
                        if (i.date == date) {
                            array.remove(i)
                        }
                    }
                    setResult(Activity.RESULT_OK,intent.putExtra("array",array))
                    finish()
                }
            } else {
                setResult(Activity.RESULT_CANCELED,intent.putExtra("array",array))
                finish()
            }
        }

        binding.fabSave.setOnClickListener {
            var array: ArrayList<Item>
            intent.let {
                array = it.getParcelableArrayListExtra<Item>("array") as ArrayList<Item>
            }
            val newDate = date ?: System.currentTimeMillis().toString()
            val textEdited = binding.editTextTextMultiLine.text.toString()
            val doc = hashMapOf<String,Any>(
                newDate to textEdited
            )
            Firebase.firestore.collection("docs").document(user.email!!).update(doc).addOnCompleteListener {
                var nuevo = true
                for (i in array) {
                    if (i.date == newDate) {
                        nuevo = false
                        array.remove(i)
                        array.add(Item(textEdited, newDate))
                    }
                }
                if (nuevo) {
                    array.add(Item(textEdited, newDate))
                }
                setResult(Activity.RESULT_OK,intent.putExtra("array",array))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.visibility = View.VISIBLE
            binding.fabIgnore.visibility = View.VISIBLE
            binding.fabDelete.visibility = View.VISIBLE
        } else {
            binding.fabSave.visibility = View.INVISIBLE
            binding.fabIgnore.visibility = View.INVISIBLE
            binding.fabDelete.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.startAnimation(translateOpen)
            binding.fabIgnore.startAnimation(translateOpen)
            binding.fabDelete.startAnimation(translateOpen)
            binding.floatingActionButton.startAnimation(rotateOpen)
        } else {
            binding.fabSave.startAnimation(translateClose)
            binding.fabIgnore.startAnimation(translateClose)
            binding.fabDelete.startAnimation(translateClose)
            binding.floatingActionButton.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.isClickable = true
            binding.fabIgnore.isClickable = true
            binding.fabDelete.isClickable = true
        } else {
            binding.fabSave.isClickable = false
            binding.fabIgnore.isClickable = false
            binding.fabDelete.isClickable = false
        }
    }
}