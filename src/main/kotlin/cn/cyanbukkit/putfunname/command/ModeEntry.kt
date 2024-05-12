package cn.cyanbukkit.putfunname.command

import cn.cyanbukkit.putfunname.GameHandle
import cn.cyanbukkit.putfunname.utils.Mode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ModeEntry : Command("mode") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is org.bukkit.entity.Player) {
            sender.sendMessage("§c只有玩家才能执行此命令")
            return true
        }
        val p = sender
        val hand = GameHandle()
        val methods = hand.javaClass.declaredMethods
        for (method in methods) {
            val mode = method.getAnnotation(Mode::class.java)
            if (mode != null) {
                if (mode.value == args[0]) {
                    val info = args.sliceArray(1 until args.size)
                    val parameterTypes = method.parameterTypes
                    val convertedArgs = anise(info, parameterTypes, p)
                    method.invoke(hand, *convertedArgs)
                    return true
                }
            }
        }
        p.sendMessage("§c未找到模式")
        return true
    }



    private fun anise(
        info: Array<out String>,
        parameterTypes: Array<Class<*>>,
        p: Player
    ): Array<Any> {
        val convertedArgs = Array(info.size) { i ->
            when (parameterTypes[i]) { // i + 1 because the first parameter is Player
                Double::class.java -> info[i].toDouble()
                Int::class.java -> info[i].toInt()
                String::class.java -> info[i]
                Player::class.java -> p
                else -> throw IllegalArgumentException("Unsupported parameter type")
            }
        }
        return convertedArgs
    }
    
}
