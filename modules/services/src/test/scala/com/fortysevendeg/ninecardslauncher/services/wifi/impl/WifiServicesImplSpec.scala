package com.fortysevendeg.ninecardslauncher.services.wifi.impl

import android.net.wifi.{WifiConfiguration, WifiInfo, WifiManager}
import android.net.{ConnectivityManager, NetworkInfo}
import cats.data.Xor
import com.fortysevendeg.ninecardslauncher.commons.contexts.ContextSupport
import com.fortysevendeg.ninecardslauncher.commons.javaNull
import com.fortysevendeg.ninecardslauncher.services.wifi.WifiServicesException
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.collection.JavaConversions._

trait WifiImplSpecification
  extends Specification
    with Mockito
    with WifiServicesImplData {

  trait WifiImplScope
    extends Scope {

    val mockContextSupport = mock[ContextSupport]
    val mockConnectivityManager = mock[ConnectivityManager]
    val mockNetWorkInfo = mock[NetworkInfo]
    val mockWifiManager = mock[WifiManager]
    val mockWifiInfo = mock[WifiInfo]

    val wifiServicesImpl = new WifiServicesImpl {

      override protected def getConnectivityManager(implicit contextSupport: ContextSupport) = Option(mockConnectivityManager)

      override protected def getWifiManager(implicit contextSupport: ContextSupport) = Option(mockWifiManager)

    }

    mockConnectivityManager.getActiveNetworkInfo returns mockNetWorkInfo
    mockNetWorkInfo.isConnected returns true
    mockNetWorkInfo.getExtraInfo returns ssid
    mockNetWorkInfo.getType returns ConnectivityManager.TYPE_WIFI

    mockWifiManager.getConfiguredNetworks returns wifiConfigurations

  }

}

class WifiServicesImplSpec
  extends  WifiImplSpecification {

  "getCurrentSSID" should {
    "returns the current SSID" in
      new WifiImplScope {
        val result = wifiServicesImpl.getCurrentSSID(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(resultSSID) => resultSSID shouldEqual Some(ssid)
        }
      }

    "returns None if it is not connected" in
      new WifiImplScope {

        mockNetWorkInfo.isConnected returns false

        val result = wifiServicesImpl.getCurrentSSID(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(resultSSID) => resultSSID shouldEqual None
        }
      }

    "returns None if type isn't WIFI" in
      new WifiImplScope {

        mockNetWorkInfo.getType returns ConnectivityManager.TYPE_MOBILE

        val result = wifiServicesImpl.getCurrentSSID(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(resultSSID) => resultSSID shouldEqual None
        }
      }

    "returns None if SSID is empty" in
      new WifiImplScope {

        mockNetWorkInfo.getExtraInfo returns ""

        val result = wifiServicesImpl.getCurrentSSID(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(resultSSID) => resultSSID shouldEqual None
        }
      }

    "returns None if SSID is null" in
      new WifiImplScope {

        mockNetWorkInfo.getExtraInfo returns javaNull

        val result = wifiServicesImpl.getCurrentSSID(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(resultSSID) => resultSSID shouldEqual None
        }
      }
  }

  "getConfiguredNetworks" should {

    "returns list of networks sorted" in
      new WifiImplScope {
        val result = wifiServicesImpl.getConfiguredNetworks(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(networks) => networks shouldEqual networksSorted
        }
      }

    "returns empty list if android don't return data" in
      new WifiImplScope {
        mockWifiManager.getConfiguredNetworks returns Seq.empty[WifiConfiguration]

        val result = wifiServicesImpl.getConfiguredNetworks(mockContextSupport).value.run
        result must beLike {
          case Xor.Right(networks) => networks shouldEqual Seq.empty
        }
      }

    "returns WifiServicesException if android returns null" in
      new WifiImplScope {
        mockWifiManager.getConfiguredNetworks returns javaNull

        val result = wifiServicesImpl.getConfiguredNetworks(mockContextSupport).value.run
        result must beLike {
          case Xor.Left(e) => e  must beAnInstanceOf[WifiServicesException]
        }
      }

  }

}
