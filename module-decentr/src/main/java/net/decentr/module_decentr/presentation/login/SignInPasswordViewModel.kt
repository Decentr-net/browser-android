package net.decentr.module_decentr.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_common.data.utils.etheriumkit.removeLeadingZeros
import net.decentr.module_decentr_common.data.utils.hdwalletkit.HDWallet
import net.decentr.module_decentr_common.data.utils.hdwalletkit.Mnemonic
import net.decentr.module_decentr_common.data.utils.hdwalletkit.UtilConverter
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_domain.usecases.signin.StoreKeysUseCase
import javax.inject.Inject

class SignInPasswordViewModel @Inject constructor(
    private val storeKeysUseCase: StoreKeysUseCase
): ViewModel() {

    private val _keysWithAddressLiveData by lazy { MutableLiveData<Result<Triple<String, String, String>>>() }
    val keysWithAddressLiveData: LiveData<Result<Triple<String, String, String>>> = _keysWithAddressLiveData

    fun getWalletFromSeed(seed: String) {
        viewModelScope.launch {
            _keysWithAddressLiveData.postValue(Result.Loading)
            val wallet = HDWallet(Mnemonic().toSeed(seed.split(" ")))
            val pubKey = wallet.hdPublicKey(0, 0, true)
            val privateKey = wallet.privateKey("m/44'/118'/0'/0/0")
            val dpAddress = UtilConverter.getAddress(pubKey.publicKey)
            val convertedPrivKey =
                UtilConverter.byteArrayToHexString(privateKey.privKeyBytes).removeLeadingZeros()
            val convertedPubKey = UtilConverter.byteArrayToHexString(pubKey.publicKey)

            storeKeys(convertedPrivKey, convertedPubKey)

            if (dpAddress.isNotEmpty() && convertedPrivKey.isNotEmpty() && convertedPubKey.isNotEmpty()) {
                _keysWithAddressLiveData.postValue(Result.Success(Triple(dpAddress, convertedPrivKey, convertedPubKey)))
            } else {
                _keysWithAddressLiveData.postValue(Result.Error(Exception("Can not generate keys from this seed phrase.")))
            }
        }
    }

    private fun storeKeys(privKey: String, pubKey: String) {
        viewModelScope.launch {
            try {
                storeKeysUseCase.invoke(privKey = privKey, pubKey = pubKey)
            } catch (e: Exception) {
                Log.e("StoreKeysError", e.message.toString())
            }
        }
    }

}