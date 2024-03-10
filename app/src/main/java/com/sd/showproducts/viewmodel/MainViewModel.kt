package com.sd.showproducts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sd.showproducts.dto.Product
import com.sd.showproducts.repository.Repository
import com.sd.showproducts.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository:Repository
) :ViewModel() {



    private val _data:MutableLiveData<List<Product>> = MutableLiveData()
    val data:LiveData<List<Product>>
        get() = _data

    private val _idForLoading:MutableLiveData<Int> = MutableLiveData(0)
    val idForLoading:LiveData<Int>
        get() = _idForLoading

    private val countForLoading:Int = 21
    val countForShowList:Int = 20

    fun loadData() {
        viewModelScope.launch {
            try{
                _data.value = repository.loadData(countForLoading, _idForLoading.value ?:0)
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    fun goToProduct(product: Product) {

    }

    fun addCountForLoading() {
        _idForLoading.value = _idForLoading.value?.plus(countForShowList)
    }

    fun removeCountForLoading() {
        _idForLoading.value = _idForLoading.value?.minus(countForShowList)
    }
}