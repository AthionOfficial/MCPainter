package org.PrimeSoft.MCPainter.blocksplacer;

import org.PrimeSoft.MCPainter.MCPainterMain;
import org.PrimeSoft.MCPainter.utils.BaseBlock;
import org.PrimeSoft.MCPainter.utils.Vector;
import org.PrimeSoft.MCPainter.worldEdit.IEditSession;
import org.PrimeSoft.MCPainter.worldEdit.MaxChangedBlocksException;
import org.bukkit.entity.Player;

/**
 *
 * @author SBPrime
 */
public class BlockEntry extends BlockLogerEntry {

    private final Vector m_location;
    private final BaseBlock m_newBlock;

    public BlockEntry(BlockLoger loger, Vector location, BaseBlock newBlock) {
        super(loger);

        m_location = location;
        m_newBlock = newBlock;
    }

    @Override
    public void execute(BlockPlacer blockPlacer) {
        Player p = getPlayer();
        IEditSession eSession = m_loger.getEditSession();
        try {
            eSession.setBlock(m_location, m_newBlock);
        } catch (MaxChangedBlocksException ex) {
            MCPainterMain.say(p, "Max block change reached");
            MCPainterMain.log(ex.getMessage());
        }
    }

    @Override
    public boolean canRemove() {
        return true;
    }

}
