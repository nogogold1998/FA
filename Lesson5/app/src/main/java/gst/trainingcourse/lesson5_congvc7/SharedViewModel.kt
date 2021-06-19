package gst.trainingcourse.lesson5_congvc7

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val cachedList = mutableListOf(
        Card("Nguyen Van A", "09/24", "9704 1234 2234 5678"),
        Card("Nguyen Van B", "09/24", "9704 1234 2234 5678"),
        Card("Nguyen Van C", "09/24", "9704 1234 2234 5678"),
        Card("Nguyen Van D", "09/24", "9704 1234 2234 5678"),
    )

    private val _cardList = MutableSharedFlow<List<Card>>(1).apply{
        viewModelScope.launch {
            emit(cachedList)
        }
    }

    private var _showingCardId = MutableSharedFlow<Int>()

    val cardList: Flow<List<Card>> = _cardList

    val showingCard: StateFlow<Card?> = MutableStateFlow<Card?>(null).apply {
        viewModelScope.launch {
            _cardList.combine(_showingCardId) { list: List<Card>, id: Int ->
                list.find { it.id == id }
            }.collect { value = it }
        }
    }

    private val _navigateToDetailsScreen = MutableStateFlow(false)
    val navigateToDetailsScreen: StateFlow<Boolean> = _navigateToDetailsScreen

    fun show(cardId: Int) {
        viewModelScope.launch {
            _showingCardId.emit(cardId)
            _navigateToDetailsScreen.value = true
        }
    }

    fun save(card: Card) {
        viewModelScope.launch {
            val index = cachedList.indexOfFirst { it.id == card.id }
            if (index > -1) {
                cachedList[index] = card
            } else {
                cachedList += card
            }
            _cardList.emit (cachedList)
        }
    }

    fun navigateToDetailScreenDone() {
        _navigateToDetailsScreen.value = false
    }
}
