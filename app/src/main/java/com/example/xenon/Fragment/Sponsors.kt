package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.SponserAdapter
import com.example.xenon.DataClass.Sponser
import com.example.xenon.R
import com.example.xenon.databinding.FragmentSponsorsBinding
import com.google.firebase.firestore.FirebaseFirestore

class Sponsors : Fragment() {
    private lateinit var binding:FragmentSponsorsBinding
    private val spon:MutableList<Sponser> = mutableListOf()
    private lateinit var sponsAdapter: SponserAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSponsorsBinding.inflate(layoutInflater, container, false)
        binding.sponsRV.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sponsAdapter = SponserAdapter(spon)
        binding.sponsRV.adapter = sponsAdapter
        binding.sponsRV.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.menu.setOnClickListener {
            openDrawer()
        }
        fetchFromFirestore()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun fetchFromFirestore() {
        spon.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Sponser").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image")?:""
                val item = Sponser(name,image)
                spon.add(item)
            }
            sponsAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.sponsRV.visibility = View.VISIBLE

        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}