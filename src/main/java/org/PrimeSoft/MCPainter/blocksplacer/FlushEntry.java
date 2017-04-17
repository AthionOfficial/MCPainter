package org.PrimeSoft.MCPainter.blocksplacer;

import org.PrimeSoft.MCPainter.worldEdit.IEditSession;

public class FlushEntry extends BlockLogerEntry {
    public FlushEntry(BlockLoger loger) {
        super(loger);
    }

    @Override
    public boolean canRemove() {
        return false;
    }
    
    
    
    @Override
    public void execute(BlockPlacer blockPlacer) {
        IEditSession eSession = m_loger.getEditSession();
        m_loger.getLocalSession().remember(eSession);
    }    
}
