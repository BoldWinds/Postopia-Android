package com.postopia.data.local.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.postopia.proto.UserProto
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<UserProto>{
    override val defaultValue: UserProto = UserProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProto {
        try{
            return UserProto.parseFrom(input)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: UserProto, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.userDataStore: DataStore<UserProto> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)
