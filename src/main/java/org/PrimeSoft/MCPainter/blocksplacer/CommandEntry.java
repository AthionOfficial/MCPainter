package org.PrimeSoft.MCPainter.blocksplacer;

/**
 *
 * @author SBPrime
 */
public class CommandEntry extends BlockLogerEntry {
    private final ILoggerCommand m_command;
        
    public CommandEntry(BlockLoger loger, ILoggerCommand command) {
        super(loger);
        
        m_command = command;
    }

    @Override
    public boolean canRemove() {
        return true;
    }    
    
    
    @Override
    public void execute(BlockPlacer blockPlacer) {
        m_command.execute(blockPlacer, m_loger);
    }
    
}
