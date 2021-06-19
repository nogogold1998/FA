package gst.trainingcourse.lesson9_ex1_congvc7.di

import gst.trainingcourse.lesson9_ex1_congvc7.MyApplication
import gst.trainingcourse.lesson9_ex1_congvc7.data.ContactRepository
import gst.trainingcourse.lesson9_ex1_congvc7.data.ContactRepositoryImpl
import gst.trainingcourse.lesson9_ex1_congvc7.ui.ContactsFragmentViewModel

private lateinit var mApplication: MyApplication
private val mContactsFragmentViewModelFactory: ContactsFragmentViewModel.Factory by lazy {
    ContactsFragmentViewModel.Factory(Injector.application, Injector.contactRepo)
}

object Injector {
    val application: MyApplication get() = mApplication
    val contactRepo: ContactRepository by lazy { ContactRepositoryImpl() }
}

fun MyApplication.inject() {
    mApplication = this
}

val ContactsFragmentViewModel.Companion.factory: ContactsFragmentViewModel.Factory
    get() = mContactsFragmentViewModelFactory
