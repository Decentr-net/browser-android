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
import net.decentr.module_decentr_domain.errors.ErrorHandler
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_domain.usecases.register.TrackInstallUseCase
import net.decentr.module_decentr_domain.usecases.signin.*
import javax.inject.Inject

class SignInSeedViewModel @Inject constructor(
    private val generageMnemonicPhraseUseCase: GenerateMnemonicPhraseUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val checkAddressInBlockchainUseCase: CheckAddressInBlockchainUseCase,
    private val trackInstallUseCase: TrackInstallUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val storeKeysUseCase: StoreKeysUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _mnemonicLiveData by lazy { MutableLiveData<Result<String>>() }
    val mnemonicLiveData: LiveData<Result<String>> = _mnemonicLiveData

    private val _profileLiveData by lazy { MutableLiveData<Result<Profile>>() }
    val profileLiveData: LiveData<Result<Profile>> = _profileLiveData

    private val _setProfileLiveData by lazy { MutableLiveData<String>() }
    val setProfileLiveData: LiveData<String> = _setProfileLiveData

    private val _keysWithAddressLiveData by lazy { MutableLiveData<Result<Triple<String, String, String>>>() }
    val keysWithAddressLiveData: LiveData<Result<Triple<String, String, String>>> =
        _keysWithAddressLiveData

    fun generateMnemonic() {
        viewModelScope.launch {
            _mnemonicLiveData.postValue(Result.Loading)
            try {
                val result = generageMnemonicPhraseUseCase.invoke()
                _mnemonicLiveData.postValue(
                    Result.Success(result)
                )
            } catch (e: Exception) {
                _mnemonicLiveData.postValue(Result.Error(e))
            }
        }
    }

    private fun getProfile(address: String?, privKey: String, pubKey: String) {
        viewModelScope.launch {
            _profileLiveData.postValue(Result.Loading)
            try {
                val result = getProfileUseCase.invoke(address)
                if (result != null) {
                    storeKeys(privKey, pubKey)
                    saveProfileUseCase.invoke(result)
                    _profileLiveData.postValue(Result.Success(result))
                } else {
                    _profileLiveData.postValue(Result.Error(Exception("No user found")))
                }
            } catch (e: Exception) {
                when (e) {
                    is NoSuchElementException -> {
                        if (address.isNullOrEmpty()) {
                            _profileLiveData.postValue(Result.Error(errorHandler.invoke(e)))
                        } else {
                            storeKeys(privKey, pubKey)
                            _setProfileLiveData.postValue(address)
                        }
                    }
                    else -> _profileLiveData.postValue(Result.Error(errorHandler.invoke(e)))
                }
            }
            try {
                address?.let { trackInstallUseCase.invoke(it) }
            } catch (e: Exception) {
                //ignore
            }
        }
    }

    fun getWalletFromSeed(seed: String) {
        viewModelScope.launch {
            try {
                val wallet = HDWallet(Mnemonic().toSeed(seed.split(" ")))
                val pubKey = wallet.hdPublicKey(0, 0, true)
                val privateKey = wallet.privateKey("m/44'/118'/0'/0/0")
                val dpAddress = UtilConverter.getAddress(pubKey.publicKey)
                val convertedPrivKey =
                    UtilConverter.byteArrayToHexString(privateKey.privKeyBytes).removeLeadingZeros()
                val convertedPubKey = UtilConverter.byteArrayToHexString(pubKey.publicKey)

                if (checkAddressInBlockchainUseCase(dpAddress))
                    getProfile(
                        dpAddress,
                        convertedPrivKey,
                        convertedPubKey
                    )
            } catch (e: Exception) {
                _profileLiveData.postValue(Result.Error(errorHandler.invoke(e, "Seed phrase is invalid.")))
            }
        }
    }

    fun createWalletFromSeed(seed: String) {
        viewModelScope.launch {
            val wallet = HDWallet(Mnemonic().toSeed(seed.split(" ")))
            val pubKey = wallet.hdPublicKey(0, 0, true)
            val privateKey = wallet.privateKey("m/44'/118'/0'/0/0")
            val dpAddress = UtilConverter.getAddress(pubKey.publicKey)
            val convertedPrivKey =
                UtilConverter.byteArrayToHexString(privateKey.privKeyBytes).removeLeadingZeros()
            val convertedPubKey = UtilConverter.byteArrayToHexString(pubKey.publicKey)

            if (dpAddress.isNotEmpty() && convertedPrivKey.isNotEmpty() && convertedPubKey.isNotEmpty()) {
                storeKeys(convertedPrivKey, convertedPubKey)
                _keysWithAddressLiveData.postValue(
                    Result.Success(
                        Triple(
                            dpAddress,
                            convertedPrivKey,
                            convertedPubKey
                        )
                    )
                )
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