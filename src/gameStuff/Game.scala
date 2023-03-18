package gameStuff

import gameStuff.Character
import java.awt.Image
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
class Game:

  private var roundCount: Int = 0
  private var currentScore: Int = 0
  private var highScores = Buffer()
  private var roundIsOver: Boolean = false
  private var thisWaveIsOver: Boolean = false
 
  def currentRound = roundCount
  def waveIsOver = thisWaveIsOver
  
  val mage = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)


  
  def playGame() = 
    while !this.isOver do
      this.welcomeMessage
      for wave <- 0 until maxWave do
        while !this.waveIsOver do
          val command = readLine()
          val turnReport = this.playTurn(command)
          if turnReport.nonEmpty then
            println(turnReport)
            this.monstersTurn()
        this.newWave()
    this.goodbyeMessage
  
  def monstersTurn() =
    roundCount += 1

  def playTurn(command: String): String = 
    val commandText = command.trim.toLowerCase
    val actor       = commandText.takeWhile( _ != ' ' )
    val action = Action(command)

    val doingStuff = action.execute(
      actor match
        case "mage" => mage)
      
    var outcomeReport = "Stuff"
    outcomeReport

  def welcomeMessage = ???
  
  def goodbyeMessage = ???

  def reset(character: Character) = ???
  
  def isOver: Boolean = false // to be implemented

  def newWave() = ???
  
  
  
  // for testing the logic of my program:
  
  def testTurn(command: String) =
    val input = command.trim.toLowerCase
    input match
      case "yes" => "Whoop"
      case "no" => "alright nope"
      case other => None


  def testPlayGame() =
    while !this.isOver do
      val command = readLine("\nCommand:")
      val turnReport = this.testTurn(command)
      if turnReport != None then
        println(turnReport)