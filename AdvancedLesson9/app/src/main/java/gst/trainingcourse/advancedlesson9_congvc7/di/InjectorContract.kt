package gst.trainingcourse.advancedlesson9_congvc7.di

import android.app.Application
import gst.trainingcourse.advancedlesson9_congvc7.data.database.Converters
import gst.trainingcourse.advancedlesson9_congvc7.data.database.TransactionDao
import gst.trainingcourse.advancedlesson9_congvc7.data.repo.TransactionRepoImpl
import gst.trainingcourse.advancedlesson9_congvc7.ui.add.AddTransactionViewModel
import gst.trainingcourse.advancedlesson9_congvc7.ui.home.HomeViewModel

private lateinit var mApplication: Application

abstract class InjectorContract {
    protected val application: Application get() = mApplication

    abstract val transactionDao: TransactionDao

    private val transactionRepoImpl by lazy {
        TransactionRepoImpl(transactionDao)
    }

    val homeVMFactory by lazy {
        HomeViewModel.Factory(transactionRepoImpl)
    }

    val addTransactionVMFactory by lazy {
        AddTransactionViewModel.Factory(transactionRepoImpl)
    }

    val converters by lazy { Converters() }
}

fun Application.inject() {
    mApplication = this
}
