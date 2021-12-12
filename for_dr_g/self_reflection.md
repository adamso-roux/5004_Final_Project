## Self Reflection:

Initially, I did not submit a good project design plan, all I did was list out my goals and I did not mention my testing plan nor did I include a class diagram for my project. Now, I have included a class diagram in this repository so that you can see how the project is laid out (see below). I also included a testing suite for this project, which I actually used to implement and write a large portion of the mathematically heavy portions of this code. I was able to get 100% coverage on all of the files within the ```/src/linkage_mode/*``` package. I was unable to successfully test the GUI and view of my project, but while running the program one can see that the GUI does indeed work thanks to the view (even if its a little buggy!).

I believe I spent more time on this project that I had initially planned, I just got really invested in getting the genetic algorithm to work well enough, and to get everything running in a loop. I was also really fascinated with using lambda expressions in java, and I went off the deep end a little bit in the ```linkage_operations.java``` file. 


#### On my goals: 
I was able to hit all of my goals, including the tertiary goal. I was able to make a GUI through which the user interacts with various linkage objecst, draws traces, and so on. I was able to implement a class of static methods which compute the functional range of an arbitrary linkage structure, and I was able to use this computation at the initialization of all linkage structures. My tertiary goal was to grow a set of linkage structures over time which 'learn' to approximate a target curve. I believe I went *further* than this, by allowing the user to input their own trace while the program is running, and to allow this trace to be updated and the algorithm to be rerun during the draw() loop of the GUI. Below is exactly what I submitted for the the project design section of this assignment, enumerating my goals. 

* #### Primary Goals:
    * Create a linkage class which contains all of the structural information required for dynamically drawing linkage structures: setting lengths, anchor points, etc. 
    * Create a very simple UI with buttons and sliders for moving and adjusting linkage structures. 
    * Draw traces of any linkage structure drawn to the screen. Explanation: store the locations of the terminal point of a linkage structure and draw the path to the screen. 

* #### Secondary Goals:
    * Compute the functional range of a linkage structure. Explanation: all linkage structures have a fixed range of motion, and computing the limit points on which the structure is defined is quite involved. Luckily, I have already written an implementation of this algorithm in python, so I need to translate it into Java. 
    * Compute the trace of any linkage structure at initialization of the structure. Explanation: Different than storing the trace, we can use some cool math to recursively compute an approximate parametric relation for the trace of a structure. 

* #### Tertiary Goals:
    * Implement genetic algorithm to grow linkage structures whose traces approximate a user-provided trace. Explanation: I've already done this in python, so it would just be a matter of writing my own optimizer in Java. This is definitely attainable, I know it will just take some time to actually implement. 


![Alt text](https://github.com/adamso-roux/5004_Final_Project/blob/main/misc/final_project_uml_diagram.png?raw=true "Title")
