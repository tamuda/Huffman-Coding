Huffman Coding


This code implements Huffman coding, a lossless data compression algorithm that uses variable-length codes to represent data. It is written in Java.


Created by: 
Tamuda Chimhanda (tchimhan@u.rochester.edu 
Ameenul Haque (ahaque@u.rochester.edu)


Usage
The Huffman coding algorithm implemented in this code can encode and decode files.


To encode a file, call the encode method of the HuffmanSubmit class and pass in the input file path, output file path, and the path to the file where the character frequency will be stored.


Example usage:


HuffmanSubmit huffman = new HuffmanSubmit();
huffman.encode("input.txt", "output.bin", "freq.txt");


To decode a file, call the decode method of the HuffmanSubmit class and pass in the input file path, output file path, and the path to the file where the character frequency is stored.


Example usage:

HuffmanSubmit huffman = new HuffmanSubmit();
huffman.decode("output.bin", "decoded.txt", "freq.txt");


Implementation


1. Huffman Node
The HuffmanNode class represents a node in the Huffman tree. It contains the binary representation of the character, its frequency, pointers to the left and right child nodes, and a pointer to its root node.


2. Generate Character Frequency
The generateCharacterFrequency method reads an input file and generates a map of character frequency. It then writes the character frequency to a file specified by the user.


3. Create Priority Queue
The createPriorityQueue method reads the file containing the character frequency and creates a priority queue of HuffmanNode objects in order of increasing frequency. It then constructs the Huffman tree.


4. Create Binary Map
The createBinaryMap method creates a map of characters to their binary representation in the Huffman tree.


5. Encode File
The encodeFile method uses the binary map to encode the input file and returns the encoded file as a string.


6. Decode File
The decodeFile method reads the encoded file and decodes it using the binary map, writing the result to an output file.


7. Print Huffman Tree
The printTree method prints the Huffman tree for debugging purposes.