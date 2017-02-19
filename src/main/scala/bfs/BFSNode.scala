package bfs

/**
  * Created by culim on 2/25/16.
  */
case class BFSNode( id : Int ) {
    override def equals(other: scala.Any) = other match{
      case bsfNode: BFSNode => id == bsfNode.id
      case _ => false
    }

    override def hashCode =id
}
