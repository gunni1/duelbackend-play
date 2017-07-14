package backend.duel.persistence

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import backend.avatar.Avatar
import backend.avatar.persistence.AvatarId
import backend.duel.{ActionEvent, DuelSimulator}
import backend.simulation.{AttackResult, FightingAvatar}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class DuelEventPersisterSpec extends TestKit(ActorSystem("DuelEventPersisterSpec"))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar{

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An DuelEventPersister" must {

    "save DuelEvents in a Repository" in {
      val repository = mock[DuelEventRepository]
      val duelEventPersister = system.actorOf(Props(new DuelEventPersister(repository)))
      val attackEvent = ActionEvent(DuelEventId(DuelId("1"),"0"),createAttackResult)
      duelEventPersister ! attackEvent
      expectMsg(attackEvent)
    }

  }

  private def createAttackResult: AttackResult = {
    val left = new Avatar("left", AvatarId("idLeft"))
    val right = new Avatar("right", AvatarId("idRight"))
    ???
  }

}
