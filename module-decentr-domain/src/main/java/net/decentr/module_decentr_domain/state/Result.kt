package net.decentr.module_decentr_domain.state

sealed class Result<out R> {

    companion object {
        fun success(): Result<Unit> = Success(Unit)
    }

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

val Result<*>.isLoading
    get() = this is Result.Loading

val Result<*>.isError
    get() = this is Result.Error