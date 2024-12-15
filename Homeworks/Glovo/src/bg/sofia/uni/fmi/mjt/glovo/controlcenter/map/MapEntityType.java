package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    private final char typeOfEntity;

    MapEntityType(char c) {
        typeOfEntity = c;
    }

    public char getTypeOfEntity() {
        return typeOfEntity;
    }

    public static MapEntityType fromCharToMapEntityType(char ch) {
        return switch (ch) {
            case '.' -> MapEntityType.ROAD;
            case '#' -> MapEntityType.WALL;
            case 'R' -> MapEntityType.RESTAURANT;
            case 'C' -> MapEntityType.CLIENT;
            case 'A' -> MapEntityType.DELIVERY_GUY_CAR;
            case 'B' -> MapEntityType.DELIVERY_GUY_BIKE;
            default -> throw new IllegalArgumentException("No such map entity: " + ch);
        };
    }

    public static boolean contains(char ch) {
        for (MapEntityType entityType : MapEntityType.values()) {
            if (entityType.getTypeOfEntity() == ch) {
                return true;
            }
        }
        return false;
    }
}