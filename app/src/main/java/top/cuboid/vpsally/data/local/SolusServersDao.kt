package top.cuboid.vpsally.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SolusServersDao {

    @Query("SELECT * FROM solusserver")
    fun getAll(): List<SolusServer>

    @Query(" SELECT * FROM solusserver WHERE sid =:sid")
    fun findById(sid: Int): SolusServer

    @Update
    fun updateServer(server: SolusServer)

    @Delete
    fun deleteServer(vararg servers: SolusServer)

    @Insert(onConflict = OnConflictStrategy.ABORT )
    suspend fun addServer(server: SolusServer)

    @Query("SELECT * FROM solusserver WHERE hash=:hash AND `key`=:key AND 'url'=:url")
    fun findByDetail(key: String, hash: String, url: String): SolusServer?
}