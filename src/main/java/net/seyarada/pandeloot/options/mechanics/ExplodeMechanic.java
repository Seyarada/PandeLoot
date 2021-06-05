package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.options.Reward;
import net.seyarada.pandeloot.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import java.util.Map;

public class ExplodeMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        if (value!=null && !value.isEmpty() && !reward.isAbandoned) {
            StringLib.warn("++++++ Applying explode effect with value "+value);
            final boolean shouldExplode = Boolean.parseBoolean(value);

            if(shouldExplode) {
                final Map<String, String> options = reward.options;
                final String type = options.get("explodetype");

                switch (type) {
                    default:
                    case "spread":
                        doSpreadDrop(reward);
                        break;
                    case "radial":
                        doRadialDrop(reward);
                        break;
                }
            }

        }
    }

    private void doSpreadDrop(Reward reward) {
        final Map<String, String> options = reward.options;
        final double offset = Double.parseDouble(options.get("expoffset"));
        final double height = Double.parseDouble(options.get("expheight"));

        Vector velocity = MathUtil.getVelocity(offset, height);
        reward.item.setVelocity(velocity);
    }

    private void doRadialDrop(Reward reward) {
        final Map<String, String> options = reward.options;
        final double radius = Double.parseDouble(options.get("exploderadius"));
        final Item item = reward.item;

        final int numberOfTotalDrops = reward.radialDropInformation.get(radius);
        final int numberOfThisDrop = reward.radialOrder;

        if(numberOfTotalDrops==1) { doSpreadDrop(reward); return; }

        final double angle = 2 * Math.PI / numberOfTotalDrops;
        final double cos = Math.cos(angle * numberOfThisDrop);
        final double sin = Math.sin(angle * numberOfThisDrop);
        final double iX = item.getLocation().getX() + radius * cos;
        final double iZ = item.getLocation().getZ() + radius * sin;

        final Location loc = item.getLocation().clone();
        loc.setX(iX);
        loc.setZ(iZ);

        item.setVelocity(
                MathUtil.calculateVelocity(item.getLocation().toVector(), loc.toVector(), 0.115, 3));
    }

}