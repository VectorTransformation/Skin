package skin.data

import org.bukkit.profile.PlayerTextures
import java.net.URL

data class SkinData(
    val skin: URL,
    val model: PlayerTextures.SkinModel
)