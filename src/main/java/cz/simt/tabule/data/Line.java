package cz.simt.tabule.data;

public class Line implements Comparable<Line> {
    private String line;
    private int traction;

    public Line (String line, int traction) {
        this.line = line;
        this.traction = traction;
    }

    public String getLine() {
        return line;
    }

    public int getTraction() {
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

    @Override
    public int compareTo(Line o) {
        if (getLine() == null || o.getLine() == null) {
            return 0;
        }
        if (getLine().length() == o.getLine().length()) {
            return getLine().compareTo(o.getLine());
        }
        return getLine().length() - o.getLine().length();
    }
}
