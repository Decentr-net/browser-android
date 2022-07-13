package org.mozilla.fenix

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_domain.models.PDV
import net.decentr.module_decentr_domain.models.PDVHistory
import net.decentr.module_decentr_domain.usecases.pdv.*
import net.decentr.module_decentr_domain.usecases.signin.GetProfileFlowUseCase
import org.mozilla.fenix.ext.isKnownSearchDomain
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlinx.coroutines.flow.collect

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
                getProfileFlowUseCase().collect { profile ->
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
                        val smallerLists: List<List<PDV>> =
                            chopped(partitionByAddress.first.reversed(), MAX_PDV_COUNT)
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

    @SuppressLint("SimpleDateFormat")
    fun savePDV(url: String) {
        val domain = url.substringAfter("://").substringBefore("/")
        if (url.isKnownSearchDomain()
            && domain.substringBeforeLast('.').substringAfter('.').isNotEmpty()
            && (domain.isNotEmpty() && domain != "null")) {
            val query = url.substringAfter("$domain/").substringAfter("=").substringBefore("&")
            val pdv = PDVHistory(
                id = 0,
                address = address,
                type = PDV.PDVType.TYPE_HISTORY,
                query = query,
                engine = domain.substringBeforeLast('.').substringAfter('.'),
                domain = domain,
                timestamp = SimpleDateFormat(timespampPattern).apply { timeZone = TimeZone.getTimeZone("GMT") }.format(Date())
            )
            savePDV(listOf(pdv))
        }
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