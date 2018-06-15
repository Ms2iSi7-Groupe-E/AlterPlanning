package fr.nantes.eni.alterplanning.model.form.enums;

public enum DownloadFormat {
    pdf ("pdf"),
    rtf ("rtf");

    private final String name;

    DownloadFormat(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
