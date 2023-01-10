package xyz.holocons.mc.holoa9t.util;

public record CuboidIntXYZ(int x1, int y1, int z1, int x2, int y2, int z2) {

    public static CuboidIntXYZ from(String str) {
        var beginEnd = str.split("~", 2 + 1);
        if (beginEnd.length != 2) {
            return null;
        }

        var beginCoords = beginEnd[0].split(",", 3 + 1);
        if (beginCoords.length != 3) {
            return null;
        }

        var endCoords = beginEnd[1].split(",", 3 + 1);
        if (endCoords.length != 3) {
            return null;
        }

        try {
            var x1 = Integer.parseInt(beginCoords[0].trim());
            var y1 = Integer.parseInt(beginCoords[1].trim());
            var z1 = Integer.parseInt(beginCoords[2].trim());

            var x2 = Integer.parseInt(endCoords[0].trim());
            var y2 = Integer.parseInt(endCoords[1].trim());
            var z2 = Integer.parseInt(endCoords[2].trim());

            return new CuboidIntXYZ(x1, y1, z1, x2, y2, z2);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static String getPlaceholder() {
        return "0, 0, 0 ~ 0, 0, 0";
    }

    public boolean includes(int x, int y, int z) {
        int xMin = this.x1;
        int xMax = this.x2;
        if (xMin > xMax) {
            xMin = this.x2;
            xMax = this.x1;
        }

        int yMin = this.y1;
        int yMax = this.y2;
        if (yMin > yMax) {
            yMin = this.y2;
            yMax = this.y1;
        }

        int zMin = this.z1;
        int zMax = this.z2;
        if (zMin > zMax) {
            zMin = this.z2;
            zMax = this.z1;
        }

        return (x >= xMin && x <= xMax &&
                y >= yMin && y <= yMax &&
                z >= zMin && z <= zMax);
    }
}
