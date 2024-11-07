package skin.data

import kotlinx.serialization.Serializable

@Serializable
data class LegacySkinData(
    val textures: LegacySkin
) {
    @Serializable
    data class LegacySkin(
        val SKIN: LegacyUrl
    ) {
        @Serializable
        data class LegacyUrl(
            val url: String,
            val metadata: LegacyModel? = null
        ) {
            @Serializable
            data class LegacyModel(
                val model: String
            )
        }
    }
}
