package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.PairOfTimeAndPathPrices;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.IncorrectLimitationValue;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapLayoutException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoPathFromClientToRestaurantException;
import bg.sofia.uni.fmi.mjt.glovo.node.Node;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType.fromCharToMapEntityType;

public class ControlCenter implements ControlCenterApi {
    private MapEntity[][] mapLayout;
    private int rows;
    private int columns;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int MAX_COUNT_ENTITIES_IN_MAP = 3;
    private static final double EPSILON = 0.00000001;

    public ControlCenter(char[][] mapLayout) {
        setMapLayout(mapLayout);
        rows = mapLayout.length;
        columns = mapLayout[0].length;
    }

    //checks for invalid map entities and creates MapEntity map
    private MapEntity[][] traverseMapLayout(char[][] mapLayout) {
        int rows = mapLayout.length;
        int columns = mapLayout[0].length;
        MapEntity[][] mapEntities = new MapEntity[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!MapEntityType.contains(mapLayout[i][j])) {
                    throw new InvalidMapLayoutException("There is an invalid map entity!");
                } else {
                    mapEntities[i][j] = new MapEntity(new Location(i, j), fromCharToMapEntityType(mapLayout[i][j]));
                }
            }
        }
        return mapEntities;
    }

    private void setMapLayout(char[][] mapLayout) {
        if (mapLayout == null) {
            throw new InvalidMapLayoutException("The map layout cannot be null");
        }
        this.mapLayout = traverseMapLayout(mapLayout);
    }

    //finds path from restaurant to client
    //returns -1 if there is no available path
    private int findPathToClient(Map<MapEntity, Integer> paths) {
        for (Map.Entry<MapEntity, Integer> entity : paths.entrySet()) {
            if (entity.getKey().type().equals(MapEntityType.CLIENT)) {
                int pathToClient = entity.getValue();
                paths.remove(entity.getKey());
                return pathToClient;
            }
        }
        return -1;
    }

    //add the current entity to the queue
    //replaces the existing one if the current one is closer
    private void addCurrentEntity(
            Location location, MapEntityType type,
            Map<MapEntity, Integer> mapPaths,
            Integer dist) {

        MapEntity currentEntity = new MapEntity(location, type);
        boolean alreadyHaveThisType = false;
        for (Map.Entry<MapEntity, Integer> entity : mapPaths.entrySet()) {
            if (entity.getKey().type().equals(type)) {
                alreadyHaveThisType = true;
                if (entity.getValue() > dist) {
                    mapPaths.remove(entity.getKey());
                }
            }
        }
        if (!alreadyHaveThisType) {
            mapPaths.putIfAbsent(currentEntity, dist);
        }
    }

    //checks the entity type of the current location and add it if possible
    private void inspectLocation(Location currLocation, Map<MapEntity, Integer> mapPaths, int dist, Location client) {
        MapEntityType entityType = mapLayout[currLocation.x()][currLocation.y()].type();
        if (entityType == MapEntityType.DELIVERY_GUY_BIKE || entityType == MapEntityType.DELIVERY_GUY_CAR) {
            addCurrentEntity(currLocation, entityType, mapPaths, dist);
        } else if (currLocation.x() == client.x() && currLocation.y() == client.y()) {
            addCurrentEntity(currLocation, entityType, mapPaths, dist);
        }
    }

    //checks if the location is in the borders of the map
    private boolean isInTheBorder(int x, int y) {
        return (x >= 0 && x < rows) && (y >= 0 && y < columns);
    }

    private boolean isReachable(MapEntityType mapEntityType) {
        return mapEntityType != MapEntityType.WALL;
    }

    private boolean isValidMove(int nextX, int nextY, boolean[][] visited) {
        return isInTheBorder(nextX, nextY)
                && isReachable(mapLayout[nextX][nextY].type())
                && !visited[nextX][nextY];
    }

    private void findPathsFromRestaurantToClientAndDeliveryGuys(
            Map<MapEntity, Integer> mapPaths,
            Location restaurant,
            Location client) {

        int xCoordRestaurant = restaurant.x();
        int yCoordRestaurant = restaurant.y();

        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(xCoordRestaurant, yCoordRestaurant, 0));
        boolean[][] visited = new boolean[rows][columns];
        visited[xCoordRestaurant][yCoordRestaurant] = true;
        while (!queue.isEmpty() && mapPaths.size() != MAX_COUNT_ENTITIES_IN_MAP) {
            Node current = queue.poll();
            inspectLocation(new Location(current.x(), current.y()), mapPaths, current.destination(), client);
            for (int[] direction : DIRECTIONS) {
                int nextX = current.x() + direction[0];
                int nextY = current.y() + direction[1];
                if (isValidMove(nextX, nextY, visited)) {
                    visited[nextX][nextY] = true;
                    queue.add(new Node(nextX, nextY, current.destination() + 1));
                }
            }
        }
    }

    private void checkForInvalidPriceAndTime(double maxPrice, int maxTime) {
        if (maxPrice < 0 && Math.abs((-1) - maxPrice) >= EPSILON) {
            throw new IncorrectLimitationValue("Incorrect price limitation value!");
        }

        if (maxTime != -1 && maxTime < 0) {
            throw new IncorrectLimitationValue("Incorrect time limitation value!");
        }
    }

    private void checkForIllegalArguments(Location clientLoc, Location restaurantLoc, ShippingMethod shippingMethod) {
        if (clientLoc == null) {
            throw new IllegalArgumentException("The client can not be null!");
        }

        if (restaurantLoc == null) {
            throw new IllegalArgumentException("The restaurant can not be null!");
        }

        if (shippingMethod == null) {
            throw new IllegalArgumentException("The shipping method can not be null!");
        }
    }

    private void checkForInvalidOrder(Location clientLocation, Location restaurantLocation) {
        if (!isInTheBorder(clientLocation.x(), clientLocation.y())) {
            throw new InvalidOrderException("The client's location is outside the borders of the map!");
        }

        if (!isInTheBorder(restaurantLocation.x(), restaurantLocation.y())) {
            throw new InvalidOrderException("The restaurant's location is outside the borders of the map!");
        }

        MapEntityType locationOfClientOnLayout = mapLayout[clientLocation.x()][clientLocation.y()].type();
        if (locationOfClientOnLayout != MapEntityType.CLIENT) {
            throw new InvalidOrderException("There is no client at the specified location!");
        }

        MapEntityType locationOfRestaurantOnLayout = mapLayout[restaurantLocation.x()][restaurantLocation.y()].type();
        if (locationOfRestaurantOnLayout != MapEntityType.RESTAURANT) {
            throw new InvalidOrderException("There is no restaurant at the specified location!");
        }
    }

    //converts the map of mapEntity and Integer to map of mapEntity and a pair of estimated time and price
    private Map<MapEntity, PairOfTimeAndPathPrices> convertMapValuesToPairsOfTimeAndPrice(
            Map<MapEntity, Integer> paths,
            int pathToClientFromRestaurant) {

        Map<MapEntity, PairOfTimeAndPathPrices> mapOfPairs = new LinkedHashMap<>();
        int price;
        int estimatedTime;
        for (Map.Entry<MapEntity, Integer> entity : paths.entrySet()) {
            DeliveryType deliveryType;
            if (entity.getKey().type().equals(MapEntityType.DELIVERY_GUY_CAR)) {
                deliveryType = DeliveryType.CAR;
            } else {
                deliveryType = DeliveryType.BIKE;
            }

            price = (pathToClientFromRestaurant + entity.getValue()) * deliveryType.getPricePerKilometer();
            estimatedTime = (pathToClientFromRestaurant + entity.getValue()) * deliveryType.getTimePerKilometer();
            mapOfPairs.put(entity.getKey(), new PairOfTimeAndPathPrices(estimatedTime, price));
        }

        return mapOfPairs;
    }

    private Map.Entry<MapEntity, PairOfTimeAndPathPrices> checkFinalDeliveryGuyChoice(
            double maxPrice,
            int maxTime,
            Map.Entry<MapEntity, PairOfTimeAndPathPrices> bestDeliveryGuy) {

        if (bestDeliveryGuy == null ||
                (Math.abs((-1) - maxPrice) >= EPSILON && bestDeliveryGuy.getValue().price() > maxPrice)
                || (maxTime != -1 && bestDeliveryGuy.getValue().estimatedTime() > maxTime)) {
            return null;
        }

        return bestDeliveryGuy;
    }

    private boolean isBetterChoice(Map.Entry<MapEntity, PairOfTimeAndPathPrices> currentChoice,
                                   Map.Entry<MapEntity, PairOfTimeAndPathPrices> newDeliveryGuy,
                                   int maxTime, double maxPrice, ShippingMethod shippingMethod) {
        PairOfTimeAndPathPrices currentChoiceValue = currentChoice == null ? null : currentChoice.getValue();
        PairOfTimeAndPathPrices newDeliveryGuyValue = newDeliveryGuy.getValue();

        if (shippingMethod.equals(ShippingMethod.FASTEST)) {
            return (maxTime == -1 || newDeliveryGuyValue.estimatedTime() <= maxTime) &&
                    (Math.abs((-1) - maxPrice) < EPSILON || newDeliveryGuyValue.price() <= maxPrice) &&
                    (currentChoiceValue == null ||
                            newDeliveryGuyValue.estimatedTime() < currentChoiceValue.estimatedTime());
        } else {
            return (maxTime == -1 || newDeliveryGuyValue.estimatedTime() <= maxTime) &&
                    (Math.abs((-1) - maxPrice) < EPSILON || newDeliveryGuyValue.price() <= maxPrice) &&
                    (currentChoiceValue == null || newDeliveryGuyValue.price() < currentChoiceValue.price());
        }
    }

    private Map.Entry<MapEntity, PairOfTimeAndPathPrices> findBestDeliveryGuy(
            Map<MapEntity, PairOfTimeAndPathPrices> deliveryGuysMap,
            double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        Map.Entry<MapEntity, PairOfTimeAndPathPrices> bestDeliveryGuy = null;
        for (Map.Entry<MapEntity, PairOfTimeAndPathPrices> entity : deliveryGuysMap.entrySet()) {
            if (isBetterChoice(bestDeliveryGuy, entity, maxTime, maxPrice, shippingMethod)) {
                bestDeliveryGuy = entity;
            }
        }
        return checkFinalDeliveryGuyChoice(maxPrice, maxTime, bestDeliveryGuy);
    }

    private DeliveryType toDeliveryType(MapEntityType mapEntityType) {
        return mapEntityType.getTypeOfEntity() == 'A' ? DeliveryType.CAR : DeliveryType.BIKE;
    }

    //Throws IllegalArgumentException if restaurant location, client location or shipping method are null
    //Throws NoPathFromClientToRestaurantException if there is no available path from restaurant to client
    @Override
    public DeliveryInfo findOptimalDeliveryGuy(
            Location restaurantLocation, Location clientLocation,
            double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        checkForIllegalArguments(clientLocation, restaurantLocation, shippingMethod);
        checkForInvalidOrder(clientLocation, restaurantLocation);
        checkForInvalidPriceAndTime(maxPrice, maxTime);
        Map<MapEntity, Integer> paths = new HashMap<>();
        findPathsFromRestaurantToClientAndDeliveryGuys(paths, restaurantLocation, clientLocation);
        int pathToClientFromRestaurant = findPathToClient(paths);
        if (pathToClientFromRestaurant == -1) {
            throw new NoPathFromClientToRestaurantException("There is no path from restaurant to client!");
        } else if (paths.isEmpty()) {
            return null;
        }
        Map<MapEntity, PairOfTimeAndPathPrices> convertedMap = convertMapValuesToPairsOfTimeAndPrice(
                paths, pathToClientFromRestaurant);

        Map.Entry<MapEntity, PairOfTimeAndPathPrices> bestDeliveryGuy = findBestDeliveryGuy(
                convertedMap, maxPrice,
                maxTime, shippingMethod);

        return bestDeliveryGuy == null ?
                null : new DeliveryInfo(bestDeliveryGuy.getKey().location(), bestDeliveryGuy.getValue().price(),
                bestDeliveryGuy.getValue().estimatedTime(), toDeliveryType(bestDeliveryGuy.getKey().type()));
    }

    @Override
    public MapEntity[][] getLayout() {
        return mapLayout;
    }
}
