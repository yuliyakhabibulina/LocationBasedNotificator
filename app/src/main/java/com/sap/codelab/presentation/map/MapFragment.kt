package com.sap.codelab.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.sap.codelab.databinding.FragmentMapBinding
import com.sap.codelab.utils.extensions.collectFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(){

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
//    = with(viewModel) {
//        collectFlow(navBackEvent) { shouldNavigateBack ->
//            if (shouldNavigateBack) {
//                findNavController().popBackStack()
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY_LOCATION = "location_request"
        const val BUNDLE_KEY_LOCATION = "selected_location"
        private val DEFAULT_LAT_LNG = LatLng(49.4, 8.6)
        private const val DEFAULT_ZOOM_LEVEL = 5f
    }
}