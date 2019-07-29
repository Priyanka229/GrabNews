package grab.com.news.advancedsearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import grab.com.news.*
import com.google.android.flexbox.FlexboxLayoutManager
import grab.com.news.databinding.AdvancedSearchBinding


class AdvancedSearchActivity: GrabNewsBaseActivity() {

    private val countryFlexboxLayoutManager = FlexboxLayoutManager(this@AdvancedSearchActivity)
    private val categoryFlexboxLayoutManager = FlexboxLayoutManager(this@AdvancedSearchActivity)
    private val viewModel by lazy { ViewModelProviders.of(this,
        GrabNewsVMFactory(GrabNewsApplication.instance,
            GrabNewsNavigator(this)
        )
    ).get(GrabNewsVM::class.java) }

    private val binding by lazy {
        AdvancedSearchBinding.inflate(LayoutInflater.from(this))
    }

    private var query: String? = null
    private var selectedCategory: String? = null
    private var selectedCountry: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val countryAdapter = CountryAdapter().apply {
            mCallback = object : CountryAdapter.CountryAdapterCallback {
                override fun onCountrySelected(country: String?) {
                    this@AdvancedSearchActivity.selectedCountry = country
                }
            }
        }

        val categoryAdapter = CategoryAdapter().apply {
            mCallback = object : CategoryAdapter.CategoryAdapterCallback {
                override fun onCategorySelected(category: String?) {
                    this@AdvancedSearchActivity.selectedCategory = category
                }
            }
        }


        binding.apply {
            countryRv.apply {
                layoutManager = countryFlexboxLayoutManager
                adapter = countryAdapter
            }

            categoryRv.apply {
                layoutManager = categoryFlexboxLayoutManager
                adapter = categoryAdapter
            }

            /** search view */
            advancedSearch.setOnClickListener {
                advancedSearch.setIconifiedByDefault(true)
                advancedSearch.isFocusable = true
                advancedSearch.isIconified = false
                advancedSearch.requestFocusFromTouch()
            }
            advancedSearch.apply {
                queryHint = "type here"
                setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        this@AdvancedSearchActivity.query = p0
                        return false
                    }
                })
            }

            /** submit button */
            submitBt.setOnClickListener {
                viewModel.openNewsListingActivity(query, selectedCountry, selectedCategory)
            }
        }

        /** countries live data listener */
        viewModel.countriesLiveData.observe(this, Observer {
            countryAdapter.countryList = it
            countryAdapter.notifyDataSetChanged()
        })



        /** categories live data listener */
        viewModel.categoriesLiveData.observe(this, Observer {
            categoryAdapter.categoryList = it
            categoryAdapter.notifyDataSetChanged()
        })

        viewModel.fetchCountries()
        viewModel.fetchCategories()
    }
}