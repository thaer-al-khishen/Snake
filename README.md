# Snake
A challenging android Snake game. Change the direction of the snake by swiping the screen. Note that it can't go in the opposite direction of its current course.

The changes I've made to the original repo:

1- Change the initial speed of the game and make it more challenging by incrementing the speed every 20 seconds until it reaches a maximum at 1 minute

2- Implement collision detection between the snake and each of the 4 walls which results in the snake losing a life upon going through any wall.

3- Implement red and blue boxes feature: The red box causes the snake to shrink in size by 1 if its length is greater than 2, note that the snake starts at a size of 2, so you need to gain a score of at least one before going to the red box or it won't shrink in size. On the contrary, the blue box causes the snake to increase in size by 1. Both of these boxes appear for only 3 seconds or until you take one of them, then, both disappear for 10 seconds. However, at the start of the game they will remain visible until you take one of them before this cycle of disappearing and reappearing continues.

4- Implement high score feature with SharedPreferences

5- Make the game more challenging by decreasing the size of the field from 20x10 to 14x7

6- Have the snake start off with 2 pieces instead of 3 

7- Modify the landing page to be in English
