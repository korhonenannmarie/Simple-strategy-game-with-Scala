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
  private var wavesAreDone: Boolean = false
  private var waveCount: Int = 0

  def currentRound = roundCount

  def waveIsOver: Boolean = Monsters.forall(mons => mons.isDead)
  def isOver: Boolean = Characters.forall(char => char.isDead) || waveCount == maxWave

  val mage: Character = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)
  val fighter: Character = Fighter(fighterName, fighterHealth, fighterArmour, fighterToHit, fighterDamage, fighterShield)
  val rogue: Character = Rogue(rogueName, rogueHealth, rogueArmour, rogueToHit, rogueDamage, rogueShield)

  val Characters: Buffer[Character] = Buffer(mage, fighter, rogue)
  val Monsters: Buffer[Monster] = Buffer()

  def playGame() =
    println(this.welcomeMessage)
    while !this.isOver do
      while !this.waveIsOver do
        val command = readLine("\nCommand:")
        val turnReport = this.playTurn(command)
        if turnReport.isDefined then
          println(turnReport.get)
          this.monstersTurn()
      waveCount += 1
      this.newWave()
    println(this.goodbyeMessage)

  def monstersTurn() = // missing a lot of monster logic
    roundCount += 1


  def setMonsters() = ???

  def playTurn(command: String): Option[String] =
    
    val commandText       = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")
    
    val target: Character = str2character(strTarget).get // Fix the exception stuff!
    val actor: Character  = str2character(strActor).get

    def execute(actor: Character) =
      verb match
        case "rest" => Some(actor.rest())
        case "attack" => Some(actor.attack(target))
        case "attackSelf" => Some(actor.attack(actor))
        case other => None
    
    val doingStuff  = execute(actor)
    
    val outcomeReport = Some(s"${doingStuff.get} \n" + s"${mage.currentStats()} \n${fighter.currentStats()} \n${rogue.currentStats()}")

    if doingStuff.isDefined then
      outcomeReport
    else
      None
      
  end playTurn


  def welcomeMessage: String = welcome
  
  def goodbyeMessage: String = goodbye
  
  def newWave(): Unit =
    Characters.foreach(_.modifyForNewWave())
    Monsters.foreach(_.modifyForNewWave())

  def str2character(str: String): Option[Character] =
    str match
      case "mage" => Some(mage)
      case "fighter" => Some(fighter)
      case "rogue" => Some(rogue)
      case other => None



  // for testing the logic of my program:

  def testTurn(command: String) =

    val commandText: String       = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")

    val target: Character         = str2character(strTarget).get
    val actor: Character          = str2character(strActor).get


  def testPlayGame() =
    println(this.welcomeMessage)
    while !this.isOver do
      while !this.waveIsOver do
        val command = readLine("\nCommand:")
        val turnReport = this.playTurn(command)
        if turnReport.isDefined then
          println(turnReport.get)
      waveCount += 1
      this.newWave()
    println(this.goodbyeMessage)