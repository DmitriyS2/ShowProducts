package com.sd.showproducts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sd.showproducts.dto.Category
import com.sd.showproducts.model.ModelCategories
import com.sd.showproducts.model.ModelProducts
import com.sd.showproducts.repository.Repository
import com.sd.showproducts.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _dataModel: MutableLiveData<ModelProducts> = MutableLiveData()
    val dataModel: LiveData<ModelProducts>
        get() = _dataModel

    private val _idForLoading: MutableLiveData<Int> = MutableLiveData(0)
    val idForLoading: LiveData<Int>
        get() = _idForLoading

    private val countForLoading: Int = 21
    val countForShowList: Int = 20

    private val _flagFilterSearch = MutableLiveData(State.ALL_OFF)
    val flagFilterSearch: LiveData<State>
        get() = _flagFilterSearch

    private val _dataCategory: MutableLiveData<ModelCategories> = MutableLiveData()
    val dataCategory: LiveData<ModelCategories>
        get() = _dataCategory

    private val _textCategory: MutableLiveData<String> = MutableLiveData("")
    val textCategory: LiveData<String>
        get() = _textCategory

    private var currentCategory = Category()

    init {
        loadAllCategories()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                _dataModel.value = ModelProducts(loading = true)
                _dataModel.value = ModelProducts(
                    products = repository.loadData(
                        countForLoading,
                        _idForLoading.value ?: 0
                    )
                )
                if (_dataModel.value?.products?.isEmpty() == true) {
                    _dataModel.value = ModelProducts(error = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _dataModel.value = ModelProducts(error = true)
            }
        }
    }

    fun addCountForLoading() {
        _idForLoading.value = _idForLoading.value?.plus(countForShowList)
    }

    fun removeCountForLoading() {
        _idForLoading.value = _idForLoading.value?.minus(countForShowList)
    }

    fun search(newText: String) {
        viewModelScope.launch {
            try {
                _dataModel.value = ModelProducts(loading = true)
                _dataModel.value = ModelProducts(products = repository.search(newText))
                if (_dataModel.value?.products?.isEmpty() == true) {
                    _dataModel.value = ModelProducts(error = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _dataModel.value = ModelProducts(error = true)
            }
        }
    }

    fun loadAllCategories() {
        viewModelScope.launch {
            try {
                _dataCategory.value = ModelCategories(loading = true)
                _dataCategory.value = ModelCategories(categories = repository.loadAllCategories())
                if (_dataCategory.value?.categories?.isEmpty() == true) {
                    _dataCategory.value = ModelCategories(error = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _dataCategory.value = ModelCategories(error = true)
            }
        }
    }

    fun loadCurrentCategory() {
        viewModelScope.launch {
            try {
                _dataModel.value = ModelProducts(loading = true)
                _dataModel.value = ModelProducts(products = repository.loadCurrentCategory(currentCategory.slug))
                if (_dataModel.value?.products?.isEmpty() == true) {
                    _dataModel.value = ModelProducts(error = true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _dataModel.value = ModelProducts(error = true)
            }
        }
    }

    fun changeTextCategory(name: String) {
        _textCategory.value = name
    }

    fun changeFlagFilterSearch(state: State) {
        _flagFilterSearch.value = state
    }

    fun changeCurrentCategory(category: Category) {
        currentCategory = category
    }

}