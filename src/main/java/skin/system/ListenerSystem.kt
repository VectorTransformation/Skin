package skin.system

import org.bukkit.event.Listener
import skin.listener.SkinListener

object ListenerSystem {
    fun register(listener: Listener) {
        Cardinal.registerEvent(listener)
    }

    fun register() {
        register(SkinListener())
    }
}