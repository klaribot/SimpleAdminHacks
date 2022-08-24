package com.programmerdan.minecraft.simpleadminhacks.hacks.basic;

import com.programmerdan.minecraft.simpleadminhacks.SimpleAdminHacks;
import com.programmerdan.minecraft.simpleadminhacks.framework.BasicHack;
import com.programmerdan.minecraft.simpleadminhacks.framework.BasicHackConfig;
import com.programmerdan.minecraft.simpleadminhacks.framework.autoload.AutoLoad;
import com.programmerdan.minecraft.simpleadminhacks.framework.autoload.DataParser;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;

import vg.civcraft.mc.civmodcore.world.WorldUtils;
import vg.civcraft.mc.civmodcore.world.ChunkLoadedFilter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

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

	private List<Material> validRails = new ArrayList<Material>()

	public RailCrossings(final SimpleAdminHacks plugin, final BasicHackConfig config) {
		super(plugin, config);
	}

	/**
	 * We use an EntityInteractEvent instead of a BlockRedstoneEvent, 
	 * because we really don't care about what the redstone activation level is.
	 * We only care about if a Minecart is interacting with a pressure plate (or rather, the blocks defined in crossingBlocks)
	 * This also gives us exactly which Minecart in the world is interacting with its corresponding pressure plate!
	 */
	@EventHandler
	public void onEntityInteract(final EntityInteractEvent event) {

		/** Captured origin block that our entity is interacting with. */
		final Block block = event.getBlock();
		/** Captured entity that is interacting with our origin block */
		final Entity entity = event.getEntity();

		// First, we search for if the event contains a valid crossing block.
		// If ANY of these are true:
		// 1. NOT a valid block (thank you for existing, WorldUtils)
		// 2. The entity that triggered the event is NOT an instance of a Minecart
		// 3. crossingBlocks does NOT contain our captured block's type
		// Return nothing,
		// which will stop the rest of the logic following this if from executing.
		if (!WorldUtils.isValidBlock(block)
		 || !(entity instanceof Minecart) 
		 || !crossingBlocks.contains(block.getType())) return;
		
		// Now, we stuff all 8 blocks that we need to detect into a new HashMap
		final Map<String,Block> cardinalBlocks = new HashMap<String,Block>();
		// {{
		// 	put("n", block.getRelative(BlockFace.NORTH));
		// 	put("e", block.getRelative(BlockFace.EAST));
		// 	put("s", block.getRelative(BlockFace.SOUTH));
		// 	put("w", block.getRelative(BlockFace.WEST));
		// 	put("bn", block.getRelative(0,0,-1));
		// 	put("be", block.getRelative(-1,0,0));
		// 	put("bs", block.getRelative(0,0,1));
		// 	put("bw", block.getRelative(1,0,0));
		// }};
		cardinalBlocks.put("n", block.getRelative(BlockFace.NORTH));
		cardinalBlocks.put("s", block.getRelative(BlockFace.EAST));
		cardinalBlocks.put("e", block.getRelative(BlockFace.SOUTH));
		cardinalBlocks.put("w", block.getRelative(BlockFace.WEST));
		cardinalBlocks.put("bn", block.getRelative(0,0,-1));
		cardinalBlocks.put("be", block.getRelative(-1,0,0));
		cardinalBlocks.put("bs", block.getRelative(0,0,1));
		cardinalBlocks.put("bw", block.getRelative(1,0,0));

		// We take a sample of the world our entity exists in, to later use for ChunkLoadedFilter
		final World world = entity.getWorld();

		// ALRIGHT, THIS ONE'S A DOOZY
		// This bad boy makes use of a Stream, instead of iterating things imperatively. 
		final List<Material> probablyRails = cardinalBlocks.values().stream()
				.map(Block::getType)
				.distinct()

		// Next, we search for if our crossingBlock is surrounded by rails on all 4 sides.
		// We also look one layer below to see if any of them could ALSO be rails, in case there's an inclined rail heading into the crossingBlock.
		
		
		// Taking these values, WHATEVER they are, we now search for if ANY of them are a RAIL that is FACING the central block.
	}

	private final class BlockStore {
		private Block n, e, s, w, bn, be, bs, bw;

		// Constructor
		protected BlockStore (Block n, Block bn, Block e, Block be, Block s, Block bs, Block w, Block bw){
			this.n = n; this.bn = bn;
			this.e = e; this.be = be;
			this.s = s; this.bs = bs;
			this.w = w; this.bw = bw;
		};
	};
};
