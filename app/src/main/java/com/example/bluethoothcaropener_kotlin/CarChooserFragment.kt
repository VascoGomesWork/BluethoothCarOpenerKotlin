package com.example.bluethoothcaropener_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluethoothcaropener_kotlin.databinding.CarAvailableItemBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CarChooserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarChooserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_car_chooser, container, false)
    }

    inner class CarsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = CarAvailableItemBinding.bind(view)
        //private lateinit var cars : Cars
        init {
            binding.root.setOnClickListener{
                println("Clicked")
            }
        }

        fun bind(car: String) {
            binding.carBrandTextView.text = "Seat"
            binding.carModelTextView.text = "Ibiza"
            //TODO - Thumbnail
            binding.carThumbnail.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    inner class CarsAdapter : RecyclerView.Adapter<CarsViewHolder>() {

        var carsList: MutableList<String> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
            val v: View = LayoutInflater.from(parent.context).inflate(R.layout.car_available_item, parent, false)
            return  CarsViewHolder(v)
        }

        override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
            val car = carsList[position]
            holder.bind(car)
        }

        override fun getItemCount() = carsList.size
    }
}