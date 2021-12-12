# An Interactive Genetic Algorithm For Linkage Structures:

### For Dr. G:

Hello! This repository should contain all necessary links and code for my final project. You'll find all the source code for this project in the ```/src/``` directory, and testing was done in the ```/test/``` directory. Begin in the directory ```/for_dr_g/```  which contains writeups for my my self-reflection, concept map, and a file containing a link to my code-walkthrough. I also included a file ```/for_dr_g/grade_template.md``` which summarizes the points I believe I attained towards my final grade with this project. 

## Introduction: 
This code generates linkage structures which can be manipulated in real time. A custom button class allows users to move around constrained linkage structures, and draw the traces of various linkage structures over time. The user creates an intuition for how linkage structures draw traces: to each linkage structure, there is a continuous curve which will be traced out when the structure is articulated. The user is then given the option to predefine their own continuous trace to be used as the target curve for a genetic algorithm. The genetic algorithm mutates a population of linkage structures whose fitness is based on the 'distance' from the target trace. The user can then view the evolution of the algorithm in real time. 

### Note: 
This project is an extension of two personal projects written in Processing and Python, github links here: https://github.com/oliver-adams-b/linkage_genetic_algorithm and https://github.com/oliver-adams-b/linkage_interactive. This implementation in Java is more developed than the prior two, and optimizations are made in the UI design and implementation of the genetic algorithm. 

Below is a simple UML-like diagram depicting the meat of this repository:
![Alt text](https://github.com/adamso-roux/5004_Final_Project/blob/main/misc/final_project_uml_diagram.png?raw=true "Title")


### Concept Map and Objective Reflection:

Note that I did not include all required sections for the inital concept map, as I did not include a testing plan or a UML diagram. For the final submission however, I have included both ! Below is what I submitted for the project design and objectives for this assignment: 

* #### Primary Goals:

* Create a linkage class which contains all of the structural information required for dynamically drawing linkage structures: setting lengths, anchor points, etc. 

* Create a very simple UI with buttons and sliders for moving and adjusting linkage structures. 

* Draw traces of any linkage structure drawn to the screen. Explanation: store the locations of the terminal point of a linkage structure and draw the path to the screen. 

* #### Secondary Goals:

* Compute the functional range of a linkage structure. Explanation: all linkage structures have a fixed range of motion, and computing the limit points on which the structure is defined is quite involved. Luckily, I have already written an implementation of this algorithm in python, so I need to translate it into Java. 

* Compute the trace of any linkage structure at initialization of the structure. Explanation: Different than storing the trace, we can use some cool math to recursively compute an approximate parametric relation for the trace of a structure. 

* #### Tertiary Goals:

* Implement genetic algorithm to grow linkage structures whose traces approximate a user-provided trace. Explanation: I've already done this in python, so it would just be a matter of writing my own optimizer in Java. This is definitely attainable, I know it will just take some time to actually implement. 
