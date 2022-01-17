package danu.ga.ui.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import danu.ga.api.BodyItem
import danu.ga.databinding.ItemHomeWisataBinding

class HomeAdapter(val data:List<BodyItem?>?, var c:Context): RecyclerView.Adapter<HomeAdapter.HomeAdapterAdapter>() {
    class HomeAdapterAdapter(var binding: ItemHomeWisataBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BodyItem?,c: Context){
            binding.textView2.text = data?.nama
            Glide.with(c).load(data?.img).into(binding.imageView3)
            binding.textView3.text = "Buka : ${data?.waktuBuka}"
            binding.textView4.text = "Tutup : ${data?.waktuTutup}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterAdapter {
        val binding = ItemHomeWisataBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeAdapterAdapter(binding)
    }

    override fun onBindViewHolder(holder: HomeAdapterAdapter, position: Int) {
        holder.bind(data?.get(position),c)
    }

    override fun getItemCount()=data?.size ?:0

}


