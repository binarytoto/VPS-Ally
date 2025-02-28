package top.cuboid.vpsally.data

import top.cuboid.vpsally.data.local.SolusServer
import top.cuboid.vpsally.domain.DataErrors
import top.cuboid.vpsally.domain.Result
import top.cuboid.vpsally.domain.SolusRequestConstants

interface SolusRepository {
    suspend fun performRemoteAction(
        action: String = SolusRequestConstants.STATUS_FLAG,
        server: SolusServer
    ): Result<Server, DataErrors.Network>

    suspend fun saveServer(server: SolusServer): Result<Boolean, DataErrors.Local>
    suspend fun getServerDetailsLocally(server: SolusServer): SolusServer?
}



