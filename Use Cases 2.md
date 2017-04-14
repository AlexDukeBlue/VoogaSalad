**Use Cases Document 2**

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

1. Dylan: User creates new Unit template

    1. The user clicks the "+" button within the TemplatePane, which creates a new UnitWizard. The TemplatePane adds a listener to the UnitWizard that triggers when the UnitWizard returns a new Unit. When the UnitWizard returns a Unit, the TemplatePane updates its collapsable list of units that allows the user to click and drag units to the WorldView.

        1. Within UnitWizard: The UnitWizard delegates the windows of the wizard to the UnitSelectionStrategy class (which extends SelectionStrategy). This means that the selection strategy has a finish() method that returns a Unit. The selection strategy goes through a number of screens that allows the user to create the new Unit. 

        2. Within the UnitSelectionStrategy: The UnitSelectionStrategy has a screen that allows the user to give the new Unit an image, name, and description. It then allows the user to specify the HP of the unit and the number of Movement Points. Then, it shows the screen that allows the user to add abilities to the unit. The user does so by selecting check boxes next to the list of abilities. Finally, the user inputs the number of movement points required to cross each type of terrain. Once all of these fields are filled, the "finish" button highlights, and the UnitSelectionStrategy returns the created Unit object.

2. Dylan: User clicks and drags Unit onto WorldView

    2. When the user initiates click-and-drag on a unit in the TemplatePane, the TemplatePane calls the setOnCellDragDrop(lambda) method of the WorldView. The setOnCellDragDrop method within worldview will loop through each cell and add a listener to each cell’s image object, with the lambda expression specified in the method signature.

    3. When the user drops the unit within the cell, the cell calls getController().addToOutbox(lambda). This expression causes the controller to send the lambda expression to the server. The server then sends the lambda expression to all players connected to the server. 

    4. When the controller receives the lambda expression, it executes the lambda on the gamestate. The controller then calls updateAll(), which updates all of the view items that have added themselves to the list to be updated (in this case, the cellView will have added itself, so it will be updated).

3. Dylan: User creates new Rule

    5. The user clicks the "+" button within the RulesPane, which creates a new RuleWizard. The RulesPane adds a listener to the RuleWizard that triggers when the RuleWizard returns a new Rule. When the RuleWizard returns a rule, the RulesPane updates its collapsable list of rules that allows the user to select rules.

        3. Within RuleWizard: The RuleWizard delegates the windows of the wizard to the RuleSelectionStrategy class (which extends SelectionStrategy). This means that the selection strategy has a finish() method that returns a rule. The selection strategy goes through a number of screens that allows the user to create the new rule. 

        4. Within the RuleSelectionstrategy: The RuleSelectionStrategy has a TextArea within which the user writes Groovy code. When the user hits "compile", the Groovy code compiles, and the RuleSelectionStrategy returns the new Rule.

4. Dylan: User hovers mouse over cell

    6. When the cell instantiates its view object (image), it adds a mouseover listener to the object. The implementation of the mouseover listener creates a new MouseOverDescription object. This object will have its text filled by the cell’s getDescription() method because the CellView’s Cell object is a VoogaEntity, which means that it has the getDescription() method by default.

5. Kris: Asdf

6. Kris: Asdf

7. Kris: Asdf

8. Kris: Asdf

9. Able to activate ability of unit

    7. User clicks on a unit within the template pane

    8. A list of abilities with their images appears in the detail pane

    9. The user drags the ability image into the cell with its unit.

10. User meets win condition of defeating all enemy units

    10. When the end condition is met, a popup screen comes up congratulating the winner

    11. The losers will see a screen saying sorry that they lost.

    12. All parties are prompted to play again or choose a new game.

11. User drags a terrain to a cell that already contains a different terrain

    13. The new terrain replaces the old one in that cell.

12. User wants to see description of unit in grid

    14. The user hovers over a unit in the grid

    15. A description will appear for as long as the user hovers of it

13. Stone: User edits unit data in detail pane

    16. User clicks on the unit in the TemplatePane

    17. Details and abilities appear in the DetailPane at the bottom of the screen

    18. User clicks on the data in the pane

    19. TextField appears with the current data in it, and user fills it with new data

    20. User presses "enter"

    21. DetailPane send a Modifier to the Controller to change that UnitTemplate

    22. Data change is made in GameState

    23. TemplatePane gets updated, which in turn updates the DetailPane

14. Stone: User switches from "edit" to “play” mode

    24. User clicks on a "Play game" button in VoogaMenuBar

    25. VoogaMenuBar sends a Modifier to the Controller to switch to "play" mode

    26. GameState reflects change in state

    27. View’s update gets called and it is reinstantiated, removing all editing features

    28. Game engine now enforces rules and checks win/lose/tie conditions

15. Stone: User sets end condition

    29. In the Rules/Conditions pane, the user clicks on either the "Win," “Lose,” or “Tie” checkbox

    30. Rules/Conditions pane sends the appropriate Modifier to the Controller

    31. Controller send Modifier to Server through Client

    32. Server verifies request and sends it to all Clients

    33. GameStates reflect change in conditions

16. Stone: Player satisfies all losing conditions

    34. Player performs an action

    35. Controller send request to the Server through the Client

    36. Game engine validates move

    37. Move is considered valid, so all conditions are checked

    38. It is determined that the Player has satisfied all losing conditions

    39. Appropriate requests are sent back to the Client

    40. Controller makes necessary changes in GameState, removing the Player from the Collection of active Players

    41. All of the Player’s Units are removed from the grid

    42. Client remains on the server so the user can continue to observe the game, but they no longer receive a turn

17. Tavo: Zxc

18. Tavo: vbnm

19. Tavo: ,./

20. Tavo: 1234

21. Alex: 5678

22. Alex: qwer

23. Alex: tyui

24. Alex: op[]

25. Noah: User creates a new unit in the frontend

    43. User clicks on the "new unit" button on the right side of the Authoring view

    44. A Unit is constructed using the fields 

    45. A modifier is created and sent to the controller containing the view. The AddUnit() method is then called which adds the unit to the gamestate.

    46. The AuthoringGamestate now includes the new units.

26. Noah: User creates a new terrain unit in the frontend

    47. User clicks on the "new terrain" button on the right side of the Authoring view

    48. A Unit is constructed using the fields 

    49. A modifier is created and sent to the controller containing the view. The AddTerrain() method is then called which adds the unit to the gamestate.

    50. The GameplayState now includes the new terrain.

27. Noah: l;’

28. Noah: zxcv

29. Timmy: User creates grid with template cell

    51. Calculates set of valid coordinates for the grid

    52. Grid copies cell to each coordinate

30. Timmy: User creates new ability

    53. New ability wizard starts

    54. Asks user for name, description, and img

    55. Allow user to script effect

31. Timmy: User 

32. Sam: asdf

33. Sam: asdf

34. Sam: asdf

35. Sam: asdf

36. Andreas: default end condition chosen (user wants to extend this condition)

    57. User navigates to the rules/end conditions pane and chooses the end condition that he/she wants to extend.

    58. User clicks on this end condition to extend it

        5. A text pane pops up where user can write script code to edit the end predicate

    59. The user can then save this end condition script code as a new end condition (or updated)

    60. This new end condition is now checked for in the game as any other would be

37. Andreas: right click on unit in cell

    61. User right clicks on unit in cell

    62. This will display editing options specific to ‘unit’ that could be found in the menu bar

    63. User can use this menu dialogue to create changes

38. Andreas: user changes characteristic of unit/terrain in the detail pane

    64. User sees description of unit/terrain in detail pane and wants to change (for example) move cost across a terrain.

    65. User double clicks on that area in the detail pane, which would bring some sort of pop-up window up (most likely the wizard for that object)

    66. User could change the value, and the updated object is created

39. Andreas: user when playing game, moves a unit

    67. In game movement (like movement in authoring) will be through click and drag movement

    68. When the user presses (mouse pressed) the mouse on a unit, the grid should display those units’ legal moves.

        6. The user can then drag the unit to any of these ‘legal’ cells to move it there, and place it on the destination cell with (mouseReleased)

        7. If the unit is dragged off the screen or to a non-legal cell, then the unit will remain in its cell (as if not moved)

40. Timmy: User disconnects in the middle of the game
    
    1. User reconnects
    
    2. Client sends error request to server 
    
    3. Server responds with the game state, including all of user's previous moves
    
    4. User picks up where they left off
    