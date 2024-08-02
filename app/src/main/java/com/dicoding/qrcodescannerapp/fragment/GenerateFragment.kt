package com.dicoding.qrcodescannerapp.fragment

import com.dicoding.qrcodescannerapp.generate.TextGenerateActivity
import android.content.*
import android.os.*
import android.view.*
import androidx.fragment.app.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.generate.*

class GenerateFragment : Fragment() {

    private var _binding: FragmentGenerateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnText.setOnClickListener {
                val intent = Intent(context, TextGenerateActivity::class.java)
                startActivity(intent)
            }
            btnWeb.setOnClickListener {
                val intent = Intent(context, WebsiteGenerateActivity::class.java)
                startActivity(intent)
            }
            btnWifi.setOnClickListener {
                val intent = Intent(context, WifiGenerateActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
