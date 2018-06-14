package com.marco.beautypuzzle;

/**
 * User: KdMobiB
 * Date: 2016/7/4
 * Time: 15:12
 */
public interface GamePuzzleListener {
    void nextLevel(int nextLevel);

    void timechanged(int currentTime);

    void gameover();

    void gamesuccess();
}
