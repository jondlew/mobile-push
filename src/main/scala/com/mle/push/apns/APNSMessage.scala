package com.mle.push.apns

import play.api.libs.json.Json._
import play.api.libs.json._


/**
 * @author Michael
 */
case class APNSMessage(aps: APSPayload, data: Map[String, JsValue] = Map())

object APNSMessage {
  def simple(alert: String): APNSMessage = simple(alert, None)

  def badged(alert: String, badge: Int): APNSMessage = simple(alert, Option(badge))

  /**
   * @return A message that updates the app badge in the background: No message is shown and no sound is played.
   */
  def background(badge: Int): APNSMessage = APNSMessage(APSPayload(None, Option(badge)))

  private def simple(alert: String, badge: Option[Int]): APNSMessage = APNSMessage(APSPayload(Some(Left(alert)), badge))

  implicit val json = new Writes[APNSMessage] {
    override def writes(o: APNSMessage): JsValue = obj("aps" -> toJson(o.aps)) ++ toJson(o.data).as[JsObject]
  }
}
