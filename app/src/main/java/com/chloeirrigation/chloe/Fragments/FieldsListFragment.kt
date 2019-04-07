package com.chloeirrigation.chloe.Fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.chloeirrigation.chloe.FieldActivity
import com.chloeirrigation.chloe.Objects.Field
import com.chloeirrigation.chloe.R
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

        val nextIrrigation = context?.getString(R.string.next_irrigation)
        field1.findViewById<TextView>(R.id.nextIrrigationTextView).text = nextIrrigation + "\n in 2 days"
        field2.findViewById<TextView>(R.id.nextIrrigationTextView).text = nextIrrigation + "\n in 3 days"
        field3.findViewById<TextView>(R.id.nextIrrigationTextView).text = nextIrrigation + "\n in 2 days"
        field4.findViewById<TextView>(R.id.nextIrrigationTextView).text = nextIrrigation + "\n in 5 days"
        field5.findViewById<TextView>(R.id.nextIrrigationTextView).text = nextIrrigation + "\n in 4 days"
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
            Field("5bc4ff031023c1b16a71554c","Kopaida Wheat Field", 38.498389, 23.230694, "5ca79c64d86170003e090774", R.drawable.kopaida_1_field, 3, 30, 27, 2.0),
            Field("5c48808317c477201a386dad","Kopaida Lettuce Field", 38.508818, 23.223839, "5ca79d53d86170001b09072c", R.drawable.kopaida_2_field, 1,29, 35, 1.3),
            Field("","Anthili Field 1", 0.0, 0.0, "5ca79d53d86170001b09072c", R.drawable.anthili_1_field, 0, 22, 30, 1.2),
            Field("","Anthili Field 2", 0.0, 0.0, "5ca79d53d86170001b09072c", R.drawable.anthili_2_field, 0, 22, 30, 1.2),
            Field("","Anthili Field 3", 0.0, 0.0, "5ca79d53d86170001b09072c", R.drawable.kopaida_1_field, 0, 22, 30, 1.2)
        )
        // TODO: 06/04/2019 Move these to a RecyclerView loaded form the server
    }
}
