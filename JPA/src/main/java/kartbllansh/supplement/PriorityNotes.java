package kartbllansh.supplement;

import java.util.Optional;

public enum PriorityNotes {


    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LIGHT("LIGHT");
    private final String s;
    PriorityNotes(String s) {
        this.s = s;
    }
    public static Optional<PriorityNotes> fromValue(String v) {
        for (PriorityNotes c: PriorityNotes.values()) {
            if (c.s.equals(v)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
