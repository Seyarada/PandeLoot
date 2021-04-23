package net.seyarada.pandeloot.compatibility;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.*;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.utilities.ScriptUtilities;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class DenizenCompatibility {

    public static ItemStack getItem(String item) {
        ItemTag itemTag = ItemTag.valueOf(item, true);
        return itemTag.getItemStack();
    }

    public void execute(String scriptName) {
        ScriptTag scripTag = new ScriptTag(scriptName);
        ScriptEntry scriptEntry = new ScriptEntry("Run", null, scripTag.getContainer());
        ElementTag pathElement = scriptEntry.getElement("path");
        ElementTag instant = scriptEntry.getElement("instant");
        ElementTag id = scriptEntry.getElement("id");
        DurationTag speed = scriptEntry.getObjectTag("speed");
        DurationTag delay = scriptEntry.getObjectTag("delay");
        MapTag defMap = scriptEntry.getObjectTag("def_map");
        String path = pathElement != null ? pathElement.asString() : null;
        if (scripTag == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Script run failed (invalid script name)!");
            return;
        }
        if (path != null && (!scripTag.getContainer().contains(path) || !scripTag.getContainer().getContents().isList(path))) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Script run failed (invalid path)!");
            return;
        }
        if (instant != null && instant.asBoolean()) {
            speed = new DurationTag(0);
        }
        ListTag definitions = scriptEntry.getObjectTag("definitions");
        Consumer<ScriptQueue> configure = (queue) -> {
            // Set any delay
            if (delay != null) {
                queue.delayUntil(DenizenCore.serverTimeMillis + delay.getMillis());
            }
            // Setup a callback if the queue is being waited on
            if (scriptEntry.shouldWaitFor()) {
                queue.callBack(() -> scriptEntry.setFinished(true));
            }
            if (defMap != null) {
                for (Map.Entry<StringHolder, ObjectTag> val : defMap.map.entrySet()) {
                    queue.addDefinition(val.getKey().str, val.getValue());
                }
            }
            // Save the queue for script referencing
            scriptEntry.addObject("created_queue", new QueueTag(queue));
            // Preserve procedural status
            queue.procedural = scriptEntry.getResidingQueue().procedural;
        };
        String idString = id != null ? "FORCE:" + id.asString() : null;
        ScriptQueue result = ScriptUtilities.createAndStartQueue(scripTag.getContainer(), path, scriptEntry.entryData, null, configure, speed, idString, definitions, scriptEntry);
        if (result == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Script run failed!");
        }
    }

}
