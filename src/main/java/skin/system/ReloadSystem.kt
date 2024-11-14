package skin.system

import java.util.concurrent.ConcurrentHashMap

enum class ReloadType {
    SKIN
}

object ReloadSystem {
    private val state = ConcurrentHashMap<ReloadType, Boolean>()

    fun operation(reloadType: ReloadType, failed: () -> Unit = {}, successful: () -> Unit) {
        if (!ready(reloadType)) {
            failed()
            return
        }
        start(reloadType)
        successful()
        end(reloadType)
    }

    fun ready(reloadType: ReloadType): Boolean {
        return state[reloadType] != false
    }

    fun start(reloadType: ReloadType) {
        state[reloadType] = true
    }

    fun end(reloadType: ReloadType) {
        state.remove(reloadType)
    }
}