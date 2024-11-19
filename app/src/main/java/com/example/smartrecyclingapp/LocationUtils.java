package com.example.smartrecyclingapp;

import java.util.List;

public class LocationUtils {
    public static LocationMap findNearestLocation(double userLat, double userLon, List<LocationMap> locations) {
        LocationMap nearestLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (LocationMap location : locations) {
            double distance = calculateDistance(userLat, userLon, location.latitude, location.longitude);
            if (distance < minDistance) {
                minDistance = distance;
                nearestLocation = location;
            }
        }

        return nearestLocation;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Công thức Haversine để tính khoảng cách giữa 2 điểm
        final double R = 6371e3; // Bán kính Trái Đất (mét)
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Khoảng cách (mét)
    }
}

