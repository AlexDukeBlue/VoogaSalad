#voogasalad

###ReadMe

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


**Project Time Frame**

Work on this project began March 24 and ended on April 30 with an estimated total of 600 combined hours spent.

**Individual Contributions**

//TODO EVERYONE ADD THEIR CONTRIBUTIONS

* Faith Rodriguez (far10) contributed to the front end development of the project.  She created both the DetailPane and TemplatePane.  Additionally, she worked to implement dragging capabilities and editing capabilities for units, as well as other various tasks that came up as the project progressed, such as helping to switch a unit from belonging to a player to belonging to a team.
* Sam Schwaller (scs51) contributed to the front end development of the project. Created the StartupScreen, StartupSelectionScreen, and Loading group. Identified bugs in UI and assissted with Style and placement decisions. 

**Getting Started**

In order to run the project, use the class Main.java located in the default package within src.  If you wish to play or edit a game with a user on another computer, you must first download LogMeIn Hamachi, turn it on, and join a network for you and the other user to play on.

**Testing**

The classes that were used to test this project can be found in the folder tests.

**Required Resources**

Resources required by the project, including images, sounds, and resource bundles, can be found within the resources package within the project.  Additionally, many front end packages contain their own resource package for items that are specific to only that part of the project.

**Using the project**

#####Creating a Game
In order to create a game, select the Create a New Game button in the startup screen and input a port number within the bounds shown.  This is necessary to create a client server network.  Next follow the outlined steps in the wizard, making sure to read through the selections carefully.  Once the authoring environment loads, create a team by clicking Edit / Create New Team in the MenuBar.  When choosing the team's color, select one other than white, as this is the color used to represent a team-less unit.  Next, join a team by clicking Play / Join a Team.  From now on, any units that you add to the board will belong to the team that you currently belong to.  But you can change this, along with any other details regarding the unit by clicking on it and pressing the Edit button that appears in the pane below the grid.

To add new units and terrains to the board, simply click on them in the scrolling pane to the right and click on the location in the grid that you want to add them to.  When you are done adding a particular unit, press the x button located in the bottom pane.  In order to move a unit that has already been placed on the board, select it and click on the location that you would like to move it to.  If there are more than one units on a particular cell, you will be required to right click the cell and select the unit you wish to interact with from the mouseover.

To set end conditions, turn requirements, or turn actions, first create a new one by either scripting a condition of your choice or loading one of our default options.  After it has been created, press the check button to add the condition to your game.

To load in new objects or an entire game use the File / load buttons found in the menu bar and various wizards.

#####Joining a Game
In order to join a game you will first need to enter the game creator's IP address for their remote Hamachi account.  Then you must enter the port number that they entered upon game creation.

#####Playing a Game
When you are ready to play, make sure that you are on your desired team and press play game.  In this mode, you will only be able to move the units belonging to your team within the parameters that you set when it is your turn.  

If you wish to edit your game, press Play / Edit Mode to return to the authoring environment.

//TODO SCRIPTING TUTORIAL

**Known Bugs and Limitations**

//TODO CLOSER TO DEADLINE

**Extra Features**

One extra feature that was implemented in this project is the extensive use of networking.  Players on different computers can both create and play games live-time with each other.  

**Overall Impressions**

This assignment, partially because of the genre that we chose, was very broad making it difficult to implement.  Trying to create a design flexible enough to support the entirety of our genre, as well as other genre's turned out to be a great challenge.

