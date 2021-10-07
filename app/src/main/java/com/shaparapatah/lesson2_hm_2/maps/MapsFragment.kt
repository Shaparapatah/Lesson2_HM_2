package com.shaparapatah.lesson2_hm_2.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.shaparapatah.lesson2_hm_2.R
import com.shaparapatah.lesson2_hm_2.databinding.FragmentGoogleMapsMainBinding
import com.shaparapatah.lesson2_hm_2.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {

    private val binding: FragmentGoogleMapsMainBinding by viewBinding(CreateMethod.INFLATE)

    companion object {
        fun newInstance() = MapsFragment()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val moscow = LatLng(55.0, 37.0)
        googleMap.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}