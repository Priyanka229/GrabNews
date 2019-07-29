package grab.com.news.advancedsearch

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import grab.com.news.Country
import grab.com.news.databinding.CountryItemBinding

class CountryAdapter: RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    var countryList: List<Country>? = null
    var mCallback: CountryAdapterCallback? = null
    var selectedCountry: Country? = null

    interface CountryAdapterCallback {
        fun onCountrySelected(country: String?)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CountryViewHolder {
        return CountryViewHolder(CountryItemBinding.inflate(LayoutInflater.from(p0.context)))
    }

    override fun getItemCount(): Int {
        return countryList?.size ?: 0
    }

    override fun onBindViewHolder(p0: CountryViewHolder, p1: Int) {
        countryList?.get(p1)?.let {
            p0.bind(it)
        }
    }

    inner class CountryViewHolder(private val binding: CountryItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Country) {
            binding.apply {
                /** set up country name */
                countryNameTv.text = item.countryName
                if (item == selectedCountry) {
                    countryCv.setCardBackgroundColor(Color.GREEN)
                } else {
                    countryCv.setCardBackgroundColor(Color.WHITE)
                }

                /** set callback */
                binding.root.setOnClickListener {
                    if (item == selectedCountry) {
                        selectedCountry = null
                        mCallback?.onCountrySelected(null)
                    } else {
                        selectedCountry = item
                        mCallback?.onCountrySelected(item.countryCode)
                    }

                    notifyDataSetChanged()
                }
            }
        }
    }
}