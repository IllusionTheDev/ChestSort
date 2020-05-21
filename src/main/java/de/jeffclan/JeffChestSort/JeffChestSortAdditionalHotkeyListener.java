package de.jeffclan.JeffChestSort;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class JeffChestSortAdditionalHotkeyListener implements Listener {
	
	JeffChestSortPlugin plugin;
	
	public JeffChestSortAdditionalHotkeyListener(JeffChestSortPlugin jeffChestSortPlugin) {
		this.plugin = jeffChestSortPlugin;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if(!plugin.getConfig().getBoolean("allow-hotkeys")) {
			return;
		}
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		// Only continue if clicked outside of the chest
		if(e.getClickedInventory()!=null) {
			return;
		}
		// Possible fix for #57
		if(e.getInventory().getHolder()==null) return;
		if(e.getInventory().getHolder() == p && e.getInventory() != p.getInventory()) return;
		// End Possible fix for #57
		if(e.getInventory().getType() != InventoryType.CHEST
				&& e.getInventory().getType() != InventoryType.DISPENSER
				&& e.getInventory().getType() != InventoryType.DROPPER
				&& e.getInventory().getType() != InventoryType.ENDER_CHEST
				&& e.getInventory().getType() != InventoryType.SHULKER_BOX
				&& (e.getInventory().getHolder() == null || !e.getInventory().getHolder().getClass().toString().endsWith(".CraftBarrel"))) {
			return;
		}
		
		if(!p.hasPermission("chestsort.use")) return;
		
		plugin.registerPlayerIfNeeded(p);
		JeffChestSortPlayerSetting setting = plugin.perPlayerSettings.get(p.getUniqueId().toString());
		
		if(e.isLeftClick() && setting.leftClick) {
			plugin.invhelper.stuffPlayerInventoryIntoAnother(p.getInventory(), e.getInventory());
			plugin.sortInventory(e.getInventory());
			plugin.invhelper.updateInventoryView(e.getInventory());
		} else if(e.isRightClick() && setting.rightClick) {
			plugin.invhelper.stuffInventoryIntoAnother(e.getInventory(), p.getInventory(),e.getInventory());
		}
	}

}
