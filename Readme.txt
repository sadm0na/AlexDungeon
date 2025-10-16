Структура 3х столпов данжеона (также есть класс Door.java и RoomData.java)

Dungeon.java (главный класс)
    - main() - точка входа
    - start() - игровой цикл while
    -  rooms[] - массив всех комнат
    - changeRoom() - смена комнат
    + содержит MyPanel и JFrame

MyPanel.java (визуализация)
    -  paintComponent() - рисует комнату
    -  dispatchKeyEvent() - обрабатывает клавиши
    - updateWorldPhysics() - меняет координаты (физика игры)

Alex.java (персонаж)
    - бегалка алекса


----

Dungeon.java, как работает главный цикл

       while(true) ────┐
            |          │
        repaint() ─────┼ MyPanel.paintComponent()
            |          │       └─ рисует бэкграунд текущей комнаты
            |          │       └─ рисует алекса
            |          |       └─ на данный момент рисует как дебаг его координаты сверху и красные двери
            |          │
    updateWorldPhysics()─── alex.update() (движение)
            |          │       └─ checkRoomTransition() (если E нажата)
            |          │             └─ dungeon.changeRoom()
        sleep(20) ─────┘                   └─ loadCurrentRoom() (новый фон! а вместе с этим и сундуки с ключами)
