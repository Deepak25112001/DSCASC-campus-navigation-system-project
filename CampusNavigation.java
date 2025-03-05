import java.util.*;

public class CampusNavigation {

    static class Node {
        String name;
        Map<String, Integer> neighbors;

        public Node(String name) {
            this.name = name;
            this.neighbors = new HashMap<>();
        }
    }

    static Map<String, Node> campusMap = new HashMap<>();

    static {
        // Initialize the campus map
        Node mainGate = new Node("Main Gate");
        Node library = new Node("Library");
        Node scienceBuilding = new Node("Science Building");
        Node cafeteria = new Node("Cafeteria");
        Node dormitory = new Node("Dormitory");

        mainGate.neighbors.put("Library", 5);
        mainGate.neighbors.put("Science Building", 10);

        library.neighbors.put("Main Gate", 5);
        library.neighbors.put("Cafeteria", 3);
        library.neighbors.put("Dormitory", 8);

        scienceBuilding.neighbors.put("Main Gate", 10);
        scienceBuilding.neighbors.put("Cafeteria", 2);

        cafeteria.neighbors.put("Library", 3);
        cafeteria.neighbors.put("Science Building", 2);
        cafeteria.neighbors.put("Dormitory", 7);

        dormitory.neighbors.put("Library", 8);
        dormitory.neighbors.put("Cafeteria", 7);

        campusMap.put("Main Gate", mainGate);
        campusMap.put("Library", library);
        campusMap.put("Science Building", scienceBuilding);
        campusMap.put("Cafeteria", cafeteria);
        campusMap.put("Dormitory", dormitory);
    }

    static class DijkstraResult {
        List<String> path;
        int distance;

        public DijkstraResult(List<String> path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    public static DijkstraResult dijkstra(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String location : campusMap.keySet()) {
            distances.put(location, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(end)) {
                List<String> path = new ArrayList<>();
                String temp = current;
                while (temp != null) {
                    path.add(temp);
                    temp = previous.get(temp);
                }
                Collections.reverse(path);
                return new DijkstraResult(path, distances.get(end));
            }

            Node currentNode = campusMap.get(current);
            for (Map.Entry<String, Integer> neighborEntry : currentNode.neighbors.entrySet()) {
                String neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                int alt = distances.get(current) + weight;
                if (alt < distances.get(neighbor)) {
                    distances.put(neighbor, alt);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return new DijkstraResult(null, Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter start location: ");
        String start = scanner.nextLine();
        System.out.print("Enter end location: ");
        String end = scanner.nextLine();

        DijkstraResult result = dijkstra(start, end);

        if (result.path != null) {
            System.out.println("Shortest path: " + String.join(" -> ", result.path));
            System.out.println("Distance: " + result.distance);
        } else {
            System.out.println("No path found.");
        }
        scanner.close();
    }
}