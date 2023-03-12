// Import any package as required


public class HuffmanSubmit implements Huffman {
  

 
	public void encode(String inputFile, String outputFile, String freqFile){
        //use tree to encode
        readFreq(inputFile, freqFile);
        Node root = buildTree(freqFile);
        String[] codes = new String[256];
        //build codes for each char and store in codes array
        buildCodes(root, codes, "");
        //read file and encode
        String s = readBinary(inputFile);
        String encoded = "";
        for(int i = 0; i < s.length(); i++){
            encoded += codes[s.charAt(i)];
        }
        //write encoded file in binary using binaryOut
        BinaryOut out = new BinaryOut(outputFile);
        for(int i = 0; i < encoded.length(); i++){
            out.write(encoded.charAt(i) == '1');
        }
        out.close();
   }


   public void decode(String inputFile, String outputFile, String freqFile){
        //read freq table and build tree
        Node root = buildTree(freqFile);
        //read file and decode
        String s = readBinary(inputFile);
        String decoded = "";
        Node temp = root;

        }
        //write decoded file in binary using binaryOut
        BinaryOut out = new BinaryOut(outputFile);
        for(int i = 0; i < decoded.length(); i++){
            out.write(decoded.charAt(i) == '1');
        }
        out.close();

   }

   //read file and store as binary string
    public String readBinary(String input){
         try{
              FileInputStream in = new FileInputStream(input);
              int c;
              String s = "";
              while((c = in.read()) != -1){
                String temp = Integer.toBinaryString(c);
                while(temp.length() < 8){
                     temp = "0" + temp;
                }
                s += temp;
              }
              in.close();
              return s;
         }catch(IOException e){
              System.out.println("Error: " + e.getMessage());
              return null;
         }
    }

   //read file and write freq table
    public void readFreq(String input, String freqFile){
        try{
            FileInputStream in = new FileInputStream(input);
            FileOutputStream out = new FileOutputStream(freqFile);
            int[] freq = new int[256];
            int c;
            while((c = in.read()) != -1){
                freq[c]++;
            }
            for(int i = 0; i < 256; i++){
                if(freq[i] != 0){
                    String s = Integer.toBinaryString(i);
                    while(s.length() < 8){
                        s = "0" + s;
                    }
                    s += ":" + freq[i] + "\n";
                    out.write(s.getBytes());
                }
            }
            in.close();
            out.close();
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    //read freq table and build huffman tree
    public Node buildTree(String freqFile){
        try{
            FileInputStream in = new FileInputStream(freqFile);
            int c;
            String s = "";
            while((c = in.read()) != -1){
                s += (char)c;
            }
            String[] lines = s.split("\n");
            Node[] nodes = new Node[lines.length];
            for(int i = 0; i < lines.length; i++){
                String[] line = lines[i].split(":");
                nodes[i] = new Node(Integer.parseInt(line[1]), Integer.parseInt(line[0], 2));
            }
            in.close();
            return buildTree(nodes);
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //build codes
    public void buildCodes(Node root, String[] codes, String code){
        if(root == null){
            return;
        }
        if(root.left == null && root.right == null){
            codes[root.c] = code;
            return;
        }
        buildCodes(root.left, codes, code + "0");
        buildCodes(root.right, codes, code + "1");
    }







   public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}
