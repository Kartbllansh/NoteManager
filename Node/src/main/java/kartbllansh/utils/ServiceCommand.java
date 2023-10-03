package kartbllansh.utils;

import java.util.Optional;

public enum ServiceCommand {
    HELP("/help"),
    CANCEL("/cancel"),
    START("/start"),
    ADD_NOTE("/add"),
    VIEW_NOTE("/view"),
    EDIT_NOTE("/edit"),
    SUPPORT("/support");


    private final String value;

    ServiceCommand(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Optional<ServiceCommand> fromValue(String v) {
        for (ServiceCommand c: ServiceCommand.values()) {
            if (c.value.equals(v)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
