package grab.com.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import grab.com.news.database.ArticleEntity
import grab.com.news.databinding.ArticleBinding

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    var articleList: List<ArticleEntity>? = null
    var mCallback: ArticleAdapterCallback? = null

    interface ArticleAdapterCallback {
        fun onItemTap(url: String?)
    }

    override fun getItemCount(): Int {
        return articleList?.size ?: 0
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArticleViewHolder {
        return ArticleViewHolder(ArticleBinding.inflate(LayoutInflater.from(p0.context)))
    }

    override fun onBindViewHolder(p0: ArticleViewHolder, p1: Int) {
        p0.bind(articleList!![p1])
    }

    inner class ArticleViewHolder(val binding: ArticleBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.layoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        fun bind(item: ArticleEntity) {
            binding.apply {
                item.apply {

                    /** set image */
                    urlToImage?.let {
                        Glide.with(itemView.context).load(it).placeholder(R.drawable.placeholder).into(imageIv)
                    }
                    /** set title */
                    title?.let {
                        titleTv.text =  it
                    }

                    /** set description */
                    val descriptionText = if (item.description.isNullOrBlank().not()) item.description else item.content
                    descriptionText?.let {
                        descriptionTv.text = it
                    }

                    /** set click listener */
                    root.setOnClickListener {
                        mCallback?.onItemTap(url)
                    }
                }
            }
        }
    }
}