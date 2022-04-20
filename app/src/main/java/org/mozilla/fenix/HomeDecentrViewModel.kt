package org.mozilla.fenix

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.decentr.module_decentr.domain.models.PDV
import net.decentr.module_decentr.domain.usecases.pdv.*
import net.decentr.module_decentr.domain.usecases.signin.GetProfileFlowUseCase
import javax.inject.Inject

class HomeDecentrViewModel @Inject constructor(
    private val savePDVUseCase: SavePDVUseCase,
    private val sendPDVUseCase: SendPDVUseCase,
    private val getAllPDVUseCase: GetAllPDVUseCase,
    private val removePDVUseCase: RemovePDVUseCase,
    private val checkUnicPDVUseCase: CheckUnicPDVUseCase,
    private val getProfileFlowUseCase: GetProfileFlowUseCase
) : ViewModel() {

    private val MAX_PDV_COUNT = 60
    private fun getPDVConfig(): Int {
        return MAX_PDV_COUNT
    }
    val timespampPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private var address: String? = null

    fun getAddress() = address

    fun init() {
        viewModelScope.launch {
            try {
                getProfileFlowUseCase.invoke().collect { profile ->
                    address = profile?.address
                }
            } catch (e: Exception) {
                //ignore
                Log.d("PROFILE FLOW", e.message.toString())
            }
        }
    }

    fun savePDV(pdv: List<PDV>) {
        viewModelScope.launch {
            try {
                if (!address.isNullOrEmpty() && checkUnicPDVUseCase(pdv.first())) {
                    val count = savePDVUseCase.invoke(pdv)
                    if (count >= getPDVConfig()) {
                        //todo should replace it to getPdvByAddress
                        val allPDV = getAllPDVUseCase.invoke()
                        val partitionByAddress = allPDV.partition {
                            it.type == PDV.PDVType.TYPE_HISTORY && (it.address == address || it.address.isNullOrEmpty())
                        }
                        val smallerLists: List<List<PDV>> = chopped(partitionByAddress.first.reversed(), MAX_PDV_COUNT)
                        val listToSend = smallerLists.first()
                        if (listToSend.isNotEmpty()) {
                            val result = sendPDVUseCase.invoke(listToSend)
                            if (result > 0) removePDVUseCase.invoke(listToSend)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DATABASE", e.message.toString())
            }
        }
    }

    fun savePDV(url: String) {
        Log.d("URL", url)
//        val domain = url.substringAfter("://").substringBefore("/")
//        val query = url.substringAfter("$domain/").substringAfter("=")
//        val pdv = PDVHistory(
//            address = address,
//            type = PDV.PDVType.TYPE_HISTORY,
//            query = query,
//            engine = domain.substringBeforeLast('.').substringAfter('.'),
//            domain = domain,
//            timestamp = SimpleDateFormat(timespampPattern).format(Date())
//        )
//        savePDV(listOf(pdv))
    }

    private fun <T> chopped(list: List<T>, L: Int): List<List<T>> {
        val parts: MutableList<List<T>> = ArrayList()
        val N = list.size
        var i = 0
        while (i < N) {
            parts.add(
                ArrayList(
                    list.subList(i, N.coerceAtMost(i + L))
                )
            )
            i += L
        }
        return parts
    }

}