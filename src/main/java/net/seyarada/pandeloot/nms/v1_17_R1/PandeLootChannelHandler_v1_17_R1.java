package net.seyarada.pandeloot.nms.v1_17_R1;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class PandeLootChannelHandler_v1_17_R1 extends ChannelDuplexHandler {

    private static final Field packetSpawnEntityId;

    static {
        Field temp;
        try {
            temp = PacketPlayOutSpawnEntity.class.getDeclaredField("a");
            temp.setAccessible(true);
        } catch (NoSuchFieldException e) {
            temp = null;
        }
        packetSpawnEntityId = temp;
    }

    private final EntityPlayer player;

    public PandeLootChannelHandler_v1_17_R1(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {

        if (packetSpawnEntityId != null) {
            if (packet instanceof PacketPlayOutSpawnEntity) {
                int id = packetSpawnEntityId.getInt(packet);
                if (NMSManager.hideItemFromPlayerMap.containsKey(id)) {
                    List<Player> players = NMSManager.hideItemFromPlayerMap.get(id);
                    if (!players.contains(player.getBukkitEntity())) {
                        System.out.println("Stopped sending packet to " + player.displayName);
                        return;
                    }
                }
            }
        }

        super.write(ctx, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        super.channelRead(ctx, packet);
    }
}
