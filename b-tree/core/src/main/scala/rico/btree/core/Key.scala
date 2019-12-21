package rico.btree.core

import monocle.Iso
import scodec.bits.ByteVector

/**
  * Ciphered btree key
  */
case class Key(bytes: Array[Byte]) extends AnyVal {

  def copy: Key = Key(bytes.clone())

  override def toString: String =
    if (bytes.isEmpty) "Key(empty)" else s"Key(${bytes.length} bytes, hex=${ByteVector.view(bytes).toHex})"

}

object Key {

  implicit val keyCodec: Iso[Key, Array[Byte]] = Iso[Key, Array[Byte]](_.bytes)(Key(_))

  implicit class KeyOps(originKey: Key) {

    def isEmpty: Boolean = originKey.bytes.isEmpty

    def toByteVector: ByteVector = ByteVector(originKey.bytes)

  }
}
