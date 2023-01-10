package xyz.holocons.mc.holoa9t.util;

public record PositionIntXYZ(int x, int y, int z) {

    public static PositionIntXYZ from(String position) {
        var coordsString = position.split(",", 3 + 1);
        if (coordsString.length != 3) {
            return null;
        }

        try {
            var x = Integer.parseInt(coordsString[0].trim());
            var y = Integer.parseInt(coordsString[1].trim());
            var z = Integer.parseInt(coordsString[2].trim());

            return new PositionIntXYZ(x, y, z);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static String getPlaceholder() {
        return "0, 0, 0";
    }
}
