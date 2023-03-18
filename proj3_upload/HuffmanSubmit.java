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
        public int totalChar=0;
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

        // Generates character frequency
        HuffmanNode root = null;
        try {
            generateCharacterFrequency(inputFile, freqFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Builds priority queue
        try {
            root= createPriorityQueue(freqFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Creates binary map
        Map<String, String> binaryMap = createBinaryMap(root);
        // Encodes file using binary map
        // Writes encoded file to output file
        try {
            writeStringToBinaryFile(encodeFile(inputFile, binaryMap), outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
   }



   public void decode(String inputFile, String outputFile, String freqFile) {
        //creates prioty queue
        HuffmanNode root = null;
        try{
            root = createPriorityQueue(freqFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //creates binary map
        Map<String, String> binaryMap = createBinaryMap(root);
        //decode file
       try{
           decodeFile(inputFile, outputFile, binaryMap);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   //calculates number of characters from freq file
    public int totalCharFreq(String freqFile) throws IOException {
         int totalChars = 0;
         String readline;
         BufferedReader in = new BufferedReader(new FileReader(freqFile));
         while ((readline = in.readLine()) != null) {
              String[] split = readline.split(":");
              totalChars += Integer.parseInt(split[1]);
         }
         return totalChars;
    }

    // Reads file and return file with character frequency
    public String generateCharacterFrequency(String inputFile, String freqFile) throws IOException {
        Map<String, Integer> frequencyMap = new HashMap<>();
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        int b;
        while ((b = inputStream.read()) != -1) {
            //convert` byte to binary
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            if (frequencyMap.containsKey(binaryString)) {
                frequencyMap.put(binaryString, frequencyMap.get(binaryString) + 1);
            } else {
                frequencyMap.put(binaryString, 1);
            }




        }

        inputStream.close();

        FileOutputStream out = new FileOutputStream(freqFile);
        for (String binaryString : frequencyMap.keySet()) {
            int frequency = frequencyMap.get(binaryString);
            out.write((binaryString + ":" + frequency + "\n").getBytes());
        }
        out.close();
        return freqFile;
    }


    //creates huffman tree in order of increasing frequency
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

    // Creates a map of character to binary string from huffman tree
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

    //uses binary map to encode file and store as string
    public String encodeFile(String inputFile, Map<String, String> binaryMap) throws IOException {
        //uses readBinaryFileToString to read file as string
        StringBuilder binaryString = new StringBuilder();
        String fileString = readBinaryFileToString(inputFile);
        StringBuilder stringBuilder = new StringBuilder();
        char[] split = fileString.toCharArray();
        for (char c : split) {
            binaryString.append(c);
            if (binaryMap.containsKey(binaryString.toString())) {
                stringBuilder.append(binaryMap.get(binaryString.toString()));
                binaryString = new StringBuilder();
            }
        }
        return stringBuilder.toString();
    }


    //takes string and writes to binary file using BinaryOut class
    public void writeStringToBinaryFile(String encodedString, String outputFile) throws IOException {
        BinaryOut binaryOut = new BinaryOut(outputFile);
        StringBuilder written = new StringBuilder();
        int i = 0;

        while (i < encodedString.length()) {
            char c = encodedString.charAt(i);
            binaryOut.write(c == '1');
            written.append(c);

            i++;
        }
        binaryOut.flush();
        binaryOut.close();

    }


    //reads file and returns string of binary
    public static String readBinaryFileToString(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder sb = new StringBuilder();
        while ((bytesRead = fis.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                sb.append(String.format("%8s", Integer.toBinaryString(buffer[i] & 0xFF)).replace(' ', '0'));
            }
        }
        fis.close();
        return sb.toString();
    }


    //decodes file using binary map
    public void decodeFile(String inputFile, String outputFile, Map<String, String> binaryMap) throws IOException {
        String encodedString = readBinaryFileToString(inputFile);
        StringBuilder stringBuilder = new StringBuilder();
        String binaryString = "";

        int totalChar = totalCharFreq("freq.txt");
        char[] split = encodedString.toCharArray();
        int i = 0;
            for (char c : split) {
                binaryString += c;
                if (i == totalChar) {
                    break;
                }
                if (binaryMap.containsValue(binaryString)) {
                    for (String key : binaryMap.keySet()) {
                        if (binaryMap.get(key).equals(binaryString)) {
                            stringBuilder.append(key);
                            binaryString = "";
                            i++;

                    }
                }
            }

        }
        String decodedString = stringBuilder.toString();

        writeStringToBinaryFile(decodedString, outputFile);
    }



    public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("alice30.txt", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");

   }



}

