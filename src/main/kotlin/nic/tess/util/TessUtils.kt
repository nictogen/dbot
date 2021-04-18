package nic.tess.util

object TessUtils {
    fun getKey(s: String) = s.substring(0, s.indexOf('='))
    fun getValue(s: String) = s.substring(s.indexOf('=') + 1)
//    fun isPlayerChannel(channelID: Long, server: Server) = PlayerHandler.players.any { channelID == it.channelID && server.id == it.serverID }
//    fun getPlayer(channelID: Long, serverID: Long) = PlayerHandler.players.first { channelID == it.channelID && serverID == it.serverID }
//    fun isAdmin(user: User, server: Server) = user.getRoles(server).any { it.allowedPermissions.contains(PermissionType.MANAGE_CHANNELS) }

    fun listFromString(string: String): List<String> {
        var s = string
        s = s.replace("\\[\\],", "")
        s = s.replace(",", "")
        s = s.replace("[", "")
        s = s.replace("]", "")
        return s.split(" ")
    }
}