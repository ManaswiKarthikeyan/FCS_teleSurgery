import java.util.*;

class DataFrame {
    private String data;
    private int fcs;

    public DataFrame(String data) {
        this.data = data;
        this.fcs = calculateFCS(data);
    }

    public String getData() {
        return data;
    }

    public int getFcs() {
        return fcs;
    }

    // Simple FCS calculation using sum of character codes
    public static int calculateFCS(String data) {
        int sum = 0;
        for (char c : data.toCharArray()) {
            sum += (int) c;
        }
        return sum % 256; // Mod 256 to simulate 1-byte FCS
    }
}

public class TeleSurgeryFCS {
    static final double ERROR_PROBABILITY = 0.2;
    static Random rand = new Random();

    public static String transmitFrame(String frameData) {
        char[] data = frameData.toCharArray();
        if (rand.nextDouble() < ERROR_PROBABILITY) {
            int index = rand.nextInt(data.length);
            data[index] = (char)(data[index] ^ 0xFF); // flip bits to simulate error
            System.out.println("ERROR: Corruption introduced at byte index " + index);
        }
        return new String(data);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Advanced Tele-Surgery FCS Simulation ===");
        System.out.print("Enter number of frames to simulate: ");
        int numFrames = sc.nextInt();
        sc.nextLine(); // consume newline

        List<DataFrame> frames = new ArrayList<>();
        for (int i = 0; i < numFrames; i++) {
            System.out.print("Enter data for Frame " + (i + 1) + ": ");
            String data = sc.nextLine();
            frames.add(new DataFrame(data));
        }

        int totalRetransmissions = 0;
        int successfulFrames = 0;

        for (int i = 0; i < frames.size(); i++) {
            DataFrame frame = frames.get(i);
            int attempts = 0;
            boolean success = false;

            System.out.println("\nSending Frame: " + frame.getData());
            System.out.println("Sender FCS: " + frame.getFcs());

            while (!success) {
                attempts++;
                String receivedData = transmitFrame(frame.getData());
                int receiverFCS = DataFrame.calculateFCS(receivedData);

                System.out.println("Received Frame Data: " + receivedData);
                System.out.println("Receiver FCS: " + receiverFCS);

                if (receiverFCS == frame.getFcs()) {
                    System.out.println("SUCCESS: Frame transmitted correctly on attempt " + attempts);
                    success = true;
                    successfulFrames++;
                } else {
                    System.out.println("ERROR: Frame corrupted. Retransmission required.");
                }
            }

            totalRetransmissions += (attempts - 1);
        }

        // Summary
        System.out.println("\n=== Transmission Summary ===");
        System.out.println("Total Frames       : " + numFrames);
        System.out.println("Successful Frames  : " + successfulFrames);
        System.out.println("Retransmissions    : " + totalRetransmissions);

        // Text-based bar chart
        System.out.println("\nTransmission Visualization:");
        System.out.print("Successful Frames: ");
        for (int i = 0; i < successfulFrames; i++) System.out.print("■");
        System.out.println();

        System.out.print("Retransmissions  : ");
        for (int i = 0; i < totalRetransmissions; i++) System.out.print("■");
        System.out.println();
    }
}
