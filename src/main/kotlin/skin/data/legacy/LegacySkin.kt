package skin.data.legacy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegacySkin(
    @SerialName("SKIN")
    val skin: LegacyUrl
)
