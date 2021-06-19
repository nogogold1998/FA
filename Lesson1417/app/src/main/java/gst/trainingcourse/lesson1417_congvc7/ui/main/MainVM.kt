package gst.trainingcourse.lesson1417_congvc7.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.lesson1417_congvc7.data.model.CurrentWeatherEntry
import gst.trainingcourse.lesson1417_congvc7.data.source.remote.OpenWeatherApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DEBOUNCE_MILLIS = 300L

class MainVM(private val weatherApi: OpenWeatherApiService) : ViewModel() {
    class Factory @Inject constructor(
        private val weatherApi: OpenWeatherApiService
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainVM(weatherApi) as T
        }
    }

    val searchInput = MutableLiveData("")

    private val _currentWeatherEntry = MutableLiveData<CurrentWeatherEntry>()
    val currentWeatherEntry: LiveData<CurrentWeatherEntry> get() = _currentWeatherEntry

    private val disposable = CompositeDisposable()
    private val publishSearchInput = PublishSubject.create<String>()

    private val searchInputObserver = Observer<String> {
        publishSearchInput.onNext(it)
    }

    init {
        publishSearchInput
            .debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
            .filter(String::isNotBlank)
            .distinctUntilChanged()
            .switchMapSingle {
                weatherApi.fetchCurrentWeather(it).onErrorReturn { CurrentWeatherEntry(id = 404) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_currentWeatherEntry::postValue)
            .let(disposable::add)

        val flowa = flowOf("Chinh", "Cong")
        val flow = flow<String> {
            emit("Chinh")
            delay(2000L)
            emit("Cong")
        }
            .debounce(DEBOUNCE_MILLIS)
            .filter(String::isNotBlank)
            .distinctUntilChanged()
            .map {
                weatherApi.fetchCurrentWeatherSuspend(it)
            }
            .flowOn(Dispatchers.IO)
        viewModelScope.launch(Dispatchers.Main) {
            flow.collect { _currentWeatherEntry.value = it }
        }


        searchInput.observeForever(searchInputObserver)
    }

    override fun onCleared() {
        super.onCleared()
        searchInput.removeObserver(searchInputObserver)
        disposable.dispose()
    }
}
