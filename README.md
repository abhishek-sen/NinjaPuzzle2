
This is an implementation of "The Kung Fu Packing Crate Maze" (http://www.puzzlebeast.com/crate/) game using Java (1.6)
It is a typical example of state space exploratory game.


LIBRARIES
---------

Libraries necessary to compile the software :

- JGrapht 5.13 : An open-source Java Graph Data Structure Library (http://jgrapht.org/)
- Google-GSon 2.2: An open-source Java library for managing JSon files (https://code.google.com/p/google-gson/)

Running the code :
-----------------

I have implemented and tested the code from IntelliJ IDE and didn't find enough time to test the code by running from command line.
At this point, we need to change the input game configuration file by modifying the variables "configFile" and "outFile" (defined on main.java) with appropriate file path.

Output format :
--------------

{ "Path": [ [Path1], [Path2],... [PathN] ]}

where, [Path1] = [ [r1,c1], [r2,c2], .., [rM,cM] ] etc.  ([r,c] = [row value, column value])



Overview of the solution
------------------------

Overall problem of path-finding through grid cells have been cast as a graph search problem.

Data-structures involved :

- GameGraph : We defined a directed graph over the maze. Each vertex is a graph corresponds to a grid-cell. We connect grid cells iff
there are crates (standing or toppled) in both these cells. Code for managing the data structure is present in
"GameGraph.java"

- VisitedMap : This is an M x N binary matrix (M x N = dimension of grid). Look at "VisitedMap.java"

- Constraint Map : This is an M x N matrix with integer entries. If a grid cell is unreachable the corresponding
entry at the matrix is -1. If there is a standing crate the entry at the constraint matrix is positive. For toppled crate
the value of the matrix element is 0. Look at "ConstraintMap.java"

- Path Map : An ArrayList that stores the list of node previously visited while exploring the current path. We print out this
list once we reach the destination node.


- All the above three data structures together constitute a game state (look at "GameState.java")

Initialization :
----------------

Create an instance of game state, by initializing the data structures appropriately. Look at "GameState.java" constructor
for details.

Game Play :
-----------

Game play starts by calling the GameEngine.runGameEngine() method which bootstraps an instance of "GameState" and starts the
game.

For the game play the bot :

1) topples a (standing) crate (GameState.ExpandGame())

During this process the GameGraph gets updated by adding edges between grids that may be accessed. Constraint matrix is updated
accordingly by looking at the location of toppled crate. Once this expansion is complete, the bot starts moving (explores
the graph)

OR

2) make a move (GameState.ExploreGame())

During this process the bot clones a new instance of game state since it wants to hallucinate a new move. (Recall that we
want to list all possible path). In this hallucinated game-state the bot sets the current node as "Visited" and moves to
any of it's neighbouring nodes. If this current node is the "destination node", the algorithm terminates and prints the output path
in a Json file.

Other Files :
___________

Reading / Writing JsonFiles :

 - READING : GameConfigReader.java, JsonFormat.java
 - WRITING : JsonWriter.java

Utility functions for book-keeping tasks :

- DataParserUtilities.java
- MapUtils.java






















