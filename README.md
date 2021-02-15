# World Navigator

World Navigator is a web-based multiplayer game. Players are put in a map of rooms and compete against each other to
reach the ending room and win the game. Implementing this game, I had to follow the principles in Robert Martin's Clean
Code, Joshua Bloch's Effective Java and the SOLID principles. I also had to use appropriate design patterns among other
things. So, here I'll go over these points and defend my code against them.

# Table of Contents

<!-- TOC depthFrom:1 depthTo:3 withLinks:1 updateOnSave:1 orderedList:0 -->

- [World Navigator](#world-navigator)
- [Table of Contents](#table-of-contents)
- [Design & Structure](#design-structure)
    - [Configuration](#configuration)
    - [Controllers](#controllers)
    - [Services](#services)
    - [Entities](#entities)
    - [Repositories](#repositories)
    - [Security](#security)
    - [Game](#game)
        - [Game Class](#game-class)
        - [Maps](#maps)
        - [World Map Generator](#world-map-generator)
        - [Player](#player)
        - [Fighting](#fighting)
- [Clean Code](#clean-code)
    - [Comments](#comments)
    - [Functions](#functions)
    - [General](#general)
    - [Java](#java)
    - [Names](#names)
- [Effective Java](#effective-java)
- [SOLID](#solid)
    - [Single-responsibility principle](#single-responsibility-principle)
    - [Open–closed principle](#openclosed-principle)
    - [Liskov substitution principle](#liskov-substitution-principle)
    - [Interface segregation principle](#interface-segregation-principle)
    - [Dependency inversion principle](#dependency-inversion-principle)
- [Code Styling](#code-styling)
- [Data Structures](#data-structures)
- [Concurrency](#concurrency)

<!-- /TOC -->

# Design & Structure

Designing this project was not an easy feat as a lot of its components interact with each other, and there were a lot of
concurrency issues that I had to deal with. So, I had to find smart ways to meet the requirements while also trying to
defend my code against the principles I mentioned earlier. For this project, I used the Spring Framework which makes
writing stand-alone, production-grade java application with maximum decoupling and minimum boilerplate code. The Spring
Framework is built on the Model-View-Controller design pattern where a central Servlet controls the flow of the program
and offers a shared algorithm for processing requests. Here, I'll go over each of the components in my project and give
a brief description for each of them.

## Configuration

These classes configure Spring to the requirements of this project and tells it how to handle HTTP and websocket
requests. They also define some beans which are components that need to be shared between objects that depend on them
and are injected to these objects on construction.

## Controllers

Controllers or Endpoints are where requests get handled. They deal with requests and communicate with the appropriate
service for each request.  
There are two types of controllers, the first is a controller that handles HTTP GET and POST requests which are used to
load the page when a new user enters the website and for signup requests. The second is websocket controllers, after the
page is loaded, users have to login and are connected to the server through a websocket and credentials are validated on
connect requests then all user requests are handled through a RESTful API which's enabled by the websockets and handled
by these controllers.

## Services

Services are used by other higher level components to resolve more complex requests. I have 4 services:

- `WebSocketAuthenticatorService:` Authenticates new websocket connect requests.
- `UserService:` Interacts directly with the `UserRepository` and is used by other components to access users.
- `GameService:` Keeps track of all the running games and resolves requests that control the flow games like creating
  and starting a game.
- `PlayerService:` After a user joins a game, they're assigned a `PlayerController`
  objects by which they can control their character in the game and navigate through the map.

## Entities

Entity classes are POJOs that define the structure of the requests that's expected from the frontend and Spring can
translate incoming requests from JSON to those model classes automatically.

## Repositories

In this project, I only have one repository, the `UserRepository` which currently only stores the login credentials for
users but can be used in the future to create a profile for each user and track their games, wins, loses, etc.  
The `UserRepository` is a Mongo repository which is automatically configured by Spring.

## Security

The `AuthChannelInterceptorAdapter` class intercepts new websocket connect requests and forwards them to
the `WebSocketAuthenticatorService` which checks the user credentials and tries to match them with user credentials from
the `UserRepository`. If the credentials provided by the user in the request don't match any credentials in the
repository, the connection is rejected.

## Game

The game was designed as an API that's completely decoupled from the layers above it. The game has many parts with
different responsibilities so we'll discuss each of them:

### Game Class

The Game class can be thought of as a **<ins>Mediator</ins>** as it controls the interaction of the different components
of the game and controls the flow of the game. We'll discuss each of the components it interacts with separately.

### Maps

- **Connecting Rooms:**
  Maps in this game consist of a set of rooms that are connected together in a graph. Players can go between rooms by
  passing through doors. I decided to structure this by having each room point at its doors and each door point at the
  next room. This makes constructing and serializing maps somewhat challenging though because it means we could have
  circular references. A simpler approach to this would be having doors just hold the id of the neighboring room and
  keeping a list of all of the rooms. So, when a player moves through a door we can get the next room from that list by
  its id. This doesn't seem like a clean solution in my opinion though because it would ruin the symmetry in the design
  and make it harder to trace the code.  
  The first design also better because doors can prevent players from getting a reference to the next room if they're
  locked which is more secure.
- **Doors:**
  An intuitive approach to structuring doors would be having the door point at both of the rooms it connects. And that
  door would be shared between the two rooms. But, I didn't like this solution because when a player object tries to
  pass through a door it has to pass the room it's coming from so the door can return the other room. Which doesn't seem
  like a clean solution and also I would need to handle the case where the passed room is different than the two rooms
  the door object already has, which could cause a runtime error that can't be handled.  
  Instead, I replaced every door with a pair of doors where every room has a door object that points the opposite room.
  So, when a player object tries to pass through a door it just needs to call `Door.getNextRoom()` without having to
  pass its current room. I was happier with this solution but it presented a problem, since doors can be locked and
  unlocked with keys, door pairs have to share the state of being locked or unlocked so I created a `Lock` class and
  every door pair share a `Lock` object. So, when one of the doors gets unlocked for example the other door will be
  unlocked too.

### World Map Generator

The game requirements state that each the map should at least have 50 rooms and that there shouldn't be a limit for how
many players can join a game so, a map generator that creates maps randomly was needed. The map generator consists of
multiple class:

- `WorldMapGenerator:` The main class which connects all of the components the map generator and defines the algorithm.
- `DifficultyLevel:` This interface defines the variables in the map generation and the probabilities of certain events
  happening in the generation process.  
  This allows the addition of multiple difficulty levels and letting the user choose one on the game creation.
  Currently, the game only has one `DifficultyLevel`, the `DefaulyDifficultyLevel`.
- `GameRandomizer:` The class is the only one that interacts directly with the specified `DifficultyLevel`. It gives a
  convenient interface for randomizing events in the generation process like getting a random key or if a door should be
  locked or not.
- `EntityGenerator:` This class is concerned with generating entities that could be placed in the rooms. Currently, the
  only entities in the game are `Observables` and it handles generating those by having a list of random generators, one
  for each type of `Observables` and when a new one is requested it picks one randomly and generates a
  random `Observable`.
- `GameBuilder:` The game builder isn't part of the generator package as it could be useful for other things like a
  feature that would allow users to create custom maps and play with friends. It keeps track of all of the created rooms
  and handles connecting rooms through doors.

### Player

The player is at the center of the game. It interacts with almost all of the components in the game. So, managing it in
a clean way is really hard as it was really easy for it to become overcomplicated. I started by making a manager class
for each type of operations the player could do. The player class has seven managers which each can manage certain type
of operations. And two other class, Inventory and Location, which define the state of the player and are shared between
managers so they can manipulate them.

- **The Interface:**
  Now I needed a way to let other classes interact with the managers so at first the player class acted as a **<ins>
  Facade</ins>**. I defined a method for every operation that could be requested from the player in the player class and
  it would redirect that request to the appropriate manager but that got redundant really quick because, as mentioned
  before, there are a huge number of operations a player can do and the player class started turning into a god object.
  So instead, I exposed all of the managers through methods and classes that use the player class need to call the
  appropriate manager in method calls like this `player.navigate().turnRight()`.   
  This increases the coupling between components of the game as manager might be though of as implementation details but
  this allowed to make the player class a lot simpler and easier to read.

- **Commands:**
  I needed a way to decouple the player class from the outer classes because each user gets assigned a player object
  when they join a game and the `PlayerService`
  directs each requests from the user to their player object. Moreover, I needed a way to check if the player is in the
  correct state to execute a command and adding a check statement in each method isn't a great solution. So I used a
  combination of the **<ins>Command</ins>** pattern and the **<ins>State</ins>** pattern by creating a command object
  for each command which acts as a decoupling layer between the player class and the player service and each command
  defines the state it could be executed in. I then created a `PlayerController` class which could take commands and
  execute them making the `Player` class totally decoupled from the `PlayerService`. It also puts the commands in a
  queue and executes them one by one making sure no concurrency issues can happen as requests from the user could arrive
  asynchronously.

- **The Inventory:**
  The Inventory has to hold various kinds of items. Currently the game has `Flashlight`, `GoldBag` and `Key` which are
  considered to be inventory items, Each of which are stored inside the `Inventory` class separately. So adding and
  accessing those items from the `Inventory` is dealt with differently for each type. So, I decided to go with
  the **<ins>Visitor</ins>** design pattern here as it serves the purpose well.

### Fighting

The game requirements state that if two players walk into the same room, they have to get into a fight. It also needs to
check if a player enters a winning room and wins the game. So, the game needed a way to keep track of who's moving
where. So, players have to request move operations from the game so it could do the needed checks. I created
a `FightTracker` which keeps track of who was the last person to enter each room so if someone else enters it, it'll
know and it creates a new `Fight` between the two players. For the case where three or even more players enter the same
room, the `FightTracker` links `Fight` objects together so that when a fight ends, it notifies the one after it with the
winner of the fight.

# Clean Code

For this book, I'll go through the points that apply to this project from chapter 17 `Smells and Heuristics` and defend
my code against them. A lot of them are self-explanatory so I'll just list them without description.

## Comments

- **C1-5:**  
  I didn't have to put any comments in code because I made sure to use descriptive names and wrote it such that it's
  self-explanatory.

## Functions

- **F1: Too Many Arguments**  
  The vast majority of my functions are niladic and monadic. I have a couple of dyadic functions and very rarely
  anything above that.
- **F2: Output Arguments**  
  I avoided using any Output Arguments. For example, in the design of the `check` command I could have made it such that
  the checkable object takes the Inventory of the player as an argument and moves any items it has to it. But I avoided
  that option because it would violate this principle and instead decided to use the Visitor design pattern.  
  One method that could be thought of to violate this principle is the `sellItemToPlayer()` in the `Seller` class which
  takes the
  `GoldBag` of the player as an argument and takes the price of the sold item from it. But I decided to do this so
  the `Seller` class can verify that the player has enough gold to complete the purchase instead of just "trusting" the
  player for it which is more secure.
- **F3: Flag Arguments**  
  I made sure not to use an flag arguments. Instead, I resorted to creating two functions, one for each case, as
  recommended in the book.
- **F4: Dead Function**  
  Intellij helps with this one as it warns me about any unused functions or variables so, I made sure I don't have any.

## General

- **G1: Multiple Languages in One Source File**  
  I didn't have to worry about this in the backend as it's written purely in Java.  
  On the other hand, the frontend is written in both HTML and Javascript so I made sure to split them in separate files.
- **G4: Overridden Safeties**  
  I didn't override any safeties and made sure to deal with any warnings I get from the compiler or Intellij.
- **G5: Duplication**

- **G6: Code at Wrong Level of Abstraction**  
  Defending your code against this point isn't an easy task as there are many ways it could go wrong. I tried my best to
  satisfy it by using interfaces wherever it's possible and using design patterns that help with that.
- **G7: Base Classes Depending on Their Derivatives**
- **G8: Too Much Information**  
  My `Player` class has a very intensive interface like explained earlier but that's necessary because it matches the
  requirements of the project and has to offer a method for each command. Otherwise, all classes have a fairly sized
  interface reducing the coupling between the components of the project.
- **G9: Dead Code**  
  As with point **F4**, Intellij warns me about any unreachable code and I made sure to deal with any of those. There
  are some else statements that are currently not reachable like:
  ```java
  public Room getNextRoom() {
    if (canPassThrough()) {
      return nextRoom;
    } else {
      throw new ItemIsLockedException();
    }
  }
  ```
  These are in place to check that the program is running correctly and to make sure that these components are used the
  right way if new updates are added to the game later on.
- **G10: Vertical Separation**  
  Private methods are defined as close as possible to their first invocation and separated by at most one or two other
  methods.
- **G11: Inconsistency**  
  Consistency in large projects like this isn't easy. I tried my hardest to be consistent with my variables names and
  made sure variables that serve the same purpose share the same name.
- **G12: Clutter**
  I tried to have as little clutter as possible in my code. Lombok annotations helped with this as they offer
  annotations like `@AllArgsConstructor`
  which automatically injects a constructor with all variables in the class.
- **G13: Artificial Coupling**  
  I made sure to not have any unnecessary coupling between classes.
- **G14: Feature Envy**  
  This shows up in my code in each of the player manager classes as they access the `Inventory` and `Location` a lot.
  But as explained earlier this was necessary to keep the `Player` class simple.
- **G15: Selector Arguments**  
  Same with point **F3** I made not to use any selector arguments in my methods. Only place I have boolean arguments is
  in constructors where I need to specify a property of the object on creation.
- **G17: Misplaced Responsibility**  
  Each class has a specific responsibility to serve, if a method doesn't fall under this responsibility, it's moved to a
  smaller, lower level class.
- **G18: Inappropriate Static**  
  I rarely use static methods because I think most of the time they're work arounds so I didn't have a problem with
  this.  
  Most of my static methods are factory methods which are considered a good practice and really useful.
- **G19: Use Explanatory Variables**  
  I used explanatory variables wherever it was needed, most often when dealing with external libraries. I tried to
  resort having good names for my methods that could be used in if statements.
- **G20: Function Names Should Say What They Do**  
  As with variable names, methods names were chosen carefully to try and make code as readable as possible.
- **G23: Prefer Polymorphism to If/Else or Switch/Case**
- **G24: Follow Standard Conventions**
- **G28: Encapsulate Conditionals**
- **G29: Avoid Negative Conditionals**  
  A lot of methods used the `isLocked()` method from the `LockedWithKey` abstract class in a Transitive Navigation
  manner so I created another method `isUnlocked()` and replaced all of those calls with it.
- **G30: Functions Should Do One Thing**
- **G32: Don’t Be Arbitrary**
- **G33: Encapsulate Boundary Conditions**
- **G34: Functions Should Descend Only One Level of Abstraction**

## Java

- **J1: Avoid Long Import Lists by Using Wildcards**  
  Google code style guide [prohibits](https://google.github.io/styleguide/javaguide.html#s3.3.1-wildcard-imports) the
  use of wildcard imports so I decided to not follow this principle.

- **J3: Constants versus Enums**  
  I avoided using constant integer codes even though they are attractive to use. In the "Karel" assignment I represented
  directions with integer constants and which then I could do mathematical operations on to modify. For example
  the `turnLeft()` method looked like this:

  ```java
  public void turnLeft() {
    direction++;
    direction %= 4;
  }
  ```  

  But now I realized that this is way less readable. I even had to put a comment on it to explain it, which proves the
  point here. So, this time I used an Enum to represent directions instead.

## Names

I tried to be very mindful about my names. I made sure my names are descriptive and express exactly what a function does
or a variable represents without using any encodings. My names aren't perfect by any means and I still got a lot to
learn but I think I made a lot of progress compared to the names I used to choose in the past.

- **N1: Choose Descriptive Names**
- **N2: Choose Names at the Appropriate Level of Abstraction**
- **N3: Use Standard Nomenclature Where Possible**
- **N4: Unambiguous Names**
- **N5: Use Long Names for Long Scopes**
- **N6: Avoid Encodings**
- **N7: Names Should Describe Side-Effects**

# Effective Java

This book presents a lot of useful tools that improve Java programs and make them a lot more expandable and reusable.  
Here is a list of the items I applied in my code:

- **Item 1: Consider static factory methods instead of constructors**  
  I used static factory methods in multiple occupations. Most notably in the `Key` class where I need to keep track of
  all the keys have been created to make sure that no duplicates exist.
- **Item 2: Consider a builder when faced with many constructor parameters**  
  As mentioned before I created a `MapBuilder` because maps are complicated and have to constructed in many steps.
- **Item 3: Enforce the singleton property with a private constructor or an enum type**  
  I made the `ResourcesManager` a singleton with a static factory method to make sure there's only one instance of it in
  the program. That way it can monitor reads and writes and make sure no overlapping operations can happen.
- **Item 6: Avoid creating unnecessary objects**  
  Like mentioned before, I applied this in the `Key` class by providing static factory methods to ensure the code
  doesn't create duplicates.
- **Item 8: Avoid finalizers and cleaners**  
  No finalizers were used in my code, the only cleaning that needs to be done is when a player loses or when a game ends
  where the `PlayerService`
  and the `GameService` need to remove these objects so they can be collected by the garbage collector. That was done
  through listeners where the `Game` and `Player` classes notify the services above them when they need to be removed.
- **Item 10: Obey the general contract when overriding equals**  
  I needed to override the equals method in multiple classes and made sure it doesn't violate any of the points
  mentioned in the book.  
  I used this format for all of them:
  ```java
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    Player player = (Player) obj;
    return Objects.equals(id, player.id);
  }
  ```
- **Items 15-17:**  
  Almost all variables are declared as private and they're declared final if possible, setters and getters are not
  provided unless it's absolutely necessary and methods are declared private unless then have to accessed outside the
  class.
- **Item 20: Prefer interfaces to abstract classes**  
  I only have two abstract classes: `LockedWithKey` and `KeyHidingSpot` because they provide an implementation that
  would be otherwise duplicated in the concrete classes.
- **Item 22: Use interfaces only to define types**
  None of my interfaces define any variables as those should be considered as implementation details and shouldn't be
  defined by interfaces.  
  Wherever it felt like there should be a shared implementation to avoid repetition, I resorted to using abstract
  classes.
- **Item 23: Prefer class hierarchies to tagged classes**  
  This can be shown in the `Room` class as at first I had a boolean variable `lightSwitch` indicating whether the room
  has a light switch or not. But instead, I created a subclass `RoomWithLightSwitch` and removed the boolean variable
  from `Room`.
- **Item 25: Limit source files to a single top-level class**
  All source files were limited to a single class.
- **Item 34: Use enums instead of int constants**  
  Enums were used wherever it seemed appropriate and int constants were totally avoided.
- **Item 52: Use overloading judiciously**  
  I used overloading in the implementation of the visitor design pattern. It was explained earlier why this was
  important.
- **Item 58: Prefer for-each loops to traditional for loops**  
  Traditional for loops are almost never used. The only exception for this is in the generator because it has a fairly
  complicated algorithm and needs to manipulate low level data types.
- **Item 61: Prefer primitive types to boxed primitives**  
  Boxed primitives are really rarely used. They're only used as keys in HashMaps to map players to their ids.
- **Item 64: Refer to objects by their interfaces**  
  This shows best in the way the player class interacts with the map items as it doesn't interact with any of them with
  their concrete type but instead interacts with them through an appropriate interface which makes adding new items to
  the game really easy since the player class wouldn't have to change at all.
- **Items 69-71:**  
  I made sure to use exceptions in such a way that they don't obscure the flow of the code. I only use unchecked
  exceptions for cases where an error happens and the code shouldn't try to recover from them.  
  There's one exception to this with the `ItemIsLockedException` which could get thrown when a player requests to move
  through a door and that's because of concurrency issues which could cause a door to get locked while a player is
  trying to move through it.

# SOLID

## Single-responsibility principle

I made sure all of my classes only have one job and one reason to change which decreases the coupling of the system and
makes adding changes to it a lot easier and a less complicated. It also makes the code more readable and easier to
understand because it creates a level of abstraction at each layer.  
I did that by breaking down large classes to smaller classes so that each can handle one of the responsibilities of the
original class. Also by breaking down large methods down to smaller ones as well, with each of them operating on a
single level of abstraction.  
Some design patterns helped with this as well like the Mediator and the Facade design patterns.

## Open–closed principle

This principle isn't easy to apply perfectly because for it to be applied programmers have to try and predict possible
future changes to the code and resolve them by introducing some level of abstraction that would make adding these
changes easy and not require editing existing code.  
The best way to apply the OCP is through polymorphism removing the coupling between components and allowing the addition
of new ones that can fit seamlessly in the system.  
This is best demonstrated in my player manager classes as they avoid interacting directly with the map items and instead
through interfaces that are meant to bring out a single property of the object. These interfaces are:

- `Observable` which is implemented by items that can the `look` command can be applied to and it includes all of the
  items in the game.
- `Checkable` which is implemented by items that the `check` command can be applied to.
- `PassThrough` which represents items that can move players between rooms and currently limited to the `Door` class.
- `Container` which represents items that can hold a set of `InventoryItems` and currently it's only implemented by
  the `Chest` class.

And a couple of abstract classes that help achieving the same goal as well:

- `KeyHidingSpot` which represents items that can hold a `Key` object, implemented by the `Painting` and `Mirror`
  classes.
- `LockedWithKey` which represents items that can require a `Key` to be accessed, implemented by the `Door` and `Chest`
  classes.

## Liskov substitution principle

This principle defines that a superclass should be replaceable with its subclasses without breaking the program. It
implies that an overridden method shouldn't be restrictive than the super method.  
This can be demonstrated in the `RoomWithLightSwitch` class which extends the `Room` class. The `RoomWithLightSwitch`
doesn't take away the ability to see items in the dark with a `Flashlight` but it adds the option of turning the light
switch to see items without a `Flashlight`.

## Interface segregation principle

Robert Martin defines this principle with "Clients should not be forced to depend upon interfaces that they do not use"
.  
I made sure apply this by having each interface represent a represent a single property and making it include only
methods that are essential for that property.

## Dependency inversion principle

According to Robert Martin Dependency inversion consists of these parts:

- High-level modules should not depend on low-level modules. Both should depend on abstractions.
- Abstractions should not depend on details. Details should depend on abstractions.

I applied this by defining an interface of methods for each component that other components can interact with it
through. This means the inner implementation of a component can be changed without it affecting the rest of the system.
It also means a component can be easily reused in other parts if the system.

# Code Styling

Writing this project, I decided to follow
the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).  
First off, I installed the google-java-format plugin on Intellij which did most of the work for me. Ensuring I have the
right indentation, optimizing the imports, keeping the column limit, etc. But there are a couple of things that the
Plugin couldn't fix for me so I had to make sure to follow some of the rules, like:

- I made sure to use braces with block-like construct even when the body is empty or contains only a single statement.
  For example, I had to edit this code:

```java
  if(keys.containsKey(keyName))return keys.get(keyName);
        else return createKey(keyName);
```

To this:

```java
  if(keys.containsKey(keyName)){
        return keys.get(keyName);
        }else{
        return createKey(keyName);
        }
```

- I made sure to use the CONSTANT_CASE for constant names. For example, I renamed the list of reserved key names in
  the `Key` class to `RESERVED_NAMES`
  because this rule applies to it: "Constants are static final fields whose contents are deeply immutable and whose
  methods have no detectable side effects".

# Scalability & Utilization

Utilization was a great focus in this project. The project was designed to serve request as quick as possible and have
the least amount of thread blocking. That was achieved by using optimized algorithms, appropriate data structures and
also by queuing player commands and serving them synchronously. Also by using a `ThreadPoolTaskScheduler` which's shared
throughout the code which allows achieving maximum utilization.  
As for scalability, horizontal scaling can't be applied because I'm using a simple in-memory message broker which means
if a player connects to an instance, other instances can't interact with them, and by the nature of the game this is a
problem. But, it can be easily fixed by using a more sophisticated message broker service like RabbitMQ or ActiveMQ
which could run independently and would allow instances to send messages to users that have their websocket connection
on other instances.

# Data Structures

I mainly used three types of data structures in my project so I'll list them and explain why I decided to use each of
them:

- **Arrays:**
  I used arrays where the size of the structure is fixed.
- **ArrayLists:**
  I used ArrayLists in scenarios where objects need to be added dynamically to the structure. For example,
  the `Inventory` where items need to added and removed continuously.
- **HashMaps:**
  These were useful in cases where I needed to find objects quickly using IDs or names. For example, the `Trader` class
  has one that maps each item to its type, so when a user requests to buy an item, it could be found quickly with its
  type.  
  I also used a more specialized type of maps `EnumMap` that allows the use of enum type keys. I used it in the `Room`
  class to map each `Direction`
  to its corresponding item on the room walls.
