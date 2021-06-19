package gst.trainingcourse.lesson9_ex1_congvc7.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.lesson9_ex1_congvc7.data.Contact
import gst.trainingcourse.lesson9_ex1_congvc7.data.ContactRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class ContactsFragmentViewModel(
    application: Application,
    private val contactRepository: ContactRepository,
) : AndroidViewModel(application) {

    class Factory(
        private val application: Application,
        private val contactRepository: ContactRepository,
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ContactsFragmentViewModel(application, contactRepository) as T
        }
    }

    private val _contacts = MutableStateFlow(emptyList<Contact>())
    val contacts: StateFlow<List<Contact>> = _contacts

    private var fetchJob: Job? = null

    fun fetchContacts() {
        synchronized(this) {
            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                val flow =
                    contactRepository.getAll(getApplication<Application>().contentResolver)
                _contacts.emitAll(flow)
            }
        }
    }

    companion object
}
