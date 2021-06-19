package gst.trainingcourse.advancedlesson9_congvc7.ui.binding

import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import gst.trainingcourse.advancedlesson9_congvc7.R
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.ui.home.TransactionAdapter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

private val formatter by lazy { SimpleDateFormat.getDateInstance() }

@BindingAdapter("dateTime")
fun TextView.dateBinding(date: Date?) {
    if (date == null) return
    this.text = formatter.format(date)
}

@BindingAdapter(CURRENCY)
fun TextView.balanceAmountBinding(currency: Currency?) {
    if (currency == null) return
    val newText = currency.name
    if (text.toString() != newText) text = newText
}

private const val BALANCE_AMOUNT = "balanceAmount"

@BindingAdapter(BALANCE_AMOUNT)
fun TextView.balanceAmount(value: Float?) {
    val newText = DecimalFormat("#,###.##").format(value)
    if (text.toString() != newText) text = newText
}

@BindingAdapter("transactionList", "transactionAdapter", requireAll = true)
fun RecyclerView.transactionsBinding(list: List<Transaction>?, adapter: TransactionAdapter?) {
    if(this.adapter != adapter) this.adapter = adapter
    adapter?.submitList(list)
}

private val transactionTypeMap: Map<Transaction.Type, Int> =
    Transaction.Type.values().associateWith {
        when (it) {
            Transaction.Type.SALE -> R.id.button_toggle_sale
            Transaction.Type.REFUND -> R.id.button_toggle_refund
        }
    }

private val reversedTypeMap: Map<Int, Transaction.Type> =
    transactionTypeMap.entries.associate { it.value to it.key }

@get:IdRes
private val Transaction.Type.buttonId: Int
    get() = transactionTypeMap[this]!!

@Suppress("NOTHING_TO_INLINE")
private inline fun Transaction.Type.Companion.valueOfButtonId(@IdRes id: Int): Transaction.Type? {
    return reversedTypeMap[id]
}

@Suppress("unused")
@BindingAdapter("error")
fun TextInputLayout.bindError(msg: String?) {
    error = msg
}

@BindingAdapter("error")
fun TextInputLayout.bindError(@StringRes strId: Int?) {
    error = when (strId) {
        null, Int.MIN_VALUE -> null
        else -> resources.getString(strId)
    }
}

// region Old implementation
// @BindingAdapter("transactionType")
// fun MaterialButtonToggleGroup.transactionTypeBinding(type: Transaction.Type?) {
//     if (type == null) return
//     val checkId = type.buttonId
//     if (checkedButtonId != checkId) check(checkId)
// }
//
// @InverseBindingAdapter(attribute = "transactionType")
// fun MaterialButtonToggleGroup.inverseTransactionTypeBinding(): Transaction.Type? {
//     return Transaction.Type.valueOfButtonId(checkedButtonId)
// }
// endregion

private const val TRANSACTION_TYPE = "transactionType"

@set:BindingAdapter(TRANSACTION_TYPE)
@get:InverseBindingAdapter(attribute = TRANSACTION_TYPE)
var MaterialButtonToggleGroup.transactionType: Transaction.Type?
    set(type) {
        if (type == null) return
        val checkId = type.buttonId
        if (checkedButtonId != checkId) check(checkId)
    }
    get() = Transaction.Type.valueOfButtonId(checkedButtonId)

@BindingAdapter("${TRANSACTION_TYPE}AttrChanged")
fun MaterialButtonToggleGroup.transactionTypeChangeListenerBinding(attrChange: InverseBindingListener?) {
    if (attrChange == null) return
    addOnButtonCheckedListener { _, _, isChecked ->
        if (isChecked) attrChange.onChange()
    }
}

private const val CURRENCY = "currency"

@Suppress("UNCHECKED_CAST")
@set:BindingAdapter(CURRENCY)
@get:InverseBindingAdapter(attribute = CURRENCY)
var MaterialAutoCompleteTextView.currency: Currency?
    set(value) {
        if (value == null) return
        if (adapter == null) {
            setAdapter(ArrayAdapter(context, R.layout.item_simple_text, Currency.values()))
        }
        if (text.toString() != value.name) setText(value.name, false)
    }
    get() = Currency.valueOf(text.toString())

@BindingAdapter("${CURRENCY}AttrChanged")
fun MaterialAutoCompleteTextView.currencyChangeListenerBinding(attrChange: InverseBindingListener?) {
    if (attrChange == null) return
    setOnItemClickListener { _, _, _, _ -> attrChange.onChange() }
}
