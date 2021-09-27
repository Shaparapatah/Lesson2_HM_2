package com.shaparapatah.lesson2_hm_2.Contacts

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shaparapatah.lesson2_hm_2.databinding.FragmentContentProviderBinding
import com.shaparapatah.lesson2_hm_2.utils.REQUEST_CODE

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = ContentProviderFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Чтобы продолжить работу с приложением, требуется доступ к контактам")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            myRequestPermission()
                        }
                        .setNegativeButton("Не предоставлять доступ") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }

    private fun getContacts() {

    }

    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Вы не предоставили доступ к контактам, дальнейшая работа с приложением прекращенна")
                            .setNegativeButton("Закрыть") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}


