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

## Game Objective
Navigate through all rooms, win card battles against monsters, and reach the treasure room to complete the game.