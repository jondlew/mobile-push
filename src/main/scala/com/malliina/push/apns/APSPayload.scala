package com.malliina.push.apns

import play.api.libs.json.Json._
import play.api.libs.json._

/**
  * @param alert Some(Left(...)) for a simple alert text, Some(Right(...)) for more verbose alert details, None for background notifications
  * @param badge badge number
  * @param sound rock.mp3
  */
case class APSPayload(
  alert: Option[Either[String, AlertPayload]],
  badge: Option[Int] = None,
  sound: Option[String] = None,
  category: Option[String] = None,
  threadId: Option[String] = None
)

object APSPayload {
  val Alert = "alert"
  val Badge = "badge"
  val Category = "category"
  val ContentAvailable = "content-available"
  val Sound = "sound"
  val ThreadId = "thread-id"

  implicit val alertFormat = eitherAsJson[String, AlertPayload](
    Format[String](Reads.StringReads, Writes.StringWrites),
    AlertPayload.json
  )
  implicit val format = Format[APSPayload](
    Json.reads[APSPayload],
    Writes[APSPayload] { p =>
      val alertJson = p.alert.fold(obj(ContentAvailable -> 1))(e =>
        obj(Alert -> e.fold(s => toJson(s), a => toJson(a)))
      )
      alertJson ++ objectify(Badge, p.badge) ++ objectify(Sound, p.sound) ++
        objectify(Category, p.category) ++ objectify(ThreadId, p.threadId)
    }
  )

  def full(
    payload: AlertPayload,
    badge: Option[Int] = None,
    sound: Option[String] = None,
    category: Option[String] = None
  ): APSPayload =
    apply(Option(Right(payload)), badge, sound, category)

  def simple(
    text: String,
    badge: Option[Int] = None,
    sound: Option[String] = None,
    category: Option[String] = None
  ): APSPayload =
    apply(Option(Left(text)), badge, sound, category)

  def background(
    badge: Option[Int] = None,
    sound: Option[String] = None,
    category: Option[String] = None
  ): APSPayload =
    apply(None, badge, sound, category)

  def eitherAsJson[L, R](l: Format[L], r: Format[R]): Format[Either[L, R]] = Format(
    Reads[Either[L, R]](json =>
      json.validate[L](l).map(Left.apply).orElse(json.validate[R](r).map(Right.apply))
    ),
    Writes[Either[L, R]](e => e.fold(left => toJson(left)(l), right => toJson(right)(r)))
  )

  private def objectify[T](key: String, opt: Option[T])(implicit w: Writes[T]): JsObject =
    opt.fold(obj())(v => obj(key -> toJson(v)))
}
