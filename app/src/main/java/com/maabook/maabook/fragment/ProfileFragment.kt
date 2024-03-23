package com.maabook.maabook.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.shadow.ShadowRenderer
import com.maabook.maabook.R
import com.maabook.maabook.activity.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnLogOut: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preference_file_name),
            Context.MODE_PRIVATE)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Retrieve the data from the shared preferences
        val name = sharedPreferences.getString("name", "")
        val email = sharedPreferences.getString("email", "")
        val mobileNumber = sharedPreferences.getString("mobileNumber", "")
        val password = sharedPreferences.getString("password", "")

        // Set the data to the TextViews
        view.findViewById<TextView>(R.id.txt_name).text = name
        view.findViewById<TextView>(R.id.txt_email).text = email
        view.findViewById<TextView>(R.id.txt_mobileNum).text = mobileNumber
        view.findViewById<TextView>(R.id.txt_password).text = password

        btnLogOut = view.findViewById(R.id.btnLogOut)

        btnLogOut.setOnClickListener {
            clearSharedPreference()

            Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.putExtra("name", name.toString())
            intent.putExtra("email", email.toString())
            intent.putExtra("mobileNum", mobileNumber.toString())
            intent.putExtra("password", password.toString())
            startActivity(intent)
            activity?.finish()
            }

        btnDelete = view.findViewById(R.id.btnDelete)

        btnDelete.setOnClickListener {
            clearSharedPreference()

            Toast.makeText(requireContext(), "Account Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
            }

        return view
    }

    private fun clearSharedPreference() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}