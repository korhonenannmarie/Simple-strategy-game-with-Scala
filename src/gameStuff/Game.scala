package gameStuff

import gameStuff.Character
import java.awt.Image
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.util.Random
class Game:

  private var roundCount: Int         = 0
  private var currentScore: Int       = 0
  private var highScores: Buffer[Int] = Buffer()
  private var roundIsOver: Boolean    = false
  private var wavesAreDone: Boolean   = false
  private var waveCount: Int          = 0

  def currentRound: Int = roundCount
  def currentWave: Int  = waveCount

  def waveIsOver: Boolean = Monsters.forall(_.isDead)
  def isOver: Boolean = Characters.count(_.isDead) == 3 || waveCount == maxWave // more generalised!

  val mage: Mage = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)
  val fighter: Fighter = Fighter(fighterName, fighterHealth, fighterArmour, fighterToHit, fighterDamage, fighterShield)
  val rogue: Rogue = Rogue(rogueName, rogueHealth, rogueArmour, rogueToHit, rogueDamage, rogueShield)

  // buffers containing all characters and all the monsters for that wave
  val Characters: Buffer[Character] = Buffer(mage, fighter, rogue)
  val Monsters: Buffer[Monster] = Buffer()


// todo: add high score stuff
  def playGame() =
    println(this.welcomeMessage)
    while !this.isOver do
      this.newWave()
      while !this.isOver && !this.waveIsOver do
        val command = readLine("\nCommand:")
        val turnReport = this.testTurn(command)
        if turnReport.nonEmpty then
          println(turnReport.get)
          this.monstersTurn()
          Characters.foreach(_.resetForNewTurn())
        else
          println("Something went wrong. Make sure your inputting is correct.")
      waveCount += 1
    println(this.goodbyeMessage)
  end playGame


  def monstersTurn() =
    val monster = chooseMonster(Monsters)
    monster.move(Characters)
    monster.attack(chooseTarget(Characters))
    roundCount += 1

// make this more generic
  def setMonsters(): String =
    val monsterAmount: Int = Random.between(1,4)
    for i <- 1 to monsterAmount do
      val m = Monster(s"monster$i", monsterHealth, monsterArmour, monsterToHit, monsterDamage, monsterShield, Random.between(0,2)) // todo: make extendable
      Monsters += m
    s"There are $monsterAmount monsters here."

  def chooseMonster(monsters: Buffer[Monster]): Monster = ???
  


  def playTurn(command: String): Option[String] =
    
    val commandText               = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")

    val target: Option[Character] =
      if Characters.map(_.characterName).contains(strTarget) || Monsters.map(_.characterName).contains(strTarget) then
        str2character(strTarget)
      else
        None

    val actor: Option[Character] =
      if Characters.map(_.characterName).contains(strActor) then
        str2character(strActor)
      else
        None

    //todo: make a list of possible command words and insert that in these instead
    //todo: commands such as help, end game, etc.
    def execute(actor: Character): Option[String] =
      verb match
        case "attack" if !target.get.isDead => Some(actor.attack(target.get))
        case "heal" if actor.isInstanceOf[Mage]  => Some(actor.asInstanceOf[Mage].heal(target.get))
        case "crossbow" if actor.isInstanceOf[Rogue] => Some(actor.asInstanceOf[Rogue].crossbow(target.get))
        case "protect" if actor.isInstanceOf[Fighter] => Some(actor.asInstanceOf[Fighter].protect(target.get))
        case "longbow" if actor.isInstanceOf[Fighter] => Some(actor.asInstanceOf[Fighter].protect(target.get))
        case other => None

    def noTargetExecute(actor: Character): Option[String] =
      verb match
        case "rest" => Some(actor.rest())
        case "defend" => Some(actor.defend())
        case other => None

    def simpleExecute(actor: String): Option[String] =
      actor match
        case "help" => Some(this.help)

    val doingStuff = //todo: figure this shit out
      if actor.nonEmpty && !(actor.get.isDead) then
        if target.nonEmpty then
          execute(actor.get)
        else if verb.nonEmpty then
          noTargetExecute(actor.get)
        else
          simpleExecute(commandText)
      else
        None

    val outcomeReport: Option[String] =
      if doingStuff.isDefined then
        Some(s"${doingStuff.get}\n" + s"${mage.currentStats()} ${fighter.currentStats()} ${rogue.currentStats()}")
      else
        None

    outcomeReport
      
  end playTurn

  def help: String = "yeet"

  def welcomeMessage: String = welcome //todo: add high scores here
  
  def goodbyeMessage: String = goodbye
  
  def newWave(): Unit =
    if waveCount != 0 then
      Characters.foreach(_.modifyForNewWave())
      Monsters.clear()
      val info = setMonsters()
      Monsters.foreach(_.modifyForNewWave())
      println(info)
    else
      println(setMonsters())



  // this isn't super extendable... //todo: think of a more general option
  def str2character(str: String): Option[Character] =
    str match
      case "mage" => Some(mage)
      case "fighter" => Some(fighter)
      case "rogue" => Some(rogue)
      case "monster1" => Some(Monsters.head)
      case "monster2" => Some(Monsters(1))
      case "monster3" => Some(Monsters(2))
      case other => None














  // for testing the logic of my program before committing it to the main functionality:

  def testingInfo(): String =
    Characters.map(x => x.currentStats()).mkString("") + "\n" +
    Monsters.map(x => x.currentStats()).mkString("") + "\n" +
    Monsters.map(x => x.currentDis).mkString("") + "\n" +
    s"$currentRound \n" +
    s"$currentWave \n"

  def testTurn(command: String): Option[String] =
    val commandText               = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")

    val target: Option[Character] =
      if Characters.map(_.characterName).contains(strTarget) || Monsters.map(_.characterName).contains(strTarget) then
        str2character(strTarget)
      else
        None

    val actor: Option[Character] =
      if Characters.map(_.characterName).contains(strActor) then
        str2character(strActor)
      else
        None
    //todo: make a list of possible command words and insert that in these instead
    def execute(actor: Character): Option[String] =
      verb match
        case "attack" if !target.get.isDead => Some(actor.attack(target.get))
        case "heal"   => Some(mage.heal(target.get))
        case other => None

    def noTargetExecute(actor: Character): Option[String] =
      verb match
        case "rest" => Some(actor.rest())
        case "defend" => Some(actor.defend())
        case other => None

    val doingStuff =
      if actor.nonEmpty && !(actor.get.isDead) then
        if target.nonEmpty then
          execute(actor.get)
        else
          noTargetExecute(actor.get)
      else
        None

    val outcomeReport: Option[String] =
      if doingStuff.isDefined then
        Some(s"${doingStuff.get}\n" + s"${mage.currentStats()} ${fighter.currentStats()} ${rogue.currentStats()}")
      else
        None

    outcomeReport

  end testTurn


  def testPlayGame() =
    println(this.welcomeMessage)
    while !this.isOver do
      this.newWave()
      println(testingInfo())
      while !this.isOver && !this.waveIsOver do
        val command = readLine("\nCommand:")
        val turnReport = this.testTurn(command)
        if turnReport.nonEmpty then
          println(turnReport.get)
          this.monstersTurn()
          Characters.foreach(_.resetForNewTurn())
        else
          println("Something went wrong. Make sure your inputting is correct.")
        println(testingInfo())
      waveCount += 1
    println(this.goodbyeMessage)

