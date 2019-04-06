package com.chloeirrigation.chloe


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_fields_list.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FieldsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fields_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: Done!")

        setupFieldNames()
        setupFieldListeners()
    }


    private fun setupFieldNames() {

        field1.findViewById<TextView>(R.id.fieldNameTextView).text = field1Name
        field2.findViewById<TextView>(R.id.fieldNameTextView).text = field2Name
        field3.findViewById<TextView>(R.id.fieldNameTextView).text = field3Name
        field4.findViewById<TextView>(R.id.fieldNameTextView).text = field4Name
        field5.findViewById<TextView>(R.id.fieldNameTextView).text = field5Name

        field1.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(context?.getDrawable(R.drawable.kopaida_1_field))
        field2.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(context?.getDrawable(R.drawable.kopaida_2_field))
    }

    private fun setupFieldListeners() {
        //TODO: add the actions here
        field1.setOnClickListener {
//            (activity as TempToolbarTitleListener).expandActionBar(false)
//
//            val action = FieldsListFragmentDirections.actionFieldsListFragmentToFieldFragment("Field1")
//            it.findNavController().navigate(action)

            val intent = Intent(context, FieldActivity::class.java)
            intent.putExtra("fieldName", field1Name)
            startActivity(intent)
        }

        field2.setOnClickListener {
//            (activity as TempToolbarTitleListener).expandActionBar(false)
//
//            val action = FieldsListFragmentDirections.actionFieldsListFragmentToFieldFragment("Field1")
//            it.findNavController().navigate(action)

            val intent = Intent(context, FieldActivity::class.java)
            intent.putExtra("fieldName", field2Name)
            startActivity(intent)
        }
    }

    companion object {
        const val field1Name = "Field 1"
        const val field2Name = "Field 2"
        const val field3Name = "Field 3"
        const val field4Name = "Field 4"
        const val field5Name = "Field 5"
    }
}
