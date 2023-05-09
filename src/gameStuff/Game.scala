package gameStuff

import gameStuff.Character
import java.awt.Image
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.util.Random
class Game:

  private var waveCount: Int          = 0
  private var currentScore: Int       = 0         // final score that comes from the total damage done, armour added by protection and health healed

  def currentWave: Int  = waveCount
  def waveIsOver: Boolean = Monsters.forall(_.isDead)
  def isOver: Boolean = Characters.forall(_.isDead) || waveCount == maxWave

  // player characters are created immediately
  private val mage: Mage = Mage(mageName, mageHealth, mageArmour, mageToHit, mageDamage, mageShield)
  private val fighter: Fighter = Fighter(fighterName, fighterHealth, fighterArmour, fighterToHit, fighterDamage, fighterShield)
  private val rogue: Rogue = Rogue(rogueName, rogueHealth, rogueArmour, rogueToHit, rogueDamage, rogueShield)

  // buffers containing all characters and all the monsters for that wave
  private val Characters: Buffer[Character] = Buffer(mage, fighter, rogue)
  private val Monsters: Buffer[Monster] = Buffer()



  // Loop of the game
  def playGame() =

    this.welcomeMessage // prints the starting information

    while !this.isOver do // loop that creates new waves when they end
      this.newWave()
      while !this.isOver && !this.waveIsOver do

        println("\n")
        printMonsters(Monsters, Characters, monsterPositions, characterPositions) // grid displaying monsters, characters etc.
        val command = readLine(commandLine) // user input call
        val turnReport = this.playTurn(command) // what ends up happening with the user input

        if turnReport.nonEmpty then
          println(turnReport.get)
          if Monsters.exists(!_.isDead) then // if there are monsters left alive, it's their turn
            this.monstersTurn()
          Characters.foreach(_.resetForNewTurn()) // characters are reset for the new turn

      waveCount += 1 // once a new wave starts, the wave count goes up

    println(this.goodbyeMessage)
  end playGame


  // this is the bread and butter of how the game works
  def playTurn(command: String): Option[String] =

    // user input is disected
    val commandText               = command.trim.toLowerCase
    val strActor: String          = commandText.takeWhile( _ != ' ' ).trim
    val verb: String              = commandText.drop(strActor.length).trim.takeWhile( _ != ' ' ).trim
    val strTarget: String         = commandText.split("\\s+").drop(2).mkString(" ")

    // changing the actor to type Option[Character]
    val actor: Option[Character] =
      if Characters.map(_.characterName.toLowerCase).contains(strActor) then
        str2character(strActor)
      else
        None

    // changing the target to type Option[Character]
    val target: Option[Character] =
      if Characters.map(_.characterName.toLowerCase).contains(strTarget) || Monsters.map(_.characterName.toLowerCase).contains(strTarget) then
        str2character(strTarget)
      else
        None


    // Action options with an actor, action and target
    def execute(character: Character): Option[String] =
      verb match
        case "attack" if !target.get.isDead => Some(character.attack(target.get))
        case "heal" if actor.get.isInstanceOf[Mage]  => Some(character.asInstanceOf[Mage].heal(target.get))
        case "crossbow" if actor.get.isInstanceOf[Rogue] && !target.get.isDead => Some(character.asInstanceOf[Rogue].rangedAttack(target.get))
        case "protect" if actor.get.isInstanceOf[Fighter] => Some(character.asInstanceOf[Fighter].protect(target.get))
        case "longbow" if actor.get.isInstanceOf[Fighter] && !target.get.isDead => Some(character.asInstanceOf[Fighter].rangedAttack(target.get))
        case "fireball" if actor.get.isInstanceOf[Mage] && !target.get.isDead => Some(character.asInstanceOf[Mage].rangedAttack(target.get))
        case other => None

    // Actions without a target
    def noTargetExecute(character: Character): Option[String] =
      verb match
        case "rest" => Some(character.rest())
        case "defend" => Some(character.defend())
        case other => None

    // Actions with no effect
    def simpleExecute(command: String): Option[String] =
      command match
        case "help"=>
          this.help()
          None
        case other =>
          None

    // execute option is chosen here

    val somethingHappens: Option[String] =
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

    // final report of what happens, including user input error handling
    val outcomeReport: Option[String] =

      if somethingHappens.isDefined then
        Some(s"${somethingHappens.get}\n")
      else
        somethingHappens match
          case None if strActor == "help" => None
          case None if actor.isEmpty =>
            println("\nYou must specify a valid character name.")
            None
          case None if actor.get.isDead =>
            println("\nYou can't do anything because this character is dead.")
            None
          case None if target.nonEmpty && target.get.isDead =>
            println("\nYou can't target a dead character.")
            None
          case None if target.isEmpty =>
            println("\nUnknown target.")
            None
          case None if actor.nonEmpty && target.nonEmpty =>
            println("\nUnknown command. Type 'help' for a list of available commands.")
            None
          case None =>
            println("\nFormatting error. Type 'help' for a list of available commands.")
            None

    outcomeReport
  end playTurn


  // creates a new set of monsters and modifies the existing characters
  def newWave(): Unit =
    println(s"\nWave $waveCount:")
    if waveCount != 0 then
      Characters.foreach(_.modifyForNewWave())
      Monsters.clear()
      val info = setMonsters()
      Monsters.foreach(_.modifyForNewWave())
      println(info)

    else
      println(setMonsters())
  end newWave

  // A monster is chosen, then it moves and chooses who to attack. Then it attacks.
  def monstersTurn() =
    val monster = chooseMonster(Monsters)
    monster.move(Characters)
    val outcome = monster.attack(monster.chooseTarget(Characters))
    println(outcome)
  end monstersTurn

  // Creates 1 - max monster amount monsters for the player characters to fight against.
  def setMonsters(): String =

    val monsterAmount: Int = Random.between(1, ((monsterMax + 1)).max(3))
    val a: Int             = waveCount // Affects how much the monsters get better

    for i <- 1 to monsterAmount do
      // these should not be here lol, I should have made this updating system in Monster, like with Character.
      val x         = waveCount
      val armourMod = monsterArmourMod
      val healthMod = monsterHealthMod
      val damageMod = monsterDamageMod
      val toHitMod  = monsterToHitMod

      val m = Monster(s"Monster$i", monsterHealth + x * healthMod, monsterArmour + x * armourMod,
                      monsterToHit + x * toHitMod, monsterDamage + x * damageMod, monsterShield, Random.between(0,2))
      Monsters += m

    // sets the monster(s) either far away or near the character
    val monsterLocations = Monsters.map(monster => (monster.characterName,
      monster.currentDis match
        case 1 => "melee"
        case 0 => "far away"))

    val monsterInfo =
      for (a,b) <- monsterLocations yield
        s" $a is at $b distance."

    s"\nThere are $monsterAmount monsters here." + monsterInfo.mkString + "\n"
  end setMonsters

  // decides which monster gets to attack in this round
  def chooseMonster(monsters: Buffer[Monster]): Monster =
    val alives = monsters.filter(!_.isDead)
    val ableToHit = alives.filter(monster => Characters.filter(!_.isDead).exists(character => monster.toHitDef >= character.healthDef))
    if !ableToHit.isEmpty then
      ableToHit.maxBy(_.healthDef)
    else
      alives.maxBy(_.healthDef)

  // Tells the user all the available commands
  def help(): Unit =
    println("Available commands:")
    println("- <character> attack <target>")
    println("- mage heal <target>")
    println("- rogue crossbow <target>")
    println("- fighter protect <target>")
    println("- fighter longbow <target>")
    println("- mage fireball <target>")
    println("- <character> rest")
    println("- <character> defend")
    println("- help")



  // returns the score of the game.
  def score(characters: Buffer[Character]): String =
    var aScore: Int = 0
    val sum: Int = characters.map(_.damageDoneTotalDef).sum
    aScore = aScore + sum
    aScore.toString

  // welcome and goodbye messages at the beginning and the end of the game.
  def welcomeMessage: Unit =
    println(welcome)
    help()
  
  def goodbyeMessage: String =
    val extraScore = score(Characters)
    if Characters.forall(_.isDead) then
      "All of the characters died." + goodbye + s"Your score was: ${extraScore}."
    else if waveCount == maxWave then
      "The wave maximum was reached." + goodbye + s"Your score was: ${extraScore}."
    else
      goodbye + s"Your score was: ${extraScore}."

  // transforms strings into characters
  def str2character(str: String): Option[Character] =
    str match
      case "mage" => Some(mage)
      case "fighter" => Some(fighter)
      case "rogue" => Some(rogue)
      case "monster1" => Some(Monsters.head)
      case "monster2" => Some(Monsters(1))
      case "monster3" => Some(Monsters(2))
      case other => None



// VISUALS
  def monsterPositions =
    Monsters.map(m =>
      ((m.distance), Monsters.indexOf(m)))

  def characterPositions =
    Characters.map(c =>
      (((monsterMax - 1).max(2)), Characters.indexOf(c)))

  def printMonsters(monsters: Buffer[Monster], characters: Buffer[Character], monsterPositions: Buffer[(Int, Int)], characterPositions: Buffer[(Int, Int)]): Unit =
    val gridSize = monsterMax.max(3)
    val cellWidth = (monsters.map(_.gridStats.length) ++ characters.map(_.gridStats.length)).max
    val cellString = s"|${" " * (cellWidth)}|"
    val grid = Array.fill(gridSize, gridSize)(cellString)

    if monsters.nonEmpty then
      for (m <- monsters.indices) do
        val (row, col) = monsterPositions(m)
        grid(row)(col) = s"|${monsters(m).gridStats.padTo(cellWidth, ' ')}|"
      for (x <- characters.indices) do
        val (row, col) = characterPositions(x)
        grid(row)(col) = s"|${characters(x).gridStats.padTo(cellWidth, ' ')}|"

    for (row <- grid) do
      println(row.mkString(""))

  end printMonsters

end Game
