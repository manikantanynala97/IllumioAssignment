# FlowLogParser

## Overview

The `FlowLogParser` is a Java application designed to parse VPC flow logs and classify network traffic based on predefined tags. It reads flow logs and a lookup table, processes the logs to count occurrences of various tags, and outputs the results to a file.

## Assumptions

- The program supports only the default flow log format (version 2).
- The lookup table must be in CSV format with no headers.
- The log file and lookup table must be named `flow_logs.txt` and `lookup.csv`, respectively.
- The program does not support custom log formats.
- Assuming the tagCount to be counted only according to the flow logs basically if the dstport,protocol key is found then basically count as tagged or else untagged.
- Assuming the port, portocol combination is between the dstport,protocol not srcport,protocol so basically I am taking it from parts[6] which is dstport and parts[7] which is protocol (you can see in the FlowLogParser.java and taking the reference from the amazon flow log records) 

## Requirements

- Java Development Kit (JDK) 8 or later

## Files

- `src/FlowLogParser.java`: The main Java source file containing the logic for parsing flow logs.
- `bin/FlowLogParser.class`: The compiled class file generated after compilation.
- `flow_logs.txt`: The input file containing the flow logs.
- `lookup.csv`: The input file containing the lookup table.
- `output.txt`: The output file where the results will be written (generated after running the program).
- `README.md`: This documentation file.

## Directory Structure

Project directory is organized as follows:

![screenshot1](https://github.com/user-attachments/assets/98def0e0-4840-4875-994d-ff3646cf6944)



## Compilation and Execution

1. **Compile the Java Program**

   Open a terminal in Visual Studio Code or any terminal application, and navigate to the `src` directory inside your project folder. Run the following command:

     javac -d ../bin FlowLogParser.java

2. **Run the Java Program**

     After compiling, navigate back to the root directory and run the program using the following command:

     java -cp bin FlowLogParser

     This command tells the Java Virtual Machine (JVM) to look for the FlowLogParser.class file in the bin directory and execute it.

3. **Output**

The results will be written to the output.txt file located in the root directory of the project.
