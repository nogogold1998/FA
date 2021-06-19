package gst.trainingcourse.advancedlesson9_congvc7.di

import gst.trainingcourse.advancedlesson9_congvc7.data.database.TransactionDao
import gst.trainingcourse.advancedlesson9_congvc7.data.database.TransactionDaoImpl

object Injector : InjectorContract() {

    override val transactionDao: TransactionDao by lazy {
        @Suppress("EXPERIMENTAL_API_USAGE")
        TransactionDaoImpl(application.contentResolver, converters)
    }
}
