package org.PrimeSoft.MCPainter.blocksplacer;

import org.bukkit.entity.Player;


/**
 *
 * @author SBPrime
 */
public abstract class BlockLogerEntry {
    protected final BlockLoger m_loger;

    public abstract boolean canRemove();
    
    public BlockLoger getLoger() {
        return m_loger;
    }
    
    public Player getPlayer() {
        return m_loger.getPlayer();
    }

    public BlockLogerEntry(BlockLoger loger) {
        m_loger = loger;
    }
    
    /**
     * Execute the command
     * @param blockPlacer
     */
    public abstract void execute(BlockPlacer blockPlacer);
}
