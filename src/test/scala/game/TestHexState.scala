package game

import org.scalatest.FunSuite

import scala.util.Random

/**
  * Created by culim on 2/24/16.
  */
class TestHexState extends FunSuite{

    test("A newly created HexState should just have a non-empty board.") {
        val nRows  = 11;
        val nColumns  = 11;
        val state  = new HexState(nRows, nColumns)

        assert(state.board.length == nRows * nColumns)
    }

    test("A 5 x 5 board where player 1 picks (0,a) should be valid.") {
        val nRows  = 5;
        val nColumns  = 5;
        val state  = new HexState(nRows, nColumns)

        state.doAction(0)

        assert(state.board(0) == 1, "Player 1 should be in position (a,0)")
    }

    test("A 5 x 5 board where player 1 picks (0,a) and player 2 picks (0, b) should be valid.") {
        val nRows = 5
        val nColumns  = 5
        val state  = new HexState(nRows, nColumns)

        state.doAction(0)
        state.doAction(1)

        assert(state.board(0) == 1, "Player 1 should be in position (a,0)")
        assert(state.board(1) == 2, "Player 2 should be in position (a,0)")
    }

    test("P1 and P2 alternate across 5 turns should reduce #available_actions.") {
        val nRows  = 5
        val nColumns  = 5
        val state  = new HexState(nRows, nColumns)


        val nTurns = 5
        for (i <- 1 to nTurns) {
            state.doAction(state.getAvailableActions.toList(Random.nextInt(state.getAvailableActions.size)))
            println(state)
            state.doAction(state.getAvailableActions.toList(Random.nextInt(state.getAvailableActions.size)))
            println(state)

        }


        assert(state.getAvailableActions.size == nRows*nColumns - nTurns*state.totalNumberOfPlayers)

    }

    test("P1 in winning configuration has won should be detected.") {
        val nRows = 5
        val nColumns = 5;
        val state  = new HexState(nRows, nColumns)

        state.board = Array(
            1, 1, 0, 0, 0,
             0, 1, 1, 0, 0,
              0, 0, 1, 0, 0,
               0, 0, 1, 1, 1,
                0, 0, 2, 2, 2
        )
        state.lastPlayerWhoMoved = 1

        val actual  = state.getPlayerInWinConditions._1
        val expected  = 1

        assert(expected == actual)
    }

    test("P2 in winning configuration has won should be detected.") {
        val nRows = 5
        val nColumns = 5;
        val state  = new HexState(nRows, nColumns)

        state.board = Array(
            1, 0, 2, 0, 2,
             0, 2, 1, 2, 0,
              0, 0, 2, 1, 0,
               0, 2, 1, 0, 0,
                2, 1, 1, 1, 0
        )
        state.lastPlayerWhoMoved = 2

        val actual  = state.getPlayerInWinConditions._1
        val expected  = 2

        assert(expected == actual)
    }

    test("P2 in making final move on 7x7 board should not result in a win.") {
        val nRows = 7
        val nColumns = 7
        val state  = new HexState(nRows, nColumns)

        state.board = Array(
            1, 1, 1, 1, 2, 2, 1,
             1, 1, 0, 2, 0, 0, 1,
              1, 2, 2, 1, 0, 0, 0,
               2, 1, 2, 1, 0, 0, 0,
                2, 2, 1, 2, 1, 2, 0,
                 2, 1, 1, 1, 0, 2, 2,
                  1, 1, 0, 2, 1, 2, 2
        )
        state.lastPlayerWhoMoved = 1
        val pair = state.getPlayerInWinConditions
        println(state)
        println(state.lastPlayerWhoMoved)
        println("Frank" + pair._2.mkString(","))
        assert(pair._1 == 0)
        state.doAction(39)

        val pair2 = state.getPlayerInWinConditions
        println(state)
        println(state.lastPlayerWhoMoved)
        println("Frank" + pair2._2.mkString(","))
        assert(pair2._1 == 0)

    }

}
