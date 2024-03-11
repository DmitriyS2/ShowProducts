package com.sd.showproducts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sd.showproducts.model.ModelProducts
import com.sd.showproducts.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository:Repository
) :ViewModel() {

//    private val _data:MutableLiveData<List<Product>?> = MutableLiveData()
//    val data:LiveData<List<Product>?>
//        get() = _data

    private val _dataModel:MutableLiveData<ModelProducts> = MutableLiveData()
    val dataModel:LiveData<ModelProducts>
        get() = _dataModel

    private val _idForLoading:MutableLiveData<Int> = MutableLiveData(0)
    val idForLoading:LiveData<Int>
        get() = _idForLoading

    private val countForLoading:Int = 21
    val countForShowList:Int = 20

    private val _flagSearch:MutableLiveData<Boolean> = MutableLiveData(false)
    val flagSearch:LiveData<Boolean>
        get() = _flagSearch

    fun loadData() {
        viewModelScope.launch {
            try{
         //       _data.value = repository.loadData(countForLoading, _idForLoading.value ?:0)
                _dataModel.value = ModelProducts(loading = true)
                _dataModel.value = ModelProducts(products = repository.loadData(countForLoading, _idForLoading.value ?:0) ?: null)
                if(_dataModel.value?.products?.isEmpty()==true) {
                    _dataModel.value = ModelProducts(error = true)
                }
            } catch (e:Exception) {
            //    e.printStackTrace()
                Log.d("MyLog", "loadData catch e=$e")
              //  _data.value = emptyList()
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

    fun changeFlagSearch() {
        _flagSearch.value?.let {
            _flagSearch.value = !it
        }
    }

    fun search(newText: String) {
        viewModelScope.launch {
            try{
                _dataModel.value = ModelProducts(loading = true)
                _dataModel.value = ModelProducts(products = repository.search(newText))
                if(_dataModel.value?.products?.isEmpty()==true) {
                    _dataModel.value = ModelProducts(error = true)
                }
            //    _data.value = repository.search(newText)
            } catch (e:Exception) {
             //   e.printStackTrace()
                _dataModel.value = ModelProducts(error = true)
            }
        }
    }

    fun changeIdForLoading(number: Int) {
        _idForLoading.value = number
    }
}