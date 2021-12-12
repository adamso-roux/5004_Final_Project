## Introduction:
Here is an enumeration of concepts I used to complete this project, with citations to the file path and line number(s) where the concept was used. I believe I hit all the targets for the Basic and Intermediate concept demonstrations, but I only hit two out of the three targets for the 'Advanced' category: I was only able to create my own GUI from scratch and use recursion, and did not implement any of the sub-targets. I believe my 'researhed' topics cover this missed sub-target: using composition of lambda expressions and functional representations, and implementing a genetic algorithm on linkage structures. 

### Basic: 
*  #### Basic Java programming skills, 
    *   I would argue all files fit under this concept, see ```/src/view/*```
*  #### Standard variable usage and casting,
    *   I used double to int casting at ```/src/custom_buttons/movable_button.java``` at line 63. There are many more instances of casting, this is just one of them.
*  #### Basic class construction and use, 
    *  See the UML-like diagram in the ```README.md``` for this project to see an overview of the class constructions. 
*  #### Logical method use, 
    *  see the file ```/src/linkage_model/linkage_operations.java``` for a demonstration of abstract method composition
*  #### toString overriding, 
    * see the file ```/src/linkage_model/tuple.java``` at line 33 for an example of toString overriding. 

### Intermediate:
* #### Logical class construction and use, 
    * See the UML-like diagram in the ```README.md``` for this project to see an overview of the class constructions. 
* #### Data encapsulation, 
    * All classes in this repository use data encapsulation to prevent direct access to certain methods and instance variables.
* #### Testing, 
    * see the file ```/test/linkage_model_test.java``` for all the testing that was done for this project. The testing has 100% coverage over the ```/src/linkage_model/*``` directory, since all of these classes and methods need to be tested to ensure that things are working as expected. Coverage/testing was not applied in the case of the view.java and custom button class, since testing here is not feasible. 
* #### Exception handling, 
    * see the file ```/src/linkage_model/linkage.java``` at line 27 for an example of throwing an exception. There are more methods throughout this repository which throw exceptions, and instances in the view where these exceptions are handled: ```/src/view.java``` at line 319. 
* #### Inheritance/composition, 
    * All classes are either composed by or with other classes, and inheritance is used in the button package for the UI. See ```/src/custom_buttons/button.java``` for the button interface, and ```/src/custom_buttons/anchored_movable_button.java``` for a demonstration on composition 
* #### Dynamic dispatching/polymorphism, 
    * The custom buttons for the UI use polymorphism to implement the draw() method in different ways: ```/src/custom_buttons/toggle_button.java``` at line 61, ```/src/custom_buttons/movable_button.java``` at line 48 , ```/src/custom_buttons/anchored_movable_button.java``` at line 61. 
* #### Collection usage(arrays, linked lists, sets….), 
    * Arraylists are used throughout this project in many different scenarios, so I'll just list a few examples here: ```/src/linkage_model/linkage.java``` at line 22. See ```/src/view.java``` at lines 22, 23, 25. 
* #### Equality, 
    * No robust .equals() methods were used in this project, however the == operator was used many times throughout the project. 
* #### Comparison
    * Comparison was used throughout this project to ensure that instances of objects are consistent with expected values
### Advanced: 
* #### Double dispatching, 
* #### Recursion, 
    *  Recursion was used to calculate the base interval for a linkage by aligning the functional representation of a linkage: see ```/src/linkage_model/linkage_operations.java``` at line 149 for the recursion. 
* #### Linked list storage, 
* #### Hierarchical data storage, 
* #### MVC design, 
* #### GUI construction, 
    * For this project, I created and implemented my own very simple GUI: see the ```/src/view.java``` and ```/src/custom_buttons/*``` for the class objects and implementation of these objects. 
* #### The use of a software design pattern(discussed in class)

#### Researched: TWO topics not covered in class
* Implemented a genetic algorithm which grows linkage structures whose traces approximate a user-provided trace. See the file ```/src/linkage_model/genetic_simulation.java``` for an implementation of a simple genetic algorithm. 
* Used abstract lambda function composition recursively to compute the base interval of a linkage structure. See the file ```/src/linkage_model/linkage_operations.java``` at the method starting at line 111 for the location of where I used this concept. 
