package gameStuff

import gameStuff.Character
import java.awt.Image
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.util.Random
class Game:

  private var roundCount: Int = 0
  private var currentScore: Int = 0
  private var highScores = Buffer()
  private var roundIsOver: Boolean = false
  private var wavesAreDone: Boolean = false
  private var waveCount: Int = 0

  def currentRound = roundCount

  def waveIsOver: Boolean = false // Monsters.forall(mons => mons.isDead)
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
        println(setMonsters())
        val command = readLine("\nCommand:")
        val turnReport = this.playTurn(command)
        if turnReport.isDefined then
          println(turnReport.get)
          this.monstersTurn()
          Characters.foreach(_.resetForNewTurn())
      waveCount += 1
      this.newWave()
    println(this.goodbyeMessage)


  def monstersTurn() = // missing a lot of monster logic
    roundCount += 1


  def setMonsters() =
    val a: Int = Random.between(1,4)
    for i <- 1 to a do
      val m = Monster(s"monster$i", monsterHealth, monsterArmour, monsterToHit, monsterDamage, monsterShield)
      Monsters += m
    s"There are $a monsters here."


  def playTurn(command: String): Option[String] =
    
    val commandText               = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")
    
    val target: Character = str2character(strTarget).get
    val actor: Character  = str2character(strActor).get

    def execute(actor: Character) =
      verb match
        case "rest" => Some(actor.rest())
        case "attack" => Some(actor.attack(target))
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


  // for testing the logic of my program before committing it to the main functionality:

  def testingInfo(): Unit =
    Characters.foreach(_.currentStats())

  def testTurn(command: String): Option[String] =
    val commandText               = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")

    val target: Option[Character] =
      if Characters.contains(strTarget) || Monsters.contains(strTarget) then
        str2character(strTarget)
      else
        None

    val actor: Option[Character] =
      if Characters.contains(strActor) then
        str2character(strActor)
      else
        None

    def execute(actor: Character): Option[String] =
      verb match
        case "attack" => Some(actor.attack(target.get))
        case other => None

    def noTargetExecute(actor: Character): Option[String] =
      verb match
        case "rest" => Some(actor.rest())
        case other => None
    
    val doingStuff =
      if actor.nonEmpty then
        if target.nonEmpty then
          execute(actor.get)
        else
          noTargetExecute(actor.get)
      else
        None

    val outcomeReport: Option[String] =
      if doingStuff.isDefined then
        Some(s"${doingStuff.get} \n" + s"${mage.currentStats()} \n${fighter.currentStats()} \n${rogue.currentStats()}")
      else
        None

    outcomeReport

  end testTurn


  def testPlayGame() =
    println(this.welcomeMessage)
      while !this.isOver do
        println(setMonsters())
        while !this.waveIsOver do
          val command = readLine("\nCommand:")
          val turnReport = this.testTurn(command)
          if turnReport.nonEmpty then
            println(turnReport.get)
            this.monstersTurn()
            Characters.foreach(_.resetForNewTurn())
          else
            println("Something went wrong. Make sure your inputting is correct.")
        waveCount += 1
        this.newWave()
      println(this.goodbyeMessage)