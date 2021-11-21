package cz.simt.tabule.data;

public class Line {
    private String line;
    private String traction;

    public Line (String line) {
        this.line = line;
        this.traction = getTraction(line);
    }

    public String getLine() {
        return line;
    }

    public String getTraction() {
        return traction;
    }

    private String getTraction(String line) {
        return parseLineToInt(line) < 20 ? parseLineToInt(line) < 10 ? "tram" : "trolleybus" : "bus";
    }

    private static int parseLineToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 100;
        }
    }
}
