package net.seyarada.pandeloot.drops;

public class DropEffects {

    /*
    private final DropItem src;
    private final Item item;
    private final RewardLine reward;
    public static Map<UUID, DropItem> playOnPickupStorage = new HashMap<>();

    private boolean isAbandoned;
    public static Map<UUID, DropItem> storeForAbandon = new HashMap<>();

    public DropEffects(DropItem source) {
        this.src = source;
        this.item = source.item;
        this.reward = src.reward;

        if(item==null) {
            runPlayerEffects();
            runGeneralEffects();
            return;
        }

        storeForAbandon.put(item.getUniqueId(), source);

        if(runItemEffects()) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    public DropEffects(DropItem source, boolean noExecuteItemEffects) {
        this.src = source;
        this.item = source.item;
        this.reward = src.reward;

        if(noExecuteItemEffects) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    public DropEffects(UUID uuid, Item item) {
        this.src = storeForAbandon.get(uuid);
        this.item = item;
        this.reward = src.reward;

        storeForAbandon.remove(uuid);
        isAbandoned = true;
        reward.abandonTime = 0;

        if(runItemEffects()) {
            runPlayerEffects();
            runGeneralEffects();
        }
    }

    private boolean runItemEffects() {
        if(item==null) return true;

        if(reward.abandonTime>0||isAbandoned) reward.playonpickup=true;

        hologramEffect();
        explodeEffect();

        if (reward.playonpickup) {
            UUID uuid = UUID.randomUUID();
            item.setItemStack(NMSManager.addNBT(item.getItemStack(), StringLib.playOnPickup, uuid.toString()));
            playOnPickupStorage.put(uuid, src);
            reward.playonpickup = false;
            return false;
        }

        return true;
    }*/
}
