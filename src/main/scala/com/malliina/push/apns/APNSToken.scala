package com.malliina.push.apns

import com.malliina.push.{Token, TokenCompanion}
import com.notnoop.apns.internal.Utilities

import scala.util.Try

/**
  * @author Michael
  */
case class APNSToken private(token: String) extends Token

object APNSToken extends TokenCompanion[APNSToken] {
  override def isValid(token: String): Boolean =
    Try(Utilities.decodeHex(token)).isSuccess
}
