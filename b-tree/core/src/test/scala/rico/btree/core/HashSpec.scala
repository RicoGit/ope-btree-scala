package rico.btree.core

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HashSpec extends AnyWordSpec with Matchers {

  "Hash.copy" should {
    "make physical copy of array inside" in {
      val array = "some".getBytes

      val hash = Hash(array)
      hash.bytes == array shouldBe true

      val hashCopy = hash.copy
      hashCopy == hash shouldBe false
      hashCopy.bytes == array shouldBe false
    }
  }

  "Hash.hashCodec" should {
    "transform Hash to Array[Byte] and back" in {
      val bytes = "some hash".getBytes

      val result = Hash.hashCodec.get(Hash.hashCodec.reverseGet(bytes))
      result shouldBe bytes
    }
  }

  "Hash.hashEq" should {
    "return true for 2 Hash with the same byte arrays" in {
      val hash1 = Hash("some hash".getBytes)
      val hash1copy = Hash("some hash".getBytes)
      val hash2 = Hash("some hash2".getBytes)
      val emptyHash = Hash.empty
      val emptyHashCopy = Hash(Array.emptyByteArray)

      Hash.hashEq.eqv(hash1, hash1) shouldBe true
      Hash.hashEq.eqv(hash1, hash1copy) shouldBe true
      Hash.hashEq.eqv(emptyHash, emptyHashCopy) shouldBe true

      Hash.hashEq.eqv(hash1, emptyHash) shouldBe false
      Hash.hashEq.eqv(hash1, hash2) shouldBe false
    }
  }

  "HashOps.isEmpty" should {
    "return true is underlying array is empty" in {

      Hash.empty.isEmpty shouldBe true

      Hash("some hash".getBytes).isEmpty shouldBe false
    }
  }

  "HashOps.concat" should {
    "concatenates for underlying arrays" in {

      val hash1 = Hash("some hash-".getBytes)
      val hash2 = Hash("some hash2".getBytes)

      val result = hash1.concat(hash2)
      new String(result.bytes) shouldBe "some hash-some hash2"

      Hash.empty.concat(Hash.empty).bytes shouldBe Array.emptyByteArray
    }

    "concatenates for hash array to underlying array" in {

      val hash = Hash("some hash".getBytes)
      val hashes = Array(Hash.empty, Hash(" ".getBytes), Hash("some hash2".getBytes), Hash("!".getBytes))

      val result = hash.concat(hashes)
      new String(result.bytes) shouldBe "some hash some hash2!"
    }
  }

  private val hashes = Array(Hash("a".getBytes), Hash("b".getBytes), Hash("c".getBytes))

  "ArrayHashOps.rewriteValue" should {
    "copy underlying array and rewrite specified value" in {

      hashes.rewriteValue(Hash("X".getBytes), 0).asStr should contain theSameElementsInOrderAs Seq("X", "b", "c")
      hashes.rewriteValue(Hash("X".getBytes), 1).asStr should contain theSameElementsInOrderAs Seq("a", "X", "c")
      hashes.rewriteValue(Hash("X".getBytes), 2).asStr should contain theSameElementsInOrderAs Seq("a", "b", "X")
      hashes.rewriteValue(Hash.empty, 1).asStr should contain inOrder ("a", "", "c")
    }

    "throw" in {
      assertThrows[ArrayIndexOutOfBoundsException] {
        Array.empty[Hash].rewriteValue(Hash("X".getBytes), 0)
      }
    }

  }

  "ArrayHashOps.insertValue" should {
    "copy underlying array and insert specified value" in {

      hashes.insertValue(Hash("X".getBytes), 0).asStr should contain theSameElementsInOrderAs Seq("X", "a", "b", "c")
      hashes.insertValue(Hash("X".getBytes), 1).asStr should contain theSameElementsInOrderAs Seq("a", "X", "b", "c")
      hashes.insertValue(Hash("X".getBytes), 2).asStr should contain theSameElementsInOrderAs Seq("a", "b", "X", "c")
      hashes.insertValue(Hash("X".getBytes), 3).asStr should contain theSameElementsInOrderAs Seq("a", "b", "c", "X")
      hashes.insertValue(Hash.empty, 1).asStr should contain theSameElementsInOrderAs Seq("a", "", "b", "c")

      Array.empty[Hash].insertValue(Hash("X".getBytes), 0).asStr should contain theSameElementsInOrderAs Seq("X")
    }
  }

  "ArrayHashOps.asByteVector" should {
    "copy underlying array into ByteVector" in {
      val array = "some".getBytes
      val hash = Hash(array)
      hash.bytes sameElements array

      val asBVector = hash.asByteVector
      asBVector.toArray sameElements hash.bytes
      // change underlying array
      array(0) = 'z'
      asBVector.toArray == hash.bytes shouldBe false
    }
  }

  private implicit class Hashes2Strings(hashArr: Array[Hash]) {
    def asStr: Array[String] = hashArr.map(h => new String(h.bytes))
  }

}
