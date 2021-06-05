package net.seyarada.pandeloot.options.conditions;

import net.seyarada.pandeloot.Config;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.options.ConditionEvent;
import net.seyarada.pandeloot.options.Reward;
import org.bukkit.entity.Player;

public class TopCondition implements ConditionEvent {

    boolean result;

    @Override
    public boolean onCall(Reward reward, String value) {

        if(reward.damageUtil==null) return true;
        if(value==null) return true;

        StringLib.warn("++++++ Condition: Top, Value: "+value);

        DamageUtil damageUtil = reward.damageUtil;
        Player player = reward.player;

        StringLib.warn("+++++++ The player to check has a top of: "+damageUtil.getPlayerRank(player));

        if(value.contains("to")) {
            StringLib.warn("+++++++ Top is ranged");
            String[] values = value.split("to");
            result = Integer.parseInt(values[0]) <= damageUtil.getPlayerRank(player) &&
                    Integer.parseInt(values[1]) >= damageUtil.getPlayerRank(player);
            StringLib.warn("++++++++ Result is: "+result);
            return  result;
        }

        int intTop = Integer.parseInt(value)-1;
        if(intTop>=0) {
            if(damageUtil.getRankedPlayers().size()>intTop) {
                StringLib.warn("++++++++ Player at internal position "+intTop+ " is "+damageUtil.getRankedPlayers().get(intTop).getKey());
                return damageUtil.getRankedPlayers().get(intTop).getKey().equals(player);
            }
            StringLib.warn("++++++++ Player at internal position "+intTop+ " is not registered");
            return false;
        }
        StringLib.warn("++++++++ Internal position "+intTop+ " is <0, ignoring condition");
        return true;
    }
}
