#API Changes

>CompSci 308 VOOGASalad Project

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

## Backend API Changes

The back end API did not change much during the second coding sprint because most of the work was done to make the front end catch up to the back end. There were a few significant changes, however.

One minor change in the back end API is that we added a HasSound interface. This interface simply specifies that an object that implements it can be given a soundpath so that the front end can play a sound when something interacts with it. For example, a terrain could have a sound that plays when a unit moves to it. (Rocks would make a different sound when a unit moves over it than water, for example.)

A second, more significant change in the back end API is that all of the predefined templates were moved to XML files. When you create a new game, there are lots of predefined units, terrains, and abilities that auto-populate the authoring environment. However, creating these presets in the midpoint code used a set of public statics like the following: 

>public static final ModifiableCell BASIC_HEXAGONAL_EMPTY = new ModifiableCell()
>
>public static final ModifiableCell BASIC_HEXAGONAL_FLAT = new ModifiableCell()
>
>public static final ModifiableCell BASIC_HEXAGONAL_FOREST = new ModifiableCell()
>
>public static final ModifiableCell BASIC_HEXAGONAL_WATER = new ModifiableCell()
>
>public static final ModifiableCell BASIC_HEXAGONAL_MOUNTAIN = new ModifiableCell()
>
>public static final ModifiableCell BASIC_HEXAGONAL_FORTIFIED = new ModifiableCell()

This is obviously not an extensible design pattern, so for the final code, we refactored the `ModifiableCell`, `ModifiableUnit`, `ActiveAbility`, `InteractionModifier`, and `TriggeredEffect` classes so that they load these predefined entities from xml files. Aside from cleaning up the code, this has the added benefit of allowing us to add more predefined entities simply by adding xml files to the project, rather than having to write code within the classes and risk breaking them. This change increase the level of closed software in our project.

## Controller API Changes

The most important API Change in the controller package is that we expanded the list of the Controller's public methods. Before the midpoint demo, the controller had methods for `getAuthoringGameState()`, for sending requests to the server, and for getting the list of templates that have been created (i.e. `getUnitTemplates()` `getTerrainTemplates`, etc.)

After the midpoint demo, we expanded the functionality of the controller. The controller now has many methods that add to its ability to communicate with the front and back end:

>public ImmutablePlayer getPlayer(String name)
>
>public U extends ReadonlyGameplayState void sendModifier(ModifierU modifier)
>
>public Collection<? extends Unit> getUnits()
>
>public Collection<? extends VoogaEntity> getTemplatesByCategory(String category)
>
>public void addTemplatesByCategory(String category, VoogaEntity... templates) 
>
>public void removeTemplatesByCategory(String category, String... templateNames) 
>
>public void addListener(UIComponentListener listener) 
>
>public void removeListener(UIComponentListener listener) 
>
>public void enterAuthoringMode() 
>
>public void enterGamePlayMode()
>
>public boolean isAuthoringMode()
>
>public boolean myTeamWon()
>
>public boolean myTeamLost()
>
>public boolean myTeamTied()
>
>public void setPlayer(String name, String description, String imgPath)
>
>public void addTurnRequirement(String name, String description, String imgPath, SerializableBiPredicate biPredicate)
>
>public void removeTurnRequirement(String name)
>
>public void activateTurnRequirement(String name)
>
>public void deactivateTurnRequirement(String name)
>
>public void addTurnAction(String name, String description, String imgPath, SerializableBiConsumer biConsumer)
>
>public void removeTurnAction(String name)
>
>public void activateTurnAction(String name)
>
>public void deactivateTurnAction(String name)
>
>public void addEndCondition(String name, String description, String imgPath, ResultQuadPredicate resultQuadPredicate)
>
>public void removeEndCondition(String name)
>
>public void activateEndCondition(String name)
>
>public void deactivateEndCondition(String name)
>
>public void updateAll()
>
>public void joinTeam(String teamName)
>
>public void sendFile(String path)
>
>public void endTurn()
>
>public void moveUnit(String unitName, CoordinateTuple unitLocation, CoordinateTuple targetLocation)
>
>public void sendMessage(String messageContent, ChatMessage.AccessLevel accessLevel, String receiverName)
>
>public void setBoundsHandler(String boundsHandlerName)
>
>public void copyTemplateToGrid(VoogaEntity template, HasLocation destination)
>
>public void removeUnitFromGrid(String unitName, CoordinateTuple unitLocation)
>
>public void useActiveAbility(String abilityName, String userName, CoordinateTuple userLocation, String targetName, CoordinateTuple targetLocation)
>
>public void undo()
>

All of the above methods were added after the demo in order to support all of the increased functionality of our game that we created. The functions that these methods enable the controller to handle are: rules such as turn actions, turn requirements, and end conditions; creating and joining teams; having players take turns moving pieces; keeping track of who won or lost; and switching between authoring and gameplay mode.

We did deprecate a few methods from the controller. The getUnitTemplates and getTerrainTemplates methods were replaced with `getTemplatesByCategory(String categoryname)` in order to make it easier to add new categories without having to make more future changes to the API.

## Frontend API Changes

The front end did not experience very many deprecated methods or deletions during the second coding sprint, primarily because we were adding functionality rather than removing it. However, there are a few deprecations that were made:

In the BaseUIManager class, we deprecated the methods `getPossibleLanguages` and `getLanguage` because we changed our project in order to use the Polyglot utility. Instead of these deprecated methods, the front end classes that extend BaseUIManager are supposed to use the new `getPolyglot` method in order to access language functionality and the `getResourceBundle` method to access the resourcebundle directly.

One significant addition we made was that we created the ConditionsPane in the front end in order to allow the user to add rules to the game in authoring environment. 

We also expanded most of the front end classes to improve support for clicking and click-and-drags. We created the ClickHandler class that listens for clicks on different parts of the UI and contains much of the logic for what happens when something is clicked. This allows us to differentiate between authoring and gameplay mode without having to make significant changes to the UI classes.