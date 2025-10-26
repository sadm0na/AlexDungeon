# AlexDungeon

A hybrid adventure game combining room exploration with card-based combat.

## Requirements
- Java 8 or higher
- No external libraries needed

## How to Run
1. Clone the repository
2. Navigate to the project directory
3. Compile: `javac src/*.java`
4. Run: `java src/Dungeon`

## Game Structure
- **Exploration**: Move through rooms collecting keys and opening chests
- **Card Combat**: Defeat monsters in card mini-games to progress

## Controls
- **Movement**: W, A, S, D
- **Sprint**: Hold SHIFT while moving
- **Interact**: 
  - Take key: F
  - Open chest: K  
  - Enter door: E

## Game Objective
Navigate through all rooms, win card battles against monsters by collecting the required coins, and reach the treasure room to complete the game.

## Game Rules

### Room Progression
- To enter the next room, you must collect a key first
- All new rooms except the final treasure room trigger a card mini-game
- To complete a card game, you need to collect the required amount of coins
- Earn coins by defeating monsters in the card game

### Card Game Mechanics
- Your character card appears in the center of the card grid
- You can move to adjacent cards (up, down, left, right)
- When you click on an adjacent card, you attempt to occupy its position
- Your previous position will be replaced with a new random card

### Movement Rules
You can freely move to a new card position if:
- **Potion Card**: Restores your health by X HP. If your health + X exceeds maximum health, your health caps at the maximum value
- **Sword Card**: Grants attack power = X. Use this to attack monsters without losing your own health

### Combat System

**When attacking with a weapon:**
- If your weapon damage is more (or equal) than the monster's health, then you kill it, gain coins and you move to monsters position, your previous place is being replaced with a new random card

- If your weapon damage is less than the monster's health:
  - The monster takes full weapon damage but doesn't die
  - You don't move to the monster's position
  - Both you and the monster remain in your original positions

**When attacking without a weapon (hand-to-hand combat):**
- If your health is greater than the monster's health:
  - You defeat the monster and move to its position
  - Your health decreases by the monster's health value
- If your health is less than or equal to the monster's health:
  - You lose the card game and must restart it

## Features to Test

### Exploration
- Character movement with WASD
- Sprint functionality (SHIFT)
- Key collection (F key)
- Chest opening (K key)
- Door access with required keys (E key)
- Proper key generation and placement

### Card Game
- Random card generation (monsters, potions, swords)
- Adjacent card movement (up, down, left, right)
- Combat mechanics (HP comparison)
- Loot collection from defeated monsters
- Font display (may fall back to system default)

### Progression
- Room transition after winning card games
- Game completion upon reaching treasure room