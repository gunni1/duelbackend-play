package backend.simulation


import akka.actor.{ActorRef, ActorSystem, Props, Scheduler, Scope}
import akka.testkit.{ImplicitSender, TestActorRef, TestActors, TestKit, TestProbe}
import backend.avatar.Avatar
import backend.avatar.persistence.AvatarId
import backend.duel.AsyncExecutionTimeSetter.SetNextExecutionTime
import backend.duel.DuelSimulator.{ExecuteNextAction, InitiateDuelBetween}
import backend.duel.{ActionExecutionTimeService, AsyncExecutionTimeSetter, DuelSimulator}
import backend.duel.persistence.{DuelEventPersister, DuelId}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import org.mockito.Mockito._
import org.mockito.Matchers._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Implementierung erfolgt wenn enscheiden ist wie der DuelSimulator funktioniert.
  */
class DuelSimulatorActorSpec extends TestKit(ActorSystem("DuelSimulatorActorSpec"))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar{

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  def mockDuelEventPersister = mock[ActorRef]

  def mockDuelExecTimeSetter = mock[ActorRef]

  def mockActionExecService = mock[ActionExecutionTimeService]

  def duelId = DuelId("1")



  "A DuelSimulator" must {
    "schedule the first action when Initiate command issued" in {
      val mockScheduler = mock[Scheduler]
      val duelSimulatorActor = system.actorOf(Props(new DuelSimulator(mockDuelEventPersister,mockDuelExecTimeSetter,duelId){
        override def scheduler = mockScheduler
      }))

      val avatars = createAvatars
      duelSimulatorActor ! InitiateDuelBetween(avatars._1,avatars._2)

      val expectedNextActionEvent = ExecuteNextAction

      within(1 second){

        verify(mockScheduler).scheduleOnce(3800 milliseconds, duelSimulatorActor, expectedNextActionEvent)
      }
    }

    "set initial actiontime" in {
      val execTimeSetter = TestProbe()
      val duelSimulator = TestActorRef(Props(new DuelSimulator(mockDuelEventPersister,execTimeSetter.ref,duelId)))

      val avatars = createAvatars
      duelSimulator ! InitiateDuelBetween(avatars._1,avatars._2)

      within(1 second){
        execTimeSetter.expectMsgPF(){
          case SetNextExecutionTime(resultDuelId,_) => resultDuelId should be (duelId)
          case _ => fail("falsches Event empfangen")
        }

      }
    }
  }

  private def createAvatars = {
    val left = new Avatar("left", AvatarId("idLeft"), 100, 100, 100,100, 100)
    val right = new Avatar("right", AvatarId("idRight"), 200, 200, 200, 200, 200)
    val random = new Random
    val fLeft = new FightingAvatar(left, right, random)
    val fRight = new FightingAvatar(right, left, random)
    (fLeft, fRight)
  }
}
