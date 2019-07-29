package grab.com.news.advancedsearch

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import grab.com.news.databinding.CategoryItemBinding

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var categoryList: List<String>? = null
    var mCallback: CategoryAdapterCallback? = null
    var selectedCategory: String? = null

    interface CategoryAdapterCallback {
        fun onCategorySelected(category: String?)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(p0.context)))
    }

    override fun getItemCount(): Int {
        return categoryList?.size ?: 0
    }

    override fun onBindViewHolder(p0: CategoryViewHolder, p1: Int) {
        categoryList?.get(p1)?.let {
            p0.bind(it)
        }
    }

    inner class CategoryViewHolder(private val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.apply {
                /** set up category name */
                categoryNameTv.text = item
                if (item == selectedCategory) {
                    categoryCv.setCardBackgroundColor(Color.GREEN)
                } else {
                    categoryCv.setCardBackgroundColor(Color.WHITE)
                }

                /** set callback */
                root.setOnClickListener {
                    if (item == selectedCategory) {
                        selectedCategory = null
                        mCallback?.onCategorySelected(null)
                    } else {
                        selectedCategory = item
                        mCallback?.onCategorySelected(item)
                    }

                    notifyDataSetChanged()
                }
            }
        }
    }
}