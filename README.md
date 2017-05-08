#VOOGASalad - No Place Like ~/

###README

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

* Faith Rodriguez (far10) contributed to the front end development of the project.  She created both the DetailPane and TemplatePane.  Additionally, she worked to implement dragging capabilities and editing capabilities for units, as well as other various tasks that came up as the project progressed, such as helping to switch a unit from belonging to a player to belonging to a team.
* Sam Schwaller (scs51) contributed to the front end development of the project. Created the StartupScreen, StartupSelectionScreen, and Loading group. Identified bugs in UI and assissted with Style and placement decisions. 
* Andreas Santos(ajs118) Helped with Design, Implementation, and creation of Wizard hierarchy and wizard classes, and helped with integration of Polyglot utility into the wizards. Worked on worldview classes for basic implementation of the worldview, and helped design/plan frontend UI. Helped add small features and debug throughout the frontend esp. in relation to implementation of game sounds, connecting wizards to add buttons on the template pane, and assigning units to teams instead of players.
* Dylan Peters (dlp22) Designed the Wizard hierarchy in order to allow as much flexibility in the creation of new VoogaEntities as possible. Coded almost all of the wizard classes. Implemented the support for Polyglot and language changing. Refactored the front end to use the factory design pattern in order to improve encapsulation. Created the BaseUIManager class that every front end class extends in order to allow flexibility for the front end classes while creating a uniform inheretance hierarchy that provides built-in behavior for every front end class. Refactored startup package to use a delegate design pattern so that the startup screen does not have to implement the actual code that is required to start, load, or join games, but rather delegates to a dedicated class to handle that. Designed the WorldView class structure (the differentiation of WorldView, GridView, CellView, UnitView, and the LayoutManagers that the GridView uses so that it doesn't have to differentiate between hexagonal and square grids. Wrote back end tests.
* Stone Mathers (smm117) Worked with Dylan in the overarching design of the frontend. Specifically implemented the StartupScreen, StartupSelectionScreen, View, VoogaMenuBar, Hexagon, GridLayoutDelegate and HexagonalLayoutDelegate classes, along with the entire frontend.conditionspane package. Also implemented communication between the frontend and Controller, as well as some communication between the Controller and backend. This included some implementation in the Controller, GameEngine, Resultant, Actionable, and Requirement classes. Served alongside Dylan as primary delegators of frontend responsibilities and helped communicate backend functionality to fronted when necessary. Contributed to overall debugging and UX design decisions. 
* Alex Zapata (az73) Worked with Timmy on the complete back-end of the project. Helped decide on the design implementation of all of the back-end classes (except the ScriptEngine and the Networking). Completely implemented the DieselEngine implementation of the GameEngine class to rule-check and act as a managing agent for TurnRequirments, TurnActions, and EndConditions. Wrote the implementations of the Actionable, Requirement, and Resultant wrapping classes for the named use of specific Predicates, Consumers, and ResultQuadPredicates (which I also wrote). Rules, turn reuirements, and beginning of turn actions are completely implemented on the back-end, but have had trouble being implemented on the front-end for front-end people. Helped come up with the AuthoringGameState and GameplayState interfaces, and implemented/refactored the GameplayState throughout. Came up with the idea for a factory design pattern on the front-end explained to teammates with the ComponentFactory class. Came up with Observer/Observable idea for front-end clicks, explained to teammates with ObserverViewTrial class. Refactored and debugged front-end code, including code for grid-pattern display and creation. 
* Noah Pritt (ncp14) Implemented the controller to communicate between the frontend and the backend. The classes that he created to achieve this are in the controller package, with most functionality found under CommuniationController.java. Helped Timmy implement the GameState classes (AuthoringGameState.java, GameplayState.java, ReadonlyGameplayState.java) which store the information of the current game in both the authoring and play states. Also implemented a QuickAdd feature to allow many types of ActiveAbilities to be created without scripting; to accomplish this, had teamates come up with numerous game ideas and then selected the most common recurring ActiveAbility types. Implemented a number of frontend features, including loading script files and handling the server frontend wizards. Implemented really awesome animated buttons (no, really, they're great).  
* Timmy Huang (th174)

    > * Overall game design
    > * Backend grid, terrain, cell, unit, abilities, player, and team design and implementation
    > * TCPIP Socket networking design, and implementation
    > * JSR_223 Scripting design and implementation
    >   * Also added Java Scripting design and implementation
    > * Networked Model View Controller design
    > * Multithreaded concurrency expert
    > * Code Maintainence and support
    > * Controller design and refactoring
    > * Visual game board UI
    > * Minimap 
    > * Visual UnitView and CellView UI components
    > * AbilityPane UI 
    > * Universal UI component interaction event handler framework 
    > * Player-Team-Turn UI component
    > * User script creator
    > * Chat client
    > * Frontend UI component refactoring
    > * Debugging


**Getting Started**

The project can be started by running the class Main.java located in the default package within src. This class will create a startup screen that allows you to create a new game, load a saved game, or join a currently running game.

#### Create a New Game: 

When the user clicks to create a new game, you will be prompted to enter a port number. This port number is used start a VOOGASalad server on your computer that will manage the development of the game (the server checks and approves actions that the user performs. Don't worry, your data is not being transferred off of your machine, but rather simply delegating to a server program running in the background! When you exit the game, the server will exit too. (But remember the port number you entered for the server, because that is what you will give to people if you want to invite them to help you edit or to play against!)

After the server has been set up, the New Game Wizard will run. Simply enter the name of the game you are creating, the icon for the game, and a short description; these fields can be left blank as well. The wizard will then ask you to instantiate a grid. You can name the grid and add a description and background image. Finally, enter the desired grid size and shape, and the real editing will start!

#### Join an existing game:

If somebody is hosting a game or game editing session, they can invite you to join. Congratulations, if you are reading this section it's because you have a friend that invited you to play/edit with them!

Start the program and select "Join an Existing Game". This will prompt you for an IP adress and a port number. Simply enter your friend's IP address and ask them what port they started their game on, and when you click finish, the editing/playing screen will begin loading.


#### Load a Saved Game:

If you already have a game that you would like to play, you don't need to replicate it in the editor, simply start the program and click the last button: "Load a saved game". Like the other two options, it will prompt for a port to start the server on. After inputting that, simply select the saved xml file to load, and the play screen will open.

**Testing**

The classes that were used to test this project can be found in the folder "voogasalad_noplacelikehome/tests/tests". The tests in this folder are intended primarily to ensure that the back end classes are reliable and function correctly. They test the creation and manipulation of units, terrains, abilities, games, etc programmatically. They also test networking abilities. There are not a large number of front end tests because they are much harder to write JUnit tests for.

**Required Resources**

Resources for this project include the following:
* Images can be found in the "voogasalad_noplacelikehome/src/resources/images" folder. Further images can be found in the "voogasalad_noplacelikehome/img" folder. There are also default sound files, css files, and html pages stored in the "src/resources" package.
* Almost every front end class displays some type of text; the text that these classes display is stored in resource files in order to make it easy to change without directly manipulating code. These "resources.properties" files are stored in the same package as the classes that use them, in order to make them easy to locate and change.
* We reference several libraries in this project, because we utilize the polyglot utility; this requires us to add the Google Translate API libraries. In addition, we also use the regular Java, JavaFX, and JUnit Test libraries. All of these referenced libraries are stored in the "voogasalad_noplacelikehome/Referenced Libraries", "voogasalad_noplacelikehome/JRE System Library", "voogasalad_noplacelikehome/JUnit Test"  folders.

**Using the project**

##### A Note About Networking:

Some wifi networks have firewalls that prevent computers from talking directly to each other. (Duke Blue is one of these types of networks.) To get around this, simply download LogMeIn Hamachi, which is a networking utility that allows computers to create virtual LAN. The download and install information can be found here: [Hamachi](https://www.vpn.net/).

Once you have installed Hamachi, you can create a virtual LAN by doing the following:

1. Start Hamachi.
2. Create an account.
3. Select "create a network."
4. Give the network a name and password.
5. Tell your friends the name and password of the network to allow them to join it and connect to you!

If you are on one of these firewalled networks but still wish to play, you can play locally on your own computer; when you start a game, you still need to enter a port number to run the local server on, but the server runs on localhost. To allow another person to play against you on the same computer, open a new instance of the program and within that instance, select "join a game." Use 127.0.0.1 (localhost) as the IP address for the server, and enter the same port you entered earlier. This connects the two running instances of the game. Now, instance A is hosting the server, while instance B is connected to the server. Both instances are running and you can play multiplayer against your friends right from the same computer!

##### Troubleshooting tips:

If you have attempted to connect with another computer, but it is not being found, try disabling your computer's firewall.

If the computers connect but do not update when one computer changes something, the Java versions may be incompatible. It is important that both computers have the same version of Java. If running the program within Eclipse, do the following:

1. Open Eclipse.
2. Go to Preferences > Java > Installed JREs, and check that both computers have the same JRE version installed. 
3. If they do not, find the latest JRE version from [Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) and install them in Eclipse.

## More Detailed Usage Information:

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

**Scripting Tutorial**

**Known Bugs and Limitations**


**Extra Features**

One extra feature that was implemented in this project is the extensive use of networking.  Players on different computers can both create and play games live-time with each other.  

The biggest extra features that our project has are: networking, live editing, parrallelism, and language changing.
###### Networking:
Networking is the single biggest extra feature that our program has. Because turn based strategy games are inherently multiplayer, we decided from the beginning that we wanted to have the ability to play against other players online. Because we built this capability into our program, we have created the added ability to edit the game on multiple computers as well as play on multiple computers.
###### Live Editing:
Our program allows multiple users to edit the game at one time. The editing of the gamestate works in a similar way to playing, by sending messages to a server, which are then forwarded to rest of the connected computers. This means that two (or three, or four, etc.) people can work together to create a complex game quickly.
###### Parrallelism:
Our program is designed as modularly as possible, so you can run multiple instances of it on the same computer. The user can edit one game while playing another game in another window. This feature will be especially useful for the hardcore gamers that want to play chess while waiting for their opponent to move in tic tac toe, while creating a Civilization-type game online with some of his/her friends in another country.
###### Language Changing:
We have utilized another team's utility -- Polyglot -- in order to facilitate language changing in our game editor. The support for Polyglot is in nearly every UI class, so simply selecting one of the many languages from Google's Translate API allows the user to utilize the program in another language. The program is built modularly enough that people can play together even if they are in different countries or speak different languages; one person's editor can be in English while someone else's is in Spanish, and a third person is editing in Arabic (or any of the languages supported by Polyglot).

**Overall Impressions**

Because this was the largest assignment anyone in our group has worked on, it proved very difficult to develop. Coordinating 10 different people has been challenging; not only do we have to work around everyone's different schedules, but we must also be cognizant of the fact that everyone's area of coding expertise is different. The step up from the previous assignment (with four people) to the magnitude of this assignment (with 10) presented a challenge, but our team managed to break up the project into stages and finish it.
We chose to do turn based strategy games because the genre was very flexible and allowed us to implement so many different types of games. The broadness of the genre made it difficult to create a develpment environment that could create any turn based strategy game. However, we have accomplished creating a very generalized editor that allows enough flexibility; our team rose to the challenge of our genre.
The ability to create such a large project in a short amount of time helped everyone on the team learn and significantly improve our ability to design software, discuss design decisions, and weigh the pros and cons of a given design.