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
  def isOver: Boolean = Characters.forall(_.isDead) || waveCount == maxWave

  private val mage: Mage = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)
  private val fighter: Fighter = Fighter(fighterName, fighterHealth, fighterArmour, fighterToHit, fighterDamage, fighterShield)
  private val rogue: Rogue = Rogue(rogueName, rogueHealth, rogueArmour, rogueToHit, rogueDamage, rogueShield)

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
        val turnReport = this.playTurn(command)
        if turnReport.nonEmpty then
          println(turnReport.get)
          if Monsters.exists(!_.isDead) then
            this.monstersTurn()
          Characters.foreach(_.resetForNewTurn())
        println(testingInfo())
      waveCount += 1
    println(this.goodbyeMessage)

  end playGame



  def playTurn(command: String): Option[String] =

    val commandText               = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")

    val target: Option[Character] =
      if Characters.map(_.characterName.toLowerCase).contains(strTarget) || Monsters.map(_.characterName.toLowerCase).contains(strTarget) then
        str2character(strTarget)
      else
        None

    val actor: Option[Character] =
      if Characters.map(_.characterName.toLowerCase).contains(strActor) then
        str2character(strActor)
      else
        None

    //todo: make a list of possible command words and insert that in these instead
    //todo: commands such as help, end game, etc.
    def execute(character: Character): Option[String] =
      verb match
        case "attack" if !target.get.isDead => Some(character.attack(target.get))
        case "heal" if actor.isInstanceOf[Mage]  => Some(character.asInstanceOf[Mage].heal(target.get))
        case "crossbow" if actor.isInstanceOf[Rogue] && !target.get.isDead => Some(character.asInstanceOf[Rogue].rangedAttack(target.get))
        case "protect" if actor.isInstanceOf[Fighter] => Some(character.asInstanceOf[Fighter].protect(target.get))
        case "longbow" if actor.isInstanceOf[Fighter] && !target.get.isDead => Some(character.asInstanceOf[Fighter].rangedAttack(target.get))
        case "fireball" if actor.isInstanceOf[Mage] && !target.get.isDead => Some(character.asInstanceOf[Mage].rangedAttack(target.get))
        case other => None

    def noTargetExecute(character: Character): Option[String] =
      verb match
        case "rest" => Some(character.rest())
        case "defend" => Some(character.defend())
        case other => None

    def simpleExecute(command: String): Option[String] =
      command match
        case "help"=>
          this.help()
          None
        case other =>
          None

    val doingStuff: Option[String] =
      if actor.nonEmpty && !(actor.get.isDead) then
        if target.nonEmpty then
          execute(actor.get)
        else if verb.nonEmpty then
          noTargetExecute(actor.get)
        else
          None
      else if commandText.nonEmpty then
        simpleExecute(strActor)
      else
        None

    val outcomeReport: Option[String] =
      if doingStuff.isDefined then
        Some(s"${doingStuff.get}\n" + s"${mage.currentStats()} ${fighter.currentStats()} ${rogue.currentStats()}")
      else
        doingStuff match
          case None if actor.isEmpty =>
            println("You must specify a valid character name.")
            None
          case None if actor.get.isDead =>
            println("You can't do anything because this character is dead.")
            None
          case None if target.nonEmpty && target.get.isDead =>
            println("You can't target a dead character.")
            None
          case None =>
            println("Unknown command. Type 'help' for a list of available commands.")
            None

    outcomeReport

  end playTurn

  def monstersTurn() =
    val monster = chooseMonster(Monsters)
    monster.move(Characters)
    val outcome = monster.attack(monster.chooseTarget(Characters))
    roundCount += 1
    println(outcome)

// make this more generic
  def setMonsters(): String =
    val monsterAmount: Int = Random.between(1,4)
    for i <- 1 to monsterAmount do
      val m = Monster(s"Monster$i", monsterHealth, monsterArmour, monsterToHit, monsterDamage, monsterShield, Random.between(0,2)) // todo: make extendable
      Monsters += m
    val monsterLocations = Monsters.map(monster => (monster.characterName,
      monster.currentDis match
        case 0 => "melee"
        case 1 => "far away"))
    val monsterInfo =
      for (a,b) <- monsterLocations yield
        s" $a is at $b distance"
    s"There are $monsterAmount monsters here." + monsterInfo.mkString(",")

  def chooseMonster(monsters: Buffer[Monster]): Monster =
    val alives = monsters.filter(!_.isDead)
    val ableToHit = alives.filter(monster => Characters.filter(!_.isDead).exists(character => monster.currentToHit >= character.healthToAttacker))
    if !ableToHit.isEmpty then
      ableToHit.maxBy(_.healthToAttacker)
    else
      alives.maxBy(_.healthToAttacker)



  def help(): Unit = println("yeet")

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



  // this isn't super extendable... // todo: think of a more general option
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
