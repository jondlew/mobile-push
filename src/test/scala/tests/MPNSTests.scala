package tests

import java.net.{MalformedURLException, URL}

import com.mle.push.mpns.{MPNSClient, TileData, ToastMessage}
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

/**
 * @author Michael
 */
class MPNSTests extends FunSuite {
  val devices = Seq(
    "http://s.notify.live.net/u/1/db3/HmQAAAB_5whAEO6GhJpi2BjBxhKedc4c5A5vCeH50U9hOSbcDS3qNOAIFjMmfyxJ3SSSTZMpc6NdxiehrGtcFB4rl3KU/d2luZG93c3Bob25lZGVmYXVsdA/vnjifiZVeEyAAmiZ81DJ8w/GSq1vU6RyYMIc9ZnJSfs-jpZoAk",
    "http://s.notify.live.net/u/1/db3/HmQAAAC7ZLGhuu1q4QzaD6MdN_qhULt8BHWE0XzOHs_R73Wr0qQuOqtHfpTuHHbtSpctsi8g3ZWFgu_PE7kGLCuElK44/d2luZG93c3Bob25lZGVmYXVsdA/vnjifiZVeEyAAmiZ81DJ8w/qVX6TH1mul8u93I3qUdD2oA1fjQ"
  )
  //  val devices = Nil

  test("can send toast") {
    val client = new MPNSClient
    val message = ToastMessage("hey", "you all 2", "/ServicePage.xaml", silent = false)
    val f = client.pushAll(devices, message)
    val rs = Await.result(f, 5.seconds)
    assert(rs.forall(r => r.getStatusCode === 200))
  }
  test("can send tile") {
    val client = new MPNSClient
    val message = TileData("img.jpg", 1, "Title here - Good Night", "no.jpg", "Back title here", "Back content here")
    val f = client.pushAll(devices, message)
    val rs = Await.result(f, 5.seconds)
    assert(rs.forall(r => r.getStatusCode === 200))
  }
  test("validate token") {
    val validToken = devices.head
    val invalidToken = "token123"
    intercept[MalformedURLException](new URL(invalidToken))
    new URL(validToken)
  }
}
