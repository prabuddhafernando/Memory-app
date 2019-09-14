package com.example.gameapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gameapp.data.BoxModel
import com.example.gameapp.data.BoxStatus
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


open class BaseViewModel : ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

    var list: MutableLiveData<List<BoxModel>>? = MutableLiveData()
    var rounds: MutableLiveData<Int> = MutableLiveData()

    var selectedList: List<BoxModel> = ArrayList()
    var onEventChangesListner: OnEventChangesListner? = null

    private var orderedList: List<BoxModel>? = null

    private fun setDataList() {
        orderedList = listOf(
            BoxModel(BoxStatus.IDLE, 'a'),
            BoxModel(BoxStatus.IDLE, 'b'),
            BoxModel(BoxStatus.IDLE, 'c'),
            BoxModel(BoxStatus.IDLE, 'd'),
            BoxModel(BoxStatus.IDLE, 'e'),
            BoxModel(BoxStatus.IDLE, 'f'),
            BoxModel(BoxStatus.IDLE, 'g'),
            BoxModel(BoxStatus.IDLE, 'h'),
            BoxModel(BoxStatus.IDLE, 'a'),
            BoxModel(BoxStatus.IDLE, 'b'),
            BoxModel(BoxStatus.IDLE, 'c'),
            BoxModel(BoxStatus.IDLE, 'd'),
            BoxModel(BoxStatus.IDLE, 'e'),
            BoxModel(BoxStatus.IDLE, 'f'),
            BoxModel(BoxStatus.IDLE, 'g'),
            BoxModel(BoxStatus.IDLE, 'h')
        )
    }

    fun initDataList() {
        rounds.value = 0
        (selectedList as ArrayList).clear()
        setDataList()
        orderedList!!.shuffled().let {
            list!!.postValue(it)
        }
    }


    fun onItemSelected(boxModel: BoxModel) {
        onEventChangesListner?.setProgress(true)
        if (boxModel.Status == BoxStatus.ACTIVE) {
            (selectedList as ArrayList).add(boxModel)
            if (selectedList.size == 2) { // processing state
                rounds.value!!.inc().let {
                    rounds.value = it
                }
                if (selectedList[0].letter == selectedList[1].letter) {
                    onEventChangesListner?.showMessages("Well done!...", false)
                    // update matched status on list items..
                    list!!.value!!.forEach {
                        if (it.letter == selectedList[0].letter) {
                            it.Status = BoxStatus.MATCHED
                        }
                    }
                    reloadList()
                } else {
                    list!!.value!!.forEach {
                        if (it.Status == BoxStatus.ACTIVE) {
                            it.Status = BoxStatus.IDLE
                        }
                    }.let {
                        reloadList()
                    }
                    onEventChangesListner?.showMessages("No matched! try again.", true)
                }
                // reset selected memory
                (selectedList as ArrayList).clear()
                isAllCleared().let {
                    if (it) {
                        onEventChangesListner?.showGameVinMessge(rounds.value!!)
                    }
                }
            }
        }
        onEventChangesListner?.setProgress(false) // here progress also we can use observer pattern. But I used interface for time.
        rounds.postValue(rounds.value!!)

//        Log.e(TAG, list!!.value!!.subList(0, 4).toString())
//        Log.e(TAG, list!!.value!!.subList(4, 8).toString())
//        Log.e(TAG, list!!.value!!.subList(8, 12).toString())
//        Log.e(TAG, list!!.value!!.subList(12, 16).toString())
    }

    private fun reloadList(): Disposable? {
        return Flowable.timer(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                list!!.postValue(list!!.value)
            }
    }

    /**
     * returns true if all the items MATCHED {WIN} status
     * */
    private fun isAllCleared(): Boolean {
        list?.value!!.forEach {
            if (it.Status == BoxStatus.IDLE || it.Status == BoxStatus.ACTIVE) {
                return false
            }
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        if (list != null)
            (list!!.value as ArrayList).clear()
    }

    interface OnEventChangesListner {
        fun showMessages(msg: String, isVibrate: Boolean)
        fun showGameVinMessge(rounds: Int)
        fun setProgress(isEnable: Boolean)
    }

}