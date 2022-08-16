package com.programmerdan.minecraft.simpleadminhacks.hacks.basic;

import com.programmerdan.minecraft.simpleadminhacks.SimpleAdminHacks;
import com.programmerdan.minecraft.simpleadminhacks.framework.BasicHack;
import com.programmerdan.minecraft.simpleadminhacks.framework.BasicHackConfig;
import com.programmerdan.minecraft.simpleadminhacks.framework.autoload.AutoLoad;
import com.programmerdan.minecraft.simpleadminhacks.framework.autoload.DataParser;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.entity.Minecart;

import vg.civcraft.mc.civmodcore.world.WorldUtils;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public final class RailCrossings extends BasicHack {

	/** 
	 * All of these blocks are valid blocks that we can treat as a rail crossing.
	 * It's recommended that these are blocks without a solid hitbox, like pressure plates.
	 * 
	 * To tighten execution, you could also just specify one list item, of a pressure plate block that you think would be used very rarely.
	 * By default, the only valid block is HEAVY_WEIGHTED_PRESSURE_PLATE.
	 */
	@AutoLoad(processor = DataParser.MATERIAL)
	private List<Material> crossingBlocks;

	public RailCrossings(final SimpleAdminHacks plugin, final BasicHackConfig config) {
		super(plugin, config);
	}
	@EventHandler
	public void onSwitchTrigger(BlockRedstoneEvent event) {
        final Block block = event.getBlock();
		// First, we search for if the event contains a valid crossing block.
		// If ANY of these are true:
		// 1. NOT a valid block, 
		// 2. crossingBlocks does NOT contain our captured block's type, 
		// 3. captured redstone component is NOT of signal strength 15,
		// Return nothing,
		// which will stop the rest of the logic following this if from executing.
		if (!WorldUtils.isValidBlock(block)
			|| !crossingBlocks.contains(block.getType()) //
			|| event.getNewCurrent() != 15) return;
		// Next, we take the 4 blocks surrounding the crossingBlock on all orthagonal directions, and the 4 blocks surrounding the block below the crossingBlock, and store them.
		final Map<String,Block> blockStore = new HashMap<String,Block>() {{
			put("north", block.getRelative(BlockFace.NORTH)); 
			put("east",  block.getRelative(BlockFace.EAST)); 
			put("south", block.getRelative(BlockFace.SOUTH)); 
			put("west",  block.getRelative(BlockFace.WEST)); 
			put("belowNorth", block.getRelative(0,0,-1));
			put("belowEast",  block.getRelative(-1,0,0));
			put("belowSouth", block.getRelative(0,0,1));
			put("belowWest",  block.getRelative(1,0,0));
		}};
		// Next, we search for if our crossingBlock is surrounded by rails on all 4 sides.
		// We also look one layer below to see if any of them could ALSO be rails, in case there's an inclined rail heading into the crossingBlock.
		blockStore.values().stream()
			.forEach(Block::getType);

		
		// Taking these values, WHATEVER they are, we now search for if ANY of them are a RAIL that is FACING the central block.


	}
}
