package mcts

import scala.util.Random

/**
  * Created by culim on 2/24/16.
  */
object UCT {

  def search(rootState: GameState, maxIterations: Int, verbose: Boolean = false): Int = {
    val rootNode = GameNode(state = rootState)

    var node: GameNode = null;
    var state: GameState = null;
    for (iteration <- 1 to maxIterations) {
      node = rootNode           //restore the node to rootNode (starting node, the game position where we start to search the best move)
      state = rootState.getCopy //restore the state on each iteration

      // Select
      while (node.untriedActions.isEmpty && node.children.nonEmpty) { //Here it can descend the tree to select the most rewarding node
        // println("select1::  untriedActions = " + node.untriedActions.size   +  "    children =  " + node.children.size   + "   v= " + node.numberOfVisits  +"  w= "+ node.numberOfWins + "  " + node.state  + "   " + node.level)
        // Node has exhausted all actions -- fully expanded
        // Node is non-terminal -- still has children to explore
        node = node.selectChild
        state.doAction(node.action)
        // println("select2::  untriedActions = " + node.untriedActions.size   +  "    children =  " + node.children.size  + "   v= " + node.numberOfVisits  +"  w= "+ node.numberOfWins + "  " + node.state  + "   " + node.level)
      }

      // Expand
      //          each time you are at a node, you expand a node according to these rules :
      //          if a child node has never been expanded before, then expand one of the unexplored child at random (and you can immediately unwind from this child node)
      //          otherwise, each child node has been visited at least once. Compute for all of them the "exploration/exploitation" value and expand the child node with highest value
      //            The idea of MCTS is maximizing the exploration/exploitation. If a child node has never been explored before, the "exploration" value associated with it is infinite,
      // you will have to explore it. However, once you have expanded all child nodes, then you will expand more frequently the child nodes with higher value (this is the "exploitation" part)
      //
      if (node.untriedActions.nonEmpty) {
        // println("Expand::  untriedActions = " + node.untriedActions.size   +  "    children =  " + node.children.size  + "   v= " + node.numberOfVisits  +"  w= "+ node.numberOfWins + "  " + node.state  + "   " + node.level)
        val action: Int = node.untriedActions.toList(Random.nextInt(node.untriedActions.size))
        state.doAction(action)
        node.addChild(action, state)
      }

      // Rollout
      while (state.getAvailableActions.nonEmpty) {
        state.doAction(state.getAvailableActions.toList(Random.nextInt(state.getAvailableActions.size)))
      }

      // Backpropagate
      //  println("After roll out::  untriedActions = " + node.untriedActions.size   +  "    children =  " + node.children.size  + "   v= " + node.numberOfVisits  +"  w= "+ node.numberOfWins + "  " + node.state  + "   " + node.level)
      while (node != null) {
        node.update(state.getResult(node.playerIndex))
        node = node.parent
      }
      // println("After Backpropagate::  untriedActions = " + node.untriedActions.size   +  "    children =  " + node.children.size  + "   v= " + node.numberOfVisits  +"  w= "+ node.numberOfWins + "  " + node.state  + "   " + node.level)


    }

    if (verbose) {
      println(rootNode.treeToString(0))
    }
    else {
      println(rootNode.childrenToString());
    }

    return rootNode.children.sortBy(_.numberOfVisits).last.action
  }

}
