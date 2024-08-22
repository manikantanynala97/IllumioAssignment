import java.io.*;
import java.util.*;

public class FlowLogParser {
    private static final String FLOW_LOG_FILE = "flow_logs.txt";   // Name of the file containing flow logs
    private static final String LOOKUP_FILE = "lookup.csv";       // Name of the file containing lookup table
    private static final String OUTPUT_FILE = "output.txt";       // Name of the file where results will be written

    private Map<String, String> lookupTable = new HashMap<>();    // Stores the lookup table data
    private Map<String, Integer> tagCounts = new HashMap<>();      // Stores counts of each tag from the lookup table
    private Map<String, Integer> portProtocolCounts = new HashMap<>();  // Stores counts of each port/protocol combination
    private int untaggedCount = 0;                                // Counts the number of entries not found in the lookup table

    public static void main(String[] args) {
        FlowLogParser parser = new FlowLogParser();   // Create an instance of FlowLogParser
        parser.loadLookupTable();                    // Load the lookup table data
        parser.parseFlowLogs();                     // Parse the flow logs and count tags
        parser.writeOutput();                       // Write the results to an output file
    }

    // Load the lookup table from the CSV file
    private void loadLookupTable() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOOKUP_FILE))) {
            String line;
            reader.readLine(); // Skip header line in the CSV file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");    // Split the CSV line into parts
                if (parts.length == 3) {             // Ensure the line has exactly 3 parts
                    // Create a key from destination port and protocol (both in lowercase)
                    String key = parts[0].trim().toLowerCase() + "," + parts[1].trim().toLowerCase();
                    lookupTable.put(key, parts[2].trim());  // Store the key and tag in the lookup table
                }
            }
        } catch (IOException e) {
            e.printStackTrace();   // Print error if file reading fails
        }
    }

    // Parse the flow logs and count tag occurrences
    private void parseFlowLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FLOW_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Remove leading and trailing spaces
                String[] parts = line.split("\\s+");  // Split the line by whitespace
                if (parts.length >= 14) {  // Ensure there are at least 14 parts in the line
                    String dstPort = parts[6];                      // Extract destination port
                    String protocol = getProtocol(parts[7]);       // Convert protocol number to name
                    String key = dstPort.toLowerCase() + "," + protocol.toLowerCase();  // Create a key for lookup

                    // Get the tag from the lookup table or use "Untagged" if the key is not found
                    String tag = lookupTable.getOrDefault(key, "Untagged");
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);  // Update tag count

                    if (tag.equals("Untagged")) {
                        untaggedCount++;  // Increment untagged count if the tag is "Untagged"
                    }

                    String portProtocolKey = dstPort + "," + protocol;   // Create a key for port/protocol combination
                    portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);  // Update count for the combination
                }
            }
        } catch (IOException e) {
            e.printStackTrace();   // Print error if file reading fails
        }
    }

    // Convert protocol number to protocol name
    private String getProtocol(String protocolNumber) {
        switch (protocolNumber) {
            case "6":
                return "tcp";  // TCP protocol
            case "17":
                return "udp";  // UDP protocol
            case "1":
                return "icmp"; // ICMP protocol
            default:
                return protocolNumber;  // Return the protocol number if it is not recognized
        }
    }

    // Write the output counts to a file
    private void writeOutput() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
            writer.println("Tag Counts:");  // Header for tag counts
            writer.println("Tag,Count");    // CSV header for tag counts
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                if (!entry.getKey().equals("Untagged")) {
                    writer.println(entry.getKey() + "," + entry.getValue());  // Write tag counts excluding "Untagged"
                }
            }
            writer.println("Untagged," + untaggedCount);  // Write untagged count
            writer.println();

            writer.println("Port/Protocol Combination Counts:");  // Header for port/protocol counts
            writer.println("Port,Protocol,Count");  // CSV header for port/protocol counts
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());  // Write port/protocol combination counts
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print error if file writing fails
        }
    }
}
