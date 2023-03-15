package gameStuff

import gameStuff.Character

import java.awt.Image
import scala.collection.mutable.Buffer
class Game:

  private var roundCount: Int = 0
  private var currentScore: Int = 0
  private var highScores = Buffer()
  private var roundIsOver: Boolean = false
  private var waveIsOver: Boolean = false

  val mage = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)
    
  private def newWave = ???
  private def playRound = ???
  private def usersTurn = ???
  private def monstersTurn = ???

  def playTurn(command: String) =
    val commandText = command.trim.toLowerCase
    val actor       = commandText.takeWhile( _ != ' ' )
    val action = Action(command)
    val outcomeReport = action.execute(
      actor match
        case "mage" => mage)
    
    
    


  def welcomeMessage = ???
  
  def goodbyeMessage = ???

  def currentRound = roundCount

  def reset(character: Character) = ???
  
  def isOver: Boolean =
    false

  def testTurn(command: String) =
    val input = command.trim.toLowerCase
    input match
      case "yes" => "Whoop"
      case "no" => "alright nope"
      case other => None

