// Import any package as required


import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class HuffmanSubmit implements Huffman {

    // Huffman Node
    public class HuffmanNode implements Comparable<HuffmanNode> {
        String binaryString;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;
        HuffmanNode root;


        public HuffmanNode(String binaryString, int frequency) {
            this.binaryString = binaryString;
            this.frequency = frequency;
        }

        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.left = left;
            this.right = right;
            this.frequency = left.frequency + right.frequency;
        }

        @Override
        public int compareTo(HuffmanNode o) {
            return this.frequency - o.frequency;
        }

        @Override
        public String toString() {
            return "HuffmanNode{" +
                    "binaryString='" + binaryString + '\'' +
                    ", frequency=" + frequency +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
  

 
	public void encode(String inputFile, String outputFile, String freqFile){
        // Generate character frequency
        HuffmanNode root = null;
        try {
            generateCharacterFrequency(inputFile, freqFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Build priority queue
        try {
            root= createPriorityQueue(freqFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create binary map
        Map<String, String> binaryMap = createBinaryMap(root);
        // Encode file using binary map
        try {
            encodeFile(inputFile, binaryMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Write encoded file to output file
        try {
            writeStringToBinaryFile(encodeFile(inputFile, binaryMap), outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
   }



   public void decode(String inputFile, String outputFile, String freqFile) {

   }


    // Read file and return file with character frequency
    public String generateCharacterFrequency(String inputFile, String freqFile) throws IOException {
        Map<String, Integer> frequencyMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            for (char c : line.toCharArray()) {
                String binaryString = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
                if (frequencyMap.containsKey(binaryString)) {
                    frequencyMap.put(binaryString, frequencyMap.get(binaryString) + 1);
                } else {
                    frequencyMap.put(binaryString, 1);
                }
            }
        }
        reader.close();
        FileOutputStream out = new FileOutputStream(freqFile);
        for (String binaryString : frequencyMap.keySet()) {
            int frequency = frequencyMap.get(binaryString);
            out.write((binaryString + ":" + frequency + "\n").getBytes());
        }
        out.close();
        return freqFile;
    }

    //create huffman tree in order of increasing frequency
        public HuffmanNode createPriorityQueue(String freqFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(freqFile));
        String line;
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(":");
            String binaryString = split[0];
            int frequency = Integer.parseInt(split[1]);
            priorityQueue.add(new HuffmanNode(binaryString, frequency));
        }
        reader.close();
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            priorityQueue.add(new HuffmanNode(left, right));
        }
        HuffmanNode root = priorityQueue.poll();
        return root;
    }

    // Create a map of character to binary string from huffman tree
    public Map<String, String> createBinaryMap(HuffmanNode root) {
        Map<String, String> binaryMap = new HashMap<>();
        createBinaryMap(root, binaryMap, "");
        return binaryMap;
    }

    // Recursive helper function for createBinaryMap
    public void createBinaryMap(HuffmanNode root, Map<String, String> binaryMap, String binaryString) {
        if (root.left == null && root.right == null) {
            binaryMap.put(root.binaryString, binaryString);
        } else {
            createBinaryMap(root.left, binaryMap, binaryString + "0");
            createBinaryMap(root.right, binaryMap, binaryString + "1");
        }
    }

    //use binary map to encode file and store as string
    public String encodeFile(String inputFile, Map<String, String> binaryMap) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            for (char c : line.toCharArray()) {
                String binaryString = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
                stringBuilder.append(binaryMap.get(binaryString));

            }
        }
        reader.close();
        String encodedString = stringBuilder.toString();

        return encodedString;
    }

    //take string and write to binary file using BinaryOut class
    public void writeStringToBinaryFile(String encodedString, String outputFile) throws IOException {
        BinaryOut binaryOut = new BinaryOut(outputFile);
        for (char c : encodedString.toCharArray()) {
            binaryOut.write(c == '1');
        }
        binaryOut.close();
    }



    public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("tamzy.txt", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }



}

