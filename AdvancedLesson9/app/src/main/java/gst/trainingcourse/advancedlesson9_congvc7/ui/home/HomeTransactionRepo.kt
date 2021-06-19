package gst.trainingcourse.advancedlesson9_congvc7.ui.home

import gst.trainingcourse.advancedlesson9_congvc7.data.repo.TransactionRepo

interface HomeTransactionRepo : TransactionRepo.Queryable, TransactionRepo.Deletable
