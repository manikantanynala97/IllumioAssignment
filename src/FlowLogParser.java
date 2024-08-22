import java.io.*;
import java.util.*;

public class FlowLogParser {
    private static final String FLOW_LOG_FILE = "flow_logs.txt";
    private static final String LOOKUP_FILE = "lookup.csv";
    private static final String OUTPUT_FILE = "output.txt";

    private Map<String, String> lookupTable = new HashMap<>();
    private Map<String, Integer> tagCounts = new HashMap<>();
    private Map<String, Integer> portProtocolCounts = new HashMap<>();
    private int untaggedCount = 0;

    public static void main(String[] args) {
        FlowLogParser parser = new FlowLogParser();
        parser.loadLookupTable();
        parser.parseFlowLogs();
        parser.writeOutput();
    }

    // Load the lookup table from the CSV file
    private void loadLookupTable() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOOKUP_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = parts[0].trim().toLowerCase() + "," + parts[1].trim().toLowerCase();
                    lookupTable.put(key, parts[2].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Parse the flow logs and count tag occurrences
    private void parseFlowLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FLOW_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Remove leading and trailing spaces
                String[] parts = line.split("\\s+");  // Split the line by whitespace
                if (parts.length >= 14) {
                    String dstPort = parts[6];
                    String protocol = getProtocol(parts[7]);
                    String key = dstPort.toLowerCase() + "," + protocol.toLowerCase();

                    String tag = lookupTable.getOrDefault(key, "Untagged");
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                    if (tag.equals("Untagged")) {
                        untaggedCount++;
                    }

                    String portProtocolKey = dstPort + "," + protocol;
                    portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convert protocol number to protocol name
    private String getProtocol(String protocolNumber) {
        switch (protocolNumber) {
            case "6":
                return "tcp";
            case "17":
                return "udp";
            case "1":
                return "icmp";
            default:
                return protocolNumber;
        }
    }

    // Write the output counts to a file
    private void writeOutput() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
            writer.println("Tag Counts:");
            writer.println("Tag,Count");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                if (!entry.getKey().equals("Untagged")) {
                    writer.println(entry.getKey() + "," + entry.getValue());
                }
            }
            writer.println("Untagged," + untaggedCount);
            writer.println();

            writer.println("Port/Protocol Combination Counts:");
            writer.println("Port,Protocol,Count");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
