# An Interactive Genetic Algorithm For Linkage Structures:

## Introduction: 
This code generates linkage structures which can be manipulated in real time. A custom button class allows users to move around constrained linkage structures, and draw the traces of various linkage structures over time. The user creates an intuition for how linkage structures draw traces: to each linkage structure, there is a continuous curve which will be traced out when the structure is articulated. The user is then given the option to predefine their own continuous trace to be used as the target curve for a genetic algorithm. The genetic algorithm mutates a population of linkage structures whose fitness is based on the 'distance' from the target trace. The user can then view the evolution of the algorithm in real time.

### Note: 
This project is an extension of two personal projects written in Processing and Python, github links here: https://github.com/oliver-adams-b/linkage_genetic_algorithm and https://github.com/oliver-adams-b/linkage_interactive. This implementation in Java is more developed than the prior two, and optimizations are made in the UI design and implementation of the genetic algorithm. 

![Alt text](https://github.com/adamso-roux/5004_Final_Project/blob/main/misc/final_project_uml_diagram.png?raw=true "Title")
