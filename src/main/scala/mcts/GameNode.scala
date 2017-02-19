package mcts

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Set
/**
  * Created by culim on 2/24/16.
  */
case class GameNode(action : Int = -1, parent : GameNode = null, state : GameState = null) {

    val level: Int = if (parent==null) 0 else parent.level + 1
    var numberOfWins : Double = 0
    var numberOfVisits : Int = 0
    val children : ListBuffer[GameNode] = ListBuffer.empty
    val untriedActions : Set[Int] = state.getAvailableActions
    val playerIndex : Int = state.getLastPlayerWhoMoved
    val epsilon : Double = 1e-6

    def selectChild : GameNode = children.map( node => (node,
            (node.numberOfWins/node.numberOfVisits) +
                Math.sqrt(2 * Math.log(numberOfVisits+1) / (node.numberOfVisits+epsilon))
        )).sortBy(_._2).last._1


    def update(result : Double) : Unit = {
        numberOfVisits += 1
        numberOfWins += result
    }

    def addChild(action : Int, state : GameState) : GameNode = {
        val n =  GameNode(action, this, state)
        untriedActions -= action
        children += n
         n
    }

    override def toString : String =
          s"[A: $action; " +
                s"W/V: $numberOfWins/$numberOfVisits = ${numberOfWins/numberOfVisits}; " +
                s"U: $untriedActions"



    def treeToString(indent : Int  ) : String = {
        var s : String = indentString(indent) + this.toString()
        for (c <- children) {
            s += c.treeToString (indent + 1)
        }

        return s
    }

    def indentString(indent : Int) : String = "\n" + " | " * indent


    def childrenToString() : String = children.mkString("\n")

}
