package skin.system.listener

import org.bukkit.event.Listener
import skin.listener.SkinListener
import skin.system.Cardinal

object ListenerSystem {
    val registerList = listOf(
        SkinListener()
    )

    fun register() {
        registerList.forEach(::register)
    }

    fun register(listener: Listener) {
        Cardinal.registerEvent(listener)
    }
}