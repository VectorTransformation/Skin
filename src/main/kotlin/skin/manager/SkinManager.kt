package skin.manager

import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import org.bukkit.profile.PlayerTextures
import skin.data.SkinData
import skin.data.YmlData
import skin.data.legacy.LegacySkinData
import skin.enum.Resource
import skin.extension.base64ToString
import skin.extension.child
import skin.extension.forEach
import skin.extension.format
import java.io.File
import java.net.URI
import java.util.*

object SkinManager {
    private val cacheDataMap = HashMap<UUID, SkinData>()
    private val skinDataMap = HashMap<String, SkinData>()

    fun reload() {
        dataReload()
        cacheReload()
        skinReload()
    }

    private fun dataReload() {
        Resource.ABYSS.make()
        Resource.CACHE.make()
        Resource.SKIN.make()
    }

    private fun cacheReload() {
        cacheDataMap.clear()
        Resource.CACHE.make().forEach("yml") { yml ->
            runCatching {
                val name = yml.nameWithoutExtension
                val skin = URI.create(YmlData.string("skin", "", yml)).toURL()
                val model = PlayerTextures.SkinModel.valueOf(YmlData.string("model", "", yml).uppercase())
                val uuid = UUID.fromString(name)
                cacheDataMap[uuid] = SkinData(
                    skin,
                    model
                )
            }
        }
    }

    fun skinReload() {
        skinDataMap.clear()
        Resource.SKIN.make().forEach("yml") { yml ->
            runCatching {
                val name = yml.nameWithoutExtension
                if (isLegacySkin(yml)) {
                    legacySkinData(yml, name)
                } else {
                    val skin = URI.create(YmlData.string("skin", "", yml)).toURL()
                    val model = PlayerTextures.SkinModel.valueOf(YmlData.string("model", "", yml).uppercase())
                    skinDataMap[name] = SkinData(
                        skin,
                        model
                    )
                }
            }
        }
    }

    fun isLegacySkin(file: File): Boolean {
        return YmlData.string("texture", "", file).isNotEmpty()
    }

    fun legacySkinData(file: File, name: String) {
        val base64 = YmlData.string("texture", "", file).base64ToString()
        val legacySkinData = Json.format().decodeFromString<LegacySkinData>(base64)
        legacySkinSave(legacySkinData, name)
    }

    fun legacySkinSave(legacySkinData: LegacySkinData, name: String) {
        SkinData(
            URI.create(legacySkinData.textures.skin.url).toURL(),
            PlayerTextures.SkinModel.valueOf(
                legacySkinData.textures.skin.metadata?.model?.uppercase() ?: PlayerTextures.SkinModel.CLASSIC.name
            )
        ).apply {
            skinDataMap[name] = this
            skinSave(this, name, true)
        }
    }

    fun cacheDataJoin(player: Player) {
        cacheDataMap[player.uniqueId] = skinData(player).apply {
            cacheSave(player, this)
        }
    }

    fun cacheDataQuit(player: Player) {
        cacheDataMap.remove(player.uniqueId)
    }

    fun cacheSave(player: Player, skinData: SkinData) {
        Resource.CACHE.make().child(player.uniqueId.toString().plus(".yml")).apply {
            YmlData.saveSkin(this, skinData)
        }
    }

    fun skinData(player: Player): SkinData {
        val textures = player.playerProfile.textures
        return SkinData(textures.skin!!, textures.skinModel)
    }

    fun skinSet(player: Player, skinData: SkinData) {
        with(player) {
            val profile = playerProfile
            val textures = profile.textures
            if (textures.skin == skinData.skin && textures.skinModel == skinData.model) {
                return
            }
            playerProfile = profile.apply {
                setTextures(textures.apply {
                    setSkin(
                        skinData.skin,
                        skinData.model
                    )
                })
            }
        }
    }

    fun skinSave(skinData: SkinData, name: String, isLegacy: Boolean = false) {
        val yml = name.plus(".yml")
        Resource.SKIN.make().child(yml).apply {
            if (exists()) {
                copyTo(Resource.ABYSS.make().child(yml), true)
            }
            YmlData.saveSkin(this, skinData, isLegacy)
        }
    }

    fun reset(player: Player) {
        cacheDataMap[player.uniqueId]?.apply {
            skinSet(player, this)
        }
    }

    fun save(player: Player, name: String) {
        skinData(player).apply {
            skinDataMap[name] = this
            skinSave(this, name)
        }
    }

    fun set(player: Player, name: String) {
        skinDataMap[name]?.apply {
            skinSet(player, this)
        }
    }

    fun skinList(): List<String> {
        return skinDataMap.keys.toList()
    }
}