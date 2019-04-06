package com.chloeirrigation.chloe.Fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chloeirrigation.chloe.FieldActivity
import com.chloeirrigation.chloe.R
import com.chloeirrigation.chloe.Helpers.TAG
import com.chloeirrigation.chloe.Objects.Field
import kotlinx.android.synthetic.main.fragment_fields_list.*

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

        field1.findViewById<TextView>(R.id.fieldNameTextView).text =
            fields[0].name
        field2.findViewById<TextView>(R.id.fieldNameTextView).text =
            fields[1].name
        field3.findViewById<TextView>(R.id.fieldNameTextView).text =
            fields[2].name
        field4.findViewById<TextView>(R.id.fieldNameTextView).text =
            fields[3].name
        field5.findViewById<TextView>(R.id.fieldNameTextView).text =
            fields[4].name

        field1.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(
            context?.getDrawable(fields[0].fieldDrawable)
        )

        field2.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(
            context?.getDrawable(fields[1].fieldDrawable)
        )

        field3.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(
            context?.getDrawable(fields[2].fieldDrawable)
        )

        field4.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(
            context?.getDrawable(fields[3].fieldDrawable)
        )

        field5.findViewById<ImageView>(R.id.fieldImage).setImageDrawable(
            context?.getDrawable(fields[4].fieldDrawable)
        )
    }

    private fun setupFieldListeners() {
        //TODO: add the actions here
        field1.setOnClickListener {
            val intent = Intent(context, FieldActivity::class.java)
            intent.putExtra("field", fields[0])
            startActivity(intent)
        }

        field2.setOnClickListener {
            val intent = Intent(context, FieldActivity::class.java)
            intent.putExtra("field", fields[1])
            startActivity(intent)
        }
    }

    companion object {

        // Dummy data
        val fields = arrayListOf(
            Field("Field 1", 0, 0, "5ca79c64d86170003e090774", R.drawable.kopaida_1_field),
            Field("Field 2", 0, 0, "5ca79d53d86170001b09072c", R.drawable.kopaida_2_field),
            Field("Field 3", 0, 0, "5ca79d53d86170001b09072c", R.drawable.field_blur),
            Field("Field 4", 0, 0, "5ca79d53d86170001b09072c", R.drawable.field_blur),
            Field("Field 5", 0, 0, "5ca79d53d86170001b09072c", R.drawable.field_blur)
        )
        // TODO: 06/04/2019 Move these to a RecyclerView loaded form the server
    }
}
