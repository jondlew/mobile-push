package tests

import com.malliina.push.wns.CommandId
import com.malliina.push.wns.CommandId.Dismiss
import play.api.libs.json.Json

class WNSJson extends munit.FunSuite {

  test("read") {
    val in = """"dismiss""""
    val result = Json.parse(in).asOpt[CommandId]
    assert(result.contains(Dismiss))
  }

  test("write") {
    val str = Json.stringify(Json.toJson(Dismiss: CommandId))
    assert(str == """"dismiss"""")
  }
}
