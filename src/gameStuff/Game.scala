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

  val mage: Character = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)
  val fighter: Character = Fighter(fighterName, fighterHealth, fighterArmour, fighterToHit, fighterDamage, fighterShield)
  val rogue: Character = Rogue(rogueName, rogueHealth, rogueArmour, rogueToHit, rogueDamage, rogueShield)

  val Characters: Buffer[Character] = Buffer(mage, fighter, rogue)
  val Monsters: Buffer[Monster] = ???

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

  def monstersTurn() = // missing a lot of monster logic
    roundCount += 1


  def setMonsters() = ???

  def playTurn(command: String): Option[String] =
    val commandText = command.trim.toLowerCase
    val actor       = commandText.takeWhile( _ != ' ' )
    val action      = Action(command)

    val doingStuff = action.execute(
      actor match
        case "mage" => mage
        case "fighter" => fighter
        case "rogue" => rogue)

    var outcomeReport = s"${mage.currentStats()} \n ${fighter.currentStats()} \n ${rogue.currentStats()}"

    if doingStuff.nonEmpty then
      Some(outcomeReport)
    else
      None

  def welcomeMessage = "Welcome to the game"
  
  def goodbyeMessage = "Goodbye for now"
  
  def isOver: Boolean = false // to be implemented

  def newWave(): Unit =
    Characters.foreach(_.modifyForNewWave())
    Monsters.foreach(_.modifyForNewWave())

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
      val turnReport = this.playTurn(command)
      if turnReport.nonEmpty then
         println(turnReport)