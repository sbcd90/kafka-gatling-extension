package io.gatling.data.generator

import org.apache.avro.Schema
import org.apache.avro.Schema.Type
import org.apache.avro.generic.GenericData.Record
import org.apache.avro.generic.GenericRecord

class RandomDataGenerator[K: Manifest, V: Manifest] {
  private final val ByteManifest = manifest[Byte]
  private final val ShortManifest = manifest[Short]
  private final val IntManifest = manifest[Int]
  private final val LongManifest = manifest[Long]
  private final val FloatManifest = manifest[Float]
  private final val DoubleManifest = manifest[Double]
  private final val CharManifest = manifest[Char]
  private final val StringManifest = manifest[String]
  private final val BooleanManifest = manifest[Boolean]
  private final val ByteArrayManifest = manifest[Array[Byte]]
  private final val AnyManifest = manifest[Any]
  private final val AnyRefManifest = manifest[AnyRef]
  private final val GenericRecordManifest = manifest[GenericRecord]

  def generateKey(schema: Option[Schema] = None): K = {
    manifest[K] match {
      case ByteManifest => 42.toByte.asInstanceOf[K]
      case ShortManifest => 42.toShort.asInstanceOf[K]
      case IntManifest => 42.asInstanceOf[K]
      case LongManifest => 42.toLong.asInstanceOf[K]
      case FloatManifest => 42.11.toFloat.asInstanceOf[K]
      case DoubleManifest => 42.11.asInstanceOf[K]
      case CharManifest => 'C'.asInstanceOf[K]
      case StringManifest => "Str".asInstanceOf[K]
      case BooleanManifest => true.asInstanceOf[K]
      case ByteArrayManifest => "Str".getBytes.asInstanceOf[K]
      case AnyManifest => 'C'.asInstanceOf[K]
      case AnyRefManifest => "Str".asInstanceOf[K]
      case GenericRecordManifest => generateDataForAvroSchema(schema).asInstanceOf[K]
      case x if x.runtimeClass.isArray =>
        Array(1, 2, 3, 4, 5).asInstanceOf[K]
    }
  }

  def generateValue(schema: Option[Schema] = None): V = {
    manifest[V] match {
      case ByteManifest => 42.toByte.asInstanceOf[V]
      case ShortManifest => 42.toShort.asInstanceOf[V]
      case IntManifest => 42.asInstanceOf[V]
      case LongManifest => 42.toLong.asInstanceOf[V]
      case FloatManifest => 42.11.toFloat.asInstanceOf[V]
      case DoubleManifest => 42.11.asInstanceOf[V]
      case CharManifest => 'C'.asInstanceOf[V]
      case StringManifest => "Str".asInstanceOf[V]
      case BooleanManifest => true.asInstanceOf[V]
      case ByteArrayManifest => "Str".getBytes.asInstanceOf[V]
      case AnyManifest => 'C'.asInstanceOf[V]
      case AnyRefManifest => "Str".asInstanceOf[V]
      case GenericRecordManifest => generateDataForAvroSchema(schema).asInstanceOf[V]
      case x if x.runtimeClass.isArray =>
        Array(1, 2, 3, 4, 5).asInstanceOf[V]
    }
  }

  protected def generateDataForAvroSchema(schema: Option[Schema]): GenericRecord = {
    if (schema.isEmpty) {
      throw new RuntimeException("schema is empty. Cannot generate record")
    }

    val length = schema.get.getFields.size()

    val avroRecord = new Record(schema.get)
    for (i <- 0 until length) {
      val field = schema.get.getFields.get(i)
      val fieldType = field.schema().getType

      fieldType match {
        case Type.BYTES => avroRecord.put(i, 42.toByte)
        case Type.INT => avroRecord.put(i, 42)
        case Type.LONG => avroRecord.put(i, 42.toLong)
        case Type.FLOAT => avroRecord.put(i, 42.11.toFloat)
        case Type.DOUBLE => avroRecord.put(i, 42.11)
        case Type.STRING => avroRecord.put(i, "Str")
        case Type.BOOLEAN => avroRecord.put(i, true)
        case Type.RECORD => avroRecord.put(i, generateDataForAvroSchema(schema))
      }
    }
    avroRecord
  }
}