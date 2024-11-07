package skin.system

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.stream.consumeAsFlow
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.profile.PlayerTextures
import skin.data.FileData
import skin.data.LegacySkinData
import skin.data.SkinData
import skin.data.YmlData
import skin.extension.base64ToString
import skin.extension.decimalFormat
import skin.extension.format
import skin.extension.skinName
import skin.extension.skinParentName
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.system.measureTimeMillis

object SkinSystem {
    enum class ReloadState {
        NONE,
        RELOAD
    }

    var reloadState = ReloadState.NONE

    private val cacheDataMap = ConcurrentHashMap<UUID, SkinData>()
    private val skinDataMap = ConcurrentHashMap<String, SkinData>()

    fun reloader(sender: Entity? = null) {
        if (reloadState == ReloadState.RELOAD) {
            return
        }

        measureTimeMillis {
            reloadState = ReloadState.RELOAD

            cacheReload()

            skinReload()

            reloadState = ReloadState.NONE
        }.run {
            val info = "${skinDataMap.size} Skin File Reload Completed (${decimalFormat()} ms)"
            Cardinal.info(Component.text(
                info
            ))
            if (sender is Player) {
                sender.sendMessage(Component.text(
                    "[${Cardinal.ADMINISTRATOR}] $info"
                ))
            }
        }
    }

    fun cacheReload() {
        runBlocking {
            Files.walk(
                Paths.get(
                    FileData.dataFolderPath,
                    FileData.cache
                ).apply {
                    cacheDataMap.clear()
                }
            ).consumeAsFlow().filter {
                it.name.endsWith(".yml")
            }.collect { path ->
                launch(Dispatchers.IO) {
                    try {
                        val yml = path.toFile()
                        val skin = URI.create(YmlData.string("skin", "", yml)).toURL()
                        val model = PlayerTextures.SkinModel.valueOf(YmlData.string("model", "", yml).uppercase())
                        val uuid = UUID.fromString(path.skinName())
                        cacheDataMap[uuid] = SkinData(
                            skin,
                            model
                        )
                    } catch (_: Exception) {
                        return@launch
                    }
                }
            }
        }
    }

    fun skinReload() {
        runBlocking {
            Files.walk(
                Paths.get(
                    FileData.dataFolderPath,
                    FileData.skin
                ).apply {
                    skinDataMap.clear()
                }
            ).consumeAsFlow().filter {
                it.name.endsWith(".yml")
            }.collect { path ->
                launch(Dispatchers.IO) {
                    try {
                        val yml = path.toFile()
                        val name = path.skinParentName().plus(path.skinName())
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
                    } catch (_: Exception) {
                        return@launch
                    }
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
            URI.create(legacySkinData.textures.SKIN.url).toURL(),
            PlayerTextures.SkinModel.valueOf(
                legacySkinData.textures.SKIN.metadata?.model?.uppercase() ?: PlayerTextures.SkinModel.CLASSIC.name
            )
        ).apply {
            skinDataMap[name] = this
        }.run {
            skinSave(this, name, true)
        }
    }

    fun cacheDataSave(player: Player) {
        val uuid = player.uniqueId
        cacheDataMap[uuid] = skinData(player).apply {
            cacheSave(player, this)
        }
    }

    fun cacheDataDelete(player: Player) {
        val uuid = player.uniqueId
        cacheDataMap.remove(uuid)
    }

    fun cacheSave(player: Player, skinData: SkinData) {
        Paths.get(
            FileData.dataFolderPath,
            FileData.cache,
            player.uniqueId.toString().plus(".yml")
        ).apply {
            FileData.creater(this)
            YmlData.saveSkin(this, skinData)
        }
    }

    fun skinData(player: Player): SkinData {
        val textures = player.playerProfile.textures
        return SkinData(
            textures.skin!!,
            textures.skinModel
        )
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
        Paths.get(
            FileData.dataFolderPath,
            FileData.skin,
            name.plus(".yml")
        ).apply {
            if (!FileData.creater(this)) {
                Paths.get(
                    pathString.replaceFirst(FileData.skin, FileData.abyss)
                ).let { abyss ->
                    FileData.creater(abyss)
                    toFile().copyTo(abyss.toFile(), true)
                }
            }
            YmlData.saveSkin(this, skinData, isLegacy)
        }
    }

    fun reset(player: Player) {
        val uuid = player.uniqueId
        cacheDataMap[uuid]?.apply {
            skinSet(player, this)
        }
    }

    fun save(player: Player, name: String) {
        skinData(player).apply {
            skinDataMap[name] = this
        }.run {
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