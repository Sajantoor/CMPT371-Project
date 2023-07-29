import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

class CursorManager extends JPanel{
    private Map<Integer, Cursor> cursors = new HashMap<>();

    public Cursor createCursor(int playerId) {
        if (cursors.containsKey(playerId)) {
            throw new RuntimeException("Cursor already exists for player: " + playerId);
        }
        Cursor cursor = new Cursor(playerId);
        cursors.put(playerId, cursor);
        return cursor;
    }

    public Cursor getCursor(int playerId) {
        return cursors.get(playerId);
    }

    public void moveCursor(int playerId, int x, int y) {
        Cursor cursor = cursors.get(playerId);
        if (cursor == null) {
            throw new RuntimeException("No cursor exists for player: " + playerId);
        }
        cursor.setLocation(x, y);
    }
}
