package net.decentr.module_decentr.domain.models

sealed class PDV(
    open val type: PDVType,
    open val address: String?
) {
    enum class PDVType(val value: String) {
        TYPE_HISTORY("searchHistory"),
        TYPE_PROFILE("profile")
    }
}

data class PDVHistory(
    override val type: PDVType,
    override val address: String?,
    val id: Int?,
    val domain: String,
    val engine: String,
    val query: String,
    val timestamp: String
): PDV(PDVType.TYPE_HISTORY, address)

data class PDVProfile(
    override val type: PDVType,
    override val address: String?,
    val avatar: String?,
    val bio: String?,
    val birthday: String?,
    val emails: List<String>?,
    val firstName: String,
    val lastName: String?,
    val gender: String?
): PDV(PDVType.TYPE_PROFILE, address)