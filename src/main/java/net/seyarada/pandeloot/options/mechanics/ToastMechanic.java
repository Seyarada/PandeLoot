package net.seyarada.pandeloot.options.mechanics;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R3.*;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.options.Reward;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ToastMechanic implements MechanicEvent {

    private ItemStack icon;
    private String title;
    private String frame;

    @Override
    public void onCall(Reward reward, String value) {
        StringLib.warn("++++++ Applying toast effect with value "+value);
        if(value!=null && !value.isEmpty()) {
            final Map<String, String> options = reward.options;
            title = value;
            icon = options.get("toasticon")==null
                    ? new ItemStack(Material.STONE, 1)
                    : new ItemStack(Material.valueOf(options.get("toasticon").toUpperCase()), 1);
            frame = options.get("toastframe")==null ? "GOAL" : options.get("toastframe");

            displayToast(reward.player);
        }
    }

    public void displayToast(Player player) {
        MinecraftKey minecraftKey = new MinecraftKey("pandeloot", "notification");
        HashMap<String, Criterion> criteria = new HashMap<>();

        criteria.put("for_free",new Criterion(new CriterionInstance() {
            public MinecraftKey a() {
                return new MinecraftKey("minecraft","impossible");
            }
            @Override // Not needed
            public JsonObject a(LootSerializationContext lootSerializationContext) { return null; }
        }));

        ArrayList<String[]> fixed = new ArrayList<>();
        fixed.add(new String[]{"for_free"});

        String[][] requirements = Arrays.stream(fixed.toArray()).toArray(String[][]::new);

        IChatBaseComponent chatTitle = new ChatMessage(title);
        //IChatBaseComponent chatDescription = new ChatMessage(description);
        AdvancementFrameType advancementFrame = AdvancementFrameType.valueOf(frame.toUpperCase());
        net.minecraft.server.v1_16_R3.ItemStack craftIcon = CraftItemStack.asNMSCopy(icon);
        
        AdvancementDisplay display = new AdvancementDisplay(craftIcon, chatTitle, null, null, advancementFrame, true,true,true);
        AdvancementRewards reward = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);
        Advancement advancement = new Advancement(minecraftKey, null, display, reward, criteria, requirements);

        HashMap<MinecraftKey,AdvancementProgress> progressMap = new HashMap<>();
        AdvancementProgress progress = new AdvancementProgress();
        progress.a(criteria,requirements);
        progress.getCriterionProgress("for_free").b();
        progressMap.put(minecraftKey,progress);

        PacketPlayOutAdvancements packet
                = new PacketPlayOutAdvancements(false, Collections.singletonList(advancement), new HashSet<>(), progressMap);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

        // Remove the advancement
        HashSet<MinecraftKey> remove = new HashSet<>();
        remove.add(minecraftKey);
        progressMap.clear();
        packet = new PacketPlayOutAdvancements(false, new ArrayList<>(), remove, progressMap);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
