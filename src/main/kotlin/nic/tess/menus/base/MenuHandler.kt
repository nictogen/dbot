package nic.tess.menus.base

import nic.tess.menus.MainMenu
import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.emoji.CustomEmoji
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.Reaction
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.MessageDeleteEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import org.javacord.api.listener.message.MessageCreateListener
import org.javacord.api.listener.message.MessageDeleteListener
import org.javacord.api.listener.message.reaction.ReactionAddListener
import java.awt.Color
import java.util.*

object MenuHandler : ReactionAddListener, MessageCreateListener, MessageDeleteListener{

    override fun onMessageCreate(event: MessageCreateEvent) {
        if (event.message.author.isUser && event.message.userAuthor.isPresent && !event.message.userAuthor.get().isBot){
            if(event.message.content.contains(event.message.api.yourself.id.toString())) {
                MainMenu(event.message.userAuthor.get(), event.channel)
                event.message.delete()
            }
            menuList.filter { it.isValidMessager(event.message.userAuthor.get()) }.forEach {
                it.onMessage(event.channel.asServerTextChannel().get().server, event.message.userAuthor.get(), event.message)
            }
        }
    }

    override fun onReactionAdd(event: ReactionAddEvent) {
        menuList.filter { event.users.get().any { usr -> it.isValidReactor(usr) } && it.messageID == event.message.get().id }.forEach {
                it.onReaction(event.channel.asServerTextChannel().get().server, it.user, event.reaction.get())
        }
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        menuList.removeAll(menuList.filter { event.messageId == it.messageID })
    }

    val menuList = ArrayList<Menu>()

    abstract class Menu(val user: User, var message: Message?, channel: TextChannel, color: Color, description: String, vararg fields : Field) {
        var messageID : Long = 0
        init {
            val embed = EmbedBuilder()
            embed.setDescription(description)
            embed.setColor(color)
            fields.forEach { embed.addField(it.name, it.value, it.inline) }
            if(message == null){
                channel.sendMessage(embed).whenComplete { msg, _ ->
                    message = msg
                    messageID = msg.id
                    addReactions(msg, fields)
                }
            } else {
                menuList.removeAll { it.message?.id == message?.id }
                message!!.removeAllReactions().whenComplete { _, _ ->
                    message!!.edit(embed).whenComplete { _, _ ->
                        addReactions(message!!, fields)
                        this.messageID = message!!.id
                    }
                }
            }
            menuList.add(this)
        }

        fun addReactions(message: Message, fields: Array<out Field>){
            fields.forEach {
                if(it.customEmoji.isPresent){
                    message.addReaction(it.customEmoji.get())
                } else message.addReaction(it.name)
            }
        }

        abstract fun onReaction(server: Server, user: User, reaction: Reaction)

        abstract fun onMessage(server: Server, user: User, message: Message)

        open fun isValidReactor(user: User): Boolean {
            return user == this.user
        }

        open fun isValidMessager(user: User): Boolean {
            return user == this.user
        }
    }

    class Field(val name : String, val value : String, val inline : Boolean, val customEmoji: Optional<CustomEmoji> = Optional.empty())

    class Notify(user: User, message: Message?, channel: TextChannel, description: String, color : Color = Color.RED) : Menu(user, message, channel, color, description, Field("⏩", "Thank You", true)) {

        override fun onMessage(server: Server, user: User, message: Message) {}

        override fun onReaction(server: Server, user: User, reaction: Reaction) {
            if (reaction.emoji.isUnicodeEmoji && reaction.emoji.asUnicodeEmoji().get() == "⏩") {
                reaction.message.delete()
            }
        }

    }


}