package scorex.serialization

import com.google.common.primitives.{Bytes, Shorts}

object Deser {

  def serializeArray(b: Array[Byte]): Array[Byte] = Shorts.toByteArray(b.length.toShort) ++ b

  def parseArraySize(bytes: Array[Byte], position: Int): (Array[Byte], Int) = {
    val length = Shorts.fromByteArray(bytes.slice(position, position + 2))
    (bytes.slice(position + 2, position + 2 + length), position + 2 + length)
  }

  def parseByteArrayOption(bytes: Array[Byte], position: Int, length: Int): (Option[Array[Byte]], Int) = {
    if (bytes.slice(position, position + 1).head == (1: Byte)) {
      val b = bytes.slice(position + 1, position + 1 + length)
      (Some(b), position + 1 + length)
    } else (None, position + 1)
  }

  def parseOption[T](bytes: Array[Byte], position: Int)(deser: Array[Byte] => T): (Option[T], Int) = {
    if (bytes.slice(position, position + 1).head == (1: Byte)) {
      val (arr, arrPosEnd) = parseArraySize(bytes, position + 1)
      (Some(deser(arr)), arrPosEnd)
    } else (None, position + 1)
  }

  def parseArrays(bytes: Array[Byte]): Seq[Array[Byte]] = {
    val length = Shorts.fromByteArray(bytes.slice(0, 2))
    val r = (0 until length).foldLeft((Seq.empty[Array[Byte]], 2)) { case ((acc, pos), _) =>
      val (arr, nextPos) = parseArraySize(bytes, pos)
      (acc :+ arr, nextPos)
    }
    r._1
  }

  def serializeOption[T](b: Option[T])(ser: T => Array[Byte]): Array[Byte] = b.map(a => (1: Byte) +: serializeArray(ser(a))).getOrElse(Array(0: Byte))

  def serializeArrays(bs: Seq[Array[Byte]]): Array[Byte] = Shorts.toByteArray(bs.length.toShort) ++ Bytes.concat(bs.map(serializeArray): _*)
}
