package org.PrimeSoft.MCPainter.blocksplacer;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.history.UndoContext;
import com.sk89q.worldedit.history.change.Change;
import org.PrimeSoft.MCPainter.MCPainterMain;
import org.bukkit.entity.Player;

/**
 *
 * @author SBPrime
 */
public class ChangeMessage implements Change {
    private final Player m_player;
    private final String m_msg;
    
    public ChangeMessage(Player player, String msg) {
        m_player = player;
        m_msg = msg;
    }
    
    
    public void undo(UndoContext uc) throws WorldEditException {
    }

    public void redo(UndoContext uc) throws WorldEditException {
        MCPainterMain.say(m_player, m_msg);
    }
}
