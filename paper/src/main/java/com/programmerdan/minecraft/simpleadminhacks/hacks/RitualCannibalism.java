package com.programmerdan.minecraft.simpleadminhacks.hacks;

import com.programmerdan.minecraft.simpleadminhacks.SimpleAdminHacks;
import com.programmerdan.minecraft.simpleadminhacks.configs.RitualCannibalismConfig;
import com.programmerdan.minecraft.simpleadminhacks.framework.SimpleHack;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import net.kyori.adventure.text.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 
 * ABSTRACT:
 * 
 * This hack will implement an in-game method to extract limbs and body parts (named/lored items) from a killed player, ala Cannibalism.
 * Yes. I took my time to attempt to code Cannibalism for Civ. The memes can stop now. :)
 * 
 * What I want to do, is allow server admins to define an extra loot table for player death events, 
 * given specific enchantments available on the killer's tool, 
 * and appends the new tables to the actual drops of the player.
 * 
 * Now, my implementation isn't EXACTLY a Mojang-standard JSON Loot Table, but the concept is practically the same. But in SAH's YAML config. Not JSON. <:) 
 * 
 * What loot table or loot chances is actually applied, will depend on:
 * 1. What tool was used by the killer to kill the player, 
 * 2. The enchantments and levels present on the tool, and
 * 3.  The name of the tool itself, as naming items costs XP.
 * 
 * With point #3, from a game design perspective, we assume that "naming" the item imbues it with "magic" (xp), 
 * which tells our hack which "limbs/organs" (different loot tables and chances) should drop from the player victim. 
 * 
 * As Smite has been observed to be the most useless enchantment for PVP, we will use the Smite enchantment as our de-facto required "spell" to activate the hack.
 * Thematically, this makes sense, as Smite is used to deal more damage to undead mobs, which all ALSO happen to be humanoid. 
 * (In the future, this could maybe apply to the undead mobs, too.)
 * 
 */

/**
 * The RitualCannibalism hack's main class.
 * Implements cannibalism as a Simple Hack.
 * @author Klari
 */
public final class RitualCannibalism extends SimpleHack<RitualCannibalismConfig> implements Listener {
	
	public static String NAME = "RitualCannibalism";

	// private double smiteChance;

	// private double lootingChance;

	// private Material meatItem;

	// private String meatName;

	// private List<String> meatLore;

	// private Material heartItem;

	// private String heartName;

	// private List<String> heartLore;

	// Constructor that constructs our hack
	public RitualCannibalism(SimpleAdminHacks plugin, RitualCannibalismConfig config) {
		super(plugin, config);
	}

	// THERE'S EVEN A DECORATOR FOR HANDLING EVENTS AUTOMAGICALLY???
	// HOLY CRAP JAVA SYNTAX (and ESPECIALLY the SAH Framework) IS BASED
	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event) {
		// If the killer of the player is null, return and don't do anything.
		// Writing the condition in this order also prevents event.getEntity().getKiller() from being assigned TO null, instead of throwing an error, JUST IN CASE you use a SINGLE EQUAL SIGN. :)
		Player killer = event.getEntity().getKiller();
		if (null == killer) return;

		// Check the killer's currently equipped weapon
		ItemStack handstack = killer.getEquipment().getItemInMainHand();

		// Get the map of enchantments on that item
		Map<Enchantment, Integer> itemEnchants = handstack.getEnchantments();
		if (itemEnchants.isEmpty()) return;

		// Check if one enchantment is Smite
		if (null == itemEnchants.get(Enchantment.DAMAGE_UNDEAD)) return;
		
		
		double randomNum = Math.random();
		double levelOfSmite = handstack.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
		double levelOfLooting = handstack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);

		// The actual percent drop chance algorithm, ripped straight out of ArthropodEggHack
		double dropPercentage = (smiteChance * levelOfSmite) + (lootingChance * levelOfLooting);

		// I might as well be code golfing at this point given the following one-liners:

		boolean thresholdReached = randomNum < dropPercentage;
		// If thresholdReached == true, use the heartItem. Else, use the meatItem.
		Material chosenItem = thresholdReached ? heartItem : meatItem;
		// Make a new Component.text(), and if thresholdReached == true, use the heartName. Else, use the meatName.
		Component chosenName = Component.text(thresholdReached ? heartName : meatName);
		// Make our chosenItem as a new ItemStack of 1 
		ItemStack item = new ItemStack(chosenItem, 1);
		// Paper counts the setDisplayName method as deprecated, so we'll use the new method.
		ItemMeta meta = item.getItemMeta();
		//ArrayList<String> lore = new ArrayList<String>();

		// NOW WE HAVE ALL OF OUR STUFF. Take all of our STUFF and SMASH IT BACK IN.
		lore.add();
		item.getItemMeta()//.displayName(chosenName);
		event.getDrops().add(item);

		// TODO: Get lore from config, and if lore = null, return
		// TODO: Create a replacer that removes defintions like %v and %k and replace them with the names of the Victim and Killer wherever defined, respectfully
		// TODO: Append lore to heartItem

		// if (randomNum < targetPercentage) {
		// 	ItemStack item = new ItemStack(heartItem, 1);
		// 	event.getDrops().add(item);
		// } else {
		// 	ItemStack item = new ItemStack(meatItem, 1);
		// 	event.getDrops().add(item);
		// }

	}
}
