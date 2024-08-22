# FlowLogParser

## Overview

The `FlowLogParser` is a Java application designed to parse VPC flow logs and classify network traffic based on predefined tags. It reads flow logs and a lookup table, processes the logs to count occurrences of various tags, and outputs the results to a file.

## Assumptions

- The program supports only the default flow log format (version 2).
- The lookup table must be in CSV format with no headers.
- The log file and lookup table must be named `flow_logs.txt` and `lookup.csv`, respectively.
- The program does not support custom log formats.
- Assuming the tagCount to be counted only according to the flow logs basically if the dstport,protocol key is found then basically count as tagged or else untagged
- Assuming the port,portocol combination count to be taken only from the flow logs.
- Assuming the port, portocol combination is between the dstport,protocol not srcport,protocol so basically I am taking it from parts[6] which is dstport and parts[7] which is protocol (you can see in the FlowLogParser.java and has taken the reference from the amazon flow log records -> https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html) 

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


## Approach to the problem

 
 `The FlowLogParser program is basically a tool that crunches through network logs and makes sense of them. It starts by loading a lookup table that matches ports and protocols to specific tags. Then it goes through a big log file, line by line, figuring out what each network connection is about based on that lookup table. As it goes, it keeps track of how many times it sees each tag and each port-protocol combo. The clever part is how it uses hashmaps to do this counting super fast, even with huge log files. At the end, it spits out a neat report that shows you what kinds of network traffic you've got going on. The whole thing is set up to be pretty flexible - you can easily update the lookup table or tweak how it categorizes things without messing with the core code. It's a smart way to get a quick overview of network activity without drowning in details.`


## Testing and Assumptions

`General Test Cases`

The program has been tested with various general scenarios, and the outputs were found to be correct, considering the assumptions.
Edge Cases Considered

`Additional Test Cases`

`Empty Lookup Table:`
If lookup.csv is empty, all entries in flow_logs.txt should be tagged as "Untagged."

`Empty Flow Log File:`
If flow_logs.txt is empty, the output should consist only of headers with no additional data.

`Different Protocols:`
Logs with different protocols (e.g., ICMP, IGMP) were tested to ensure they are handled correctly by the program.



## Compilation and Execution

1. **Compile the Java Program**

   Open a terminal in Visual Studio Code or any terminal application, and navigate to the `src` directory inside your project folder. Run the following command:

    ```
      javac -d ../bin FlowLogParser.java
    ```


1. **Run the Java Program**

     After compiling, navigate back to the `root` directory and run the program using the following command:

      ```
        java -cp bin FlowLogParser
      ```

     This command tells the Java Virtual Machine (JVM) to look for the FlowLogParser.class file in the bin directory and execute it.

3. **Output**

     The results will be written to the `output.txt` file located in the `root` directory of the project.
