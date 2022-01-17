package danu.ga.ui.ui.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import danu.ga.databinding.ItemNavigationWisataBinding


class NavigationWisataAdapter(val data: List<AdapterResponse>,val c:Context):RecyclerView.Adapter<NavigationWisataAdapter.NavigationWisataHandler>() {
    class NavigationWisataHandler(val binding:ItemNavigationWisataBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AdapterResponse?, c: Context,i: Int){
            binding.navNomor.text = i.toString()
            binding.navWisata.text = data?.nama
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationWisataHandler {
        val binding = ItemNavigationWisataBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NavigationWisataHandler(binding)
    }

    override fun onBindViewHolder(holder: NavigationWisataHandler, position: Int) {
        holder.bind(data?.get(position),c,position+1)
    }

    override fun getItemCount()=data?.size ?:0

}