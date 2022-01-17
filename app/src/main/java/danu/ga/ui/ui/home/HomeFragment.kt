package danu.ga.ui.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import danu.ga.api.BodyItem
import danu.ga.api.KordinatResponse
import danu.ga.api.NetworkModule
import danu.ga.databinding.FragmentHomeBinding
import danu.ga.ui.ui.home.adapter.HomeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.progressBar2.visibility = View.VISIBLE
        getData()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //get data API
    private fun getData(){
        NetworkModule.servicesKordinat().getData().enqueue(object : Callback<KordinatResponse> {
            override fun onResponse(call: Call<KordinatResponse>, response: Response<KordinatResponse>) {
                if (response.isSuccessful) {
                    binding.progressBar2.visibility = View.GONE
                    val data = response.body()?.body
                    if (data?.size ?: 0 > 0) {
                        showData(data)
                    }
                }
            }

            override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                Log.d("error server", t.message!!)
            }

        })


    }
    //show data
    private fun showData(data: List<BodyItem?>?) {
        binding.listWisata.adapter = HomeAdapter(data, this.requireContext())
    }
}