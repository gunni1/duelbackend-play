package backend.duel.persistence

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestActors, TestKit}
import backend.avatar.Avatar
import backend.avatar.persistence.AvatarId
import backend.duel.persistence.DuelEventPersister.SaveDuelEvent
import backend.duel.{ActionEvent, DuelSimulator}
import backend.simulation.{AttackResult, DamageReceived, FightingAvatar}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import scala.concurrent.duration._

import scala.util.Random


class DuelEventPersisterSpec extends TestKit(ActorSystem("DuelEventPersisterSpec"))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar{

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An DuelEventPersister" must {
    "save DuelEvents in a Repository" in {
      val repository = mock[DuelEventRepository]
      val testEventPersister = TestActorRef(Props(new DuelEventPersister(repository)))

      val duelEventId = DuelEventId(DuelId("1"),"0")
      val attackEvent = ActionEvent(duelEventId,createAttackResult)
      testEventPersister ! SaveDuelEvent(attackEvent)

      within(1 seconds){
        verify(repository).saveDuelEvent(duelEventId, attackEvent)
      }
    }

  }

  private def createAttackResult: AttackResult = {
    val left = new Avatar("left", AvatarId("idLeft"))
    val right = new Avatar("right", AvatarId("idRight"))
    val random = new Random
    val fLeft = new FightingAvatar(left, right, random)
    val fRight = new FightingAvatar(right, left, random)
    AttackResult(fLeft, DamageReceived(fRight,1))
  }

}
