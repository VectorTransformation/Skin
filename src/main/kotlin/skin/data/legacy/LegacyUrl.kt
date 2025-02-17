package skin.data.legacy

import kotlinx.serialization.Serializable

@Serializable
data class LegacyUrl(
    val url: String,
    val metadata: LegacyModel? = null
)