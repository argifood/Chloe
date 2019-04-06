package com.chloeirrigation.chloe.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.chloeirrigation.chloe.R
import com.chloeirrigation.chloe.TempToolbarTitleListener
import kotlinx.android.synthetic.main.fragment_field.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FieldContainerFragment : Fragment() {

    val args: FieldContainerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_field, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = args.fieldName
        (activity as TempToolbarTitleListener).updateTitle(title)

//        bottom_navigation.setupWithNavController()

//        setupWithNavController(bottom_navigation, view.findNavController())
        bottom_navigation.setupWithNavController(view.findNavController())
    }

//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_info -> {
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_calendar -> {
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_stats -> {
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }


}
