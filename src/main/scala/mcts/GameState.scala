package mcts

import scala.collection.mutable.Set
trait GameState {

    def getCopy                         : GameState
    def getLastPlayerWhoMoved           : Int
    def getAvailableActions             : Set[Int]
    def getResult(playerIndex : Int)    : Double
    def doAction(action : Int)          : Unit

}


