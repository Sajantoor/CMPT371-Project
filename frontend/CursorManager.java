import java.util.List;
import java.util.ArrayList;

public class CursorManager {
    private static CursorManager instance = null;
    private List<Cursor> cursors;

    private CursorManager() {
        cursors = new ArrayList<Cursor>();
        instance = this;
    }

    public static CursorManager getInstance() {
        if (instance == null) {
            instance = new CursorManager();
        }
        return instance;
    }

    public void addCursor(Cursor cursor) {
        cursors.add(cursor);
    }

    public Cursor getCursor(int playerID) {
        for (Cursor cursor : cursors) {
            if (cursor.getPlayerID() == playerID) {
                return cursor;
            }
        }

        return null;
    }

    public void createCursors() {
        // New cursor is for the player
        new Cursor();
        // Other cursors are for other players
        for (int i = 0; i < 4; i++) {
            addCursor(new Cursor(i));
        }
    }
}
