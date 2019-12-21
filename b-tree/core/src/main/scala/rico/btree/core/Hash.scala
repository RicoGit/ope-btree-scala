package rico.btree.core

import java.nio.ByteBuffer

import cats.kernel.Eq
import monocle.Iso
import scodec.bits.ByteVector

/** Any data hash */
case class Hash(bytes: Array[Byte]) extends AnyVal {

  def copy: Hash = Hash(bytes.clone())

  override def toString: String =
    if (bytes.isEmpty) "Hash(empty)" else s"Hash(${bytes.length} bytes, hex=${ByteVector.view(bytes).toHex})"

}

object Hash {

  def empty: Hash = Hash(Array.emptyByteArray)

  implicit def hashCodec: Iso[Hash, Array[Byte]] = Iso[Hash, Array[Byte]](_.bytes)(Hash(_))

  implicit val hashEq: Eq[Hash] = { (k1, k2) ⇒
    ByteBuffer.wrap(k1.bytes).equals(ByteBuffer.wrap(k2.bytes))
  }

  implicit class HashOps(originHash: Hash) {

    def isEmpty: Boolean = originHash.bytes.isEmpty

    def concat(hash: Hash): Hash = Hash(Array.concat(originHash.bytes, hash.bytes))

    def concat(hash: Array[Hash]): Hash = Hash(Array.concat(originHash.bytes, hash.flatMap(_.bytes)))

    def asByteVector: ByteVector = ByteVector(originHash.bytes)

  }

  implicit class ArrayHashOps(hashes: Array[Hash]) {

    /**
      * Returns an updated copy of hash array with the updated element for ''insIdx'' index.
      * We choose variant with array copying for prevent changing input parameters.
      * Work with mutable structures is more error-prone. It may be changed in the future by performance reason.
      */
    def rewriteValue(newElement: Hash, idx: Int): Array[Hash] = {
      val newArray = hashes.clone()
      newArray(idx) = newElement
      newArray
    }

    /**
      * Returns an updated copy of hash array with the inserted element at the specified position(''insIdx'').
      * Current array will grow up by one.
      */
    def insertValue(newElement: Hash, idx: Int): Array[Hash] = {
      val newArray = new Array[Hash](hashes.length + 1)
      // copying init of array
      Array.copy(hashes, 0, newArray, 0, idx)
      // insert element to the empty slot
      newArray(idx) = newElement
      // copying tail of array
      Array.copy(hashes, idx, newArray, idx + 1, hashes.length - idx)

      newArray
    }

  }

}
