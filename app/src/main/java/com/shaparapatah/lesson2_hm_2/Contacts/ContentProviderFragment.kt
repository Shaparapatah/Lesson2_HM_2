package com.shaparapatah.lesson2_hm_2.Contacts

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaparapatah.lesson2_hm_2.databinding.FragmentContentProviderBinding
import com.shaparapatah.lesson2_hm_2.utils.REQUEST_CODE

class ContentProviderFragment : Fragment() {

    private val binding: FragmentContentProviderBinding by viewBinding(CreateMethod.INFLATE)

    /* private var _binding: FragmentContentProviderBinding? = null
     private val binding get() = _binding!!


     */

    companion object {
        fun newInstance() = ContentProviderFragment()
        const val RQ_CONTACTS = 232
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }


    /* override fun onDestroy() {
         super.onDestroy()
         _binding = null
     }

     */

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                //    getContacts()
                    getContactByNumber()
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

   /* private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver
            val cursor: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC" //
            )

            cursor?.let { cursor ->
                for (i in 0 until cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        binding.containerForContacts.addView(TextView(it).apply {
                            text = name
                            textSize = 30f
                        })
                    }
                }
            }
        }

    }

    */

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
                   // getContacts()
                    getContactByNumber()
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




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RQ_CONTACTS) {
                data?.let { contactPicked(it) }
            }
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun getContactByNumber() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.setDataAndType(ContactsContract.Contacts.CONTENT_URI, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, RQ_CONTACTS)
    }

    private fun contactPicked(data: Intent) {
        var cursor: Cursor? = null
        try {
            val uri = data.data
            cursor = uri?.let { requireActivity().contentResolver.query(it, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER) , null, null, null) }
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val phoneNo: String? =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    Toast.makeText(context, "Выбран контакт $phoneNo", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }


}


