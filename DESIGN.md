**Design**

**Names:** 

* Dylan Peters (dlp22)

* Kris Elbert (ke60)

* Faith Rodriguez (far10)

* Stone Mathers (smm117)

* Tavo Loaiza (eol) 

* Alexander Zapata (az73) 

* Noah Pritt (ncp14)

* Timmy Huang (th174)

* Sam Schwaller (scs51)

* Andreas Santos (ajs118)

**Introduction**

*This section describes the problem your team is trying to solve by writing this program, the primary design goals of the project (i.e., where is it most flexible), and the primary architecture of the design (i.e., what is closed and what is open). Describe your chosen game genre and what qualities make it unique that your design will need to support. Discuss the design at a high-level (i.e., without referencing specific classes, data structures, or code).*

VOOGASalad is a game engine that provides a game authoring and game playing environment. Our game engine is designed to author turn-based strategy/tactics games, in which each player manipulates a set of units on a grid to achieve an objective. The most direct examples of turn-based strategy tactics games include Battle for Wesnoth, Civilization, Fire Emblem, Final Fantasy Tactics, and Chrono Trigger. Our engine should be flexible enough to support other types turn-based strategy games, such as board games like Tic Tac Toe, Risk, and Battleship. Finally, a very flexible game engine and game authoring environment would enable users to create games from other genres, such as RPG, MOBA, and Tower Defense, by interpreting them as extensions of turn-based strategy.

**Overview**

*This section serves as a map of your design for other programmers to gain a general understanding of how and why the program was divided up, and how the individual parts work together to provide the desired functionality. Describe specific modules you intend to create, their purpose with regards to the program's functionality, and how they collaborate with each other, focusing specifically on each one's API. Include a picture of how the modules are related (these pictures can be hand drawn and scanned in, created with a standard drawing program, or screen shots from ***_[a UML design progra_**m](http://www.uml-lab.com/en/uml-lab/academic/)*). Discuss specific classes, methods, and data structures, but not individual lines of code.*

Our design will be split into four parts: the game data, game loader, game engine, and game editor.

The game data will be in the form of XML files in order to make them easily readable and editable. Further game data will be binary data files such as images, audio files, and videos.

The game loader will be responsible for reading all of the game data files and interpreting them into the proper Java format so that they can be played with the game engine. To further extend the project, we could make the game loader capable of both loading games into the game engine and of saving data from the game editor.

The game engine will be responsible for running the game itself. It will contain all of the logic for the game, and will interact with the user through the Game Player, which is a simple set of classes that allow the user to start, stop, quit, and restart the game engine. Our game engine will consist of a grid of cells, which should be populated with hexagonal or rectangular cells. In later extensions, we will try to add the ability to add more cell shapes. The game engine also contains global properties, such as time of day, score, and lives. Each cell has information on its location, terrain, current occupying unit, and any special properties. The cell should be able to query the grid to determine its neighboring cells. Each unit would have a HP, attacks, defenses, and movement. Moving will consume a unit’s movement points, based on the type of terrain they pass over. A unit’s defense is also modified by the type of terrain its occupying. On each player’s turn, players should be able to manipulate each of their own owned units, as well as create new ones when necessary. Players may be limited in what they can see by Fog of War, which can be implemented within the cells by allowing the cells to determine which players can view them.

A game authoring environment will allow users to build levels by designing the terrain layout in each cell, as well as create unique units with special properties that interact with terrain, time of day, the grid, and other units. The game authoring environment will be an easy-to-use graphical interface that constructs both the Model and the View for the game. For example, when creating a Tic Tac Toe game, the game editor would be responsible for creating a model with the correct gameplay rules (you cannot move Xs or Os; three in a row wins; etc.), and the game editor would also need to write the rules for the View that determine what image files are used to represent the background, the X sprites, the O sprites, etc.

Our game authoring environment will be very similar to the game play environment; we will implement it such that the two screens are simply different versions of the same views. The user will be able to toggle between each screen (unless it is in a strictly playing mode, in which case the view will not allow the user to toggle).

**User Interface**

*This section describes how the user will interact with your program (keep it simple to start). Describe the overall appearance of program's user interface components and how users interact with these components (especially those specific to your program, i.e., means of input other than menus or toolbars). Include one or more pictures of the user interface (these pictures can be hand drawn and scanned in, created with a standard drawing program, or screen shots from a dummy program that serves as a exemplar). Describe how a game is represented to the designer and what support is provided to make it easy to create a game. Finally, describe any erroneous situations that are reported to the user (i.e., bad input data, empty data, etc.).*

From the Selection screen, the user can choose to Play a Game, Create a New Game, or Edit an Existing Game. Depending on their choice, the user may then select a file and enter either the Play or Development Environments.

Both Environments will have a menu bar at the top that can take them back to the Selection screen. Scrolling the mouse wheel will zoom in and out within zoom extents and holding down the wheel and dragging will pan the view. Additionally, there will be a small map in the corner that shows the entire extents of the board. Clicking on this will center the user’s view over the center of the grid, or the origin, at a set distance, so the user does not get lost.

When creating a new game, the user must set a name for their game, the author, cell shape (base shapes are hexagonal, rectangular, and triangular), two dimensions for the grid size, and a Game Type from  a set of default win conditions. A button at the bottom of this selection pop-up will lead the user into the dev environment. All of these selection fields, including name and author, have defaults that the user can change if desired.

In the Dev Environment, a pane on the left gives the user access to manipulation tools: delete, copy, rotate.

Rotate is dependent upon the cell shape. The user will select a base point which will be rotate around.

On the right hand side, there is a place pane. The user can drop down options for adding Terrain, or properties to cells, and Units, or manipulatable objects that can occupy already-created cells. The user can use default cells that can have limited abilities, such as only storing one unit at a time and having a background color or image to being able to store units only of a certain type and impacting their abilities while in that cell. The user can create entirely new terrains and units or create from previous ones. These custom settings can be used again and again in the game and are saved as new types when the game is saved. 

In the Play Environment, the user gets their grid(s) display which may differ from that of their opponent. Right clicking on a unit will display information about its movement or attack abilities if applicable. A notification at the top will keep track of whose turn it is. If a player can move multiple units in one turn, a counter will display the remaining number of moves they can make.

**Design Details **

*This section describes each module introduced in the ***_Overview_*** in detail (as well as any other sub-modules that may be needed but are not significant to include in a high-level description of the program). Describe how each module handles specific features given in the assignment specification, what resources it might use, how it collaborates with other modules, and how each could be extended to include additional requirements (from the assignment specification or discussed by your team). Note, each sub-team should have its own API for others in the overall team or for new team members to write extensions. Finally, justify the decision to create each module with respect to the design's key goals, principles, and abstractions.*

**Part 1: Initialization**

**Front-End:**

**Selection**

Title

* Name of our program at top

Buttons

* Create New Game

* Edit Existing Game

* Play Existing Game

**Development**

**Presets**

Label + Field to type or select option

* Name (type)

* Author (type)

* Cell Shapes (select)

* Grid Size (type, two text fields)

* Game Type (select)

Button

* Button to get started

**Game Editing**

Tool Pane

	Buttons

* Delete

* Copy

* Rotate

* Display Grid On/Off

Place Pane

	Dropdowns

* Terrain

* Units

Buttons

* Lead to detailed customization fields

	Terrain Fields

* Sprite

* Maximum and Minimum Unit Storage

	Unit Fields

* Sprite

* Num

* Movement (show a little grid and allow to choose where can end up on a move)

Map Display

* Node

**Player**

Stats Screen

* High score

* Current score

* Level 

Menu Bar

* Return to Home Screen

* Save

* Quit

* Edit

* Play new game

* Restart

* Help/Instructions

Game Screen

* Turn Count

* Number of Moves Left per Turn (if relevant)

* Avatar and Score

* Opponent's score(s)

**Back-End:**

**Part 1: **Initialization stage

![image alt text](image_0.png)

	**Part II: API**

	Our API has been written as Java interfaces found in the git repository. It can be found [here](https://coursework.cs.duke.edu/CompSci308_2017Spring/voogasalad_noplacelikehome/tree/master/src/backend).

**Example games**

*Describe three example games from your genre in detail that differ significantly. Clearly identify how the functional differences in these games is supported by your design and enabled by your authoring environment. Use these examples to help make concrete the abstractions in your design.*

1. In a game like Battleship, the Game Engine will control which player goes when and which grids they can see, so as to not reveal the location of their ships to their opponents preemptively. There are four grids total, as each player has two grids: one is the grid where a player places their own ships, and the other is their more restricted view of their opponent’s grid. The default terrain of opposing cells will be water, until a projectile unit reveals them to hold nothing, at which point they will hold a Splash, or part of a ship, at which point they will hold Fire. Each cell can hold a maximum of two units, unless they hold a Splash, at which point they cannot hold another unit. The cell will decide what unit is placed when the player attempts to place it, because the cell knows if there is or is not a ship unit in it as well. The player can place a unit anywhere on the grid, but only once per turn. Once placed, these units cannot be moved or modified by the player. Ship units are grouped based on the overall ship length, and these units must be placed next to each other so the ship is not broken. These units are placed early on and hidden from the other player until they become FIRE. Until then, the cell will hold them, but not display anything different than its default terrain on the grid of the opposing player. It will display the full ship on the grid of the player who placed them.

2. Checkers differs from Battleship in that the player cannot drop new pieces onto the board, but instead must move around the pieces that are already on the board at the beginning of the game. To support this, our game engine will support selection of game pieces by clicking on them, as well as selection of empty cells by clicking. The game engine will need to be able to detect when a player’s turn is over, and then allow the other player to move.

3. In a game like Tic Tac Toe, there is only one terrain for all of the cells, which can hold up to one unit. There are two types of units: Xes and Os, but each player can only place one type. The Game Engine will check for win conditions by asking the cells if they are full and what they are full of. The Game Engine will also check for Cats condition, when the board is full but there is no winner. Players alternate turns and place one unit each time.

**Design Considerations **

*This section describes any issues which need to be addressed or resolved before attempting to devise a complete design solution. Include any design decisions that each sub-team discussed at length (include pros and cons from all sides of the discussion) as well as any ambiguities, assumptions, or dependencies regarding the program that impact the overall design.*

One of the most difficult design design problems we are facing is how to allow the user to define the win conditions. For example, the winning conditions for Tic Tac Toe are significantly different than the winning conditions for Civilization. Furthermore, even the winning conditions for different variations of a simple game such as Tic Tac Toe can be very complicated (getting four in a row, creating an ‘L’ shape, four next to each other, etc.), and can be hard to create a graphical way of allowing the user to define such a win condition. Our present solution for this problem is to allow the user to code the winning conditions using a limited-functionality IDE. The winning conditions that the user codes will take the form of Predicates that are checked during each turn to see if a player has won. This strategy of allowing the coding of small parts of the game is used in many current production game development environments. The difficult part of creating this functionality will be ensuring that the user can access information about the state of the game, without letting the user’s code break the game, game engine, or game development environment. Finally, making it flexible enough to support any type of win condition will be difficult.

![image alt text](image_1.jpg)![image alt text](image_2.jpg)![image alt text](image_3.jpg)![image alt text](image_4.jpg)![image alt text](image_5.jpg)![image alt text](image_6.jpg)![image alt text](image_7.jpg)

