package rico.btree.core

import scala.collection.Searching.SearchResult

/**
  * A structure with all client details needed for inserting key and value to the BTree.
  *
  * @param key          The key that will be placed to the BTree
  * @param valChecksum  The value checksum that will be placed to the BTree
  * @param searchResult A result of searching client key in server leaf keys. Contains an index
  *                     for putting specified key and value
  */
case class PutDetails(key: Key, valChecksum: Hash, searchResult: SearchResult)
