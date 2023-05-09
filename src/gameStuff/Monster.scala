package gameStuff

import gameStuff.Character

import java.security.KeyStore.TrustedCertificateEntry
import scala.util.Random
import scala.collection.mutable.Buffer

class Monster(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int, var distance: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):

  // could be used in a more extended version of the code
  val armourMod = monsterArmourMod
  val healthMod = monsterHealthMod
  val damageMod = monsterDamageMod
  val toHitMod  = monsterToHitMod

  private var whoAttackedLast: Option[Character] = None

  def currentDis: Int = distance
  override def isInMelee: Boolean = currentDis == 1

  // attacking for monsters is simpler than with player characters
  override def attack(target: Character) = 
    if target.takeDamage(this.damagePerAttack, this.toHit, this) then
      s"The attack by ${this.name} hits! ${target.characterName} takes $damagePerAttack damage."
    else
      s"The attack by ${this.name} does not hit."

  // similar takeDamage method to Character
  override def takeDamage(damage: Int, toHit: Int, attacker: Character): Boolean =
    if armour <= toHit then
      if currentHealth > 0 then
        currentHealth = (currentHealth - damage).max(0)
        whoAttackedLast = Some(attacker)
        true
      else
        false
    else
      false

  // simple AI for moving, not super extendable though
  def move(characters: Buffer[Character]): Unit =
    val mostDangerous = characters.filter(!_.isDead).maxBy(_.healthDef)
    mostDangerous match
      case r: Rogue if this.isInMelee => this.distance = 0
      case f: Fighter if this.isInMelee => this.distance = 0
      case m: Mage if !this.isInMelee  => this.distance = 1
      case x: Character => this.distance = this.distance

  // simple AI for choosing a target to attack
  def chooseTarget(characters: Buffer[Character]): Character =
    val alives = characters.filter(!_.isDead)

    if alives.contains(
      whoAttackedLast match
        case Some(character) => character
        case None => false) then
      whoAttackedLast.get
    else
      val ableToHit = alives.filter(_.armourDef <= toHit)
      if !ableToHit.isEmpty then
        ableToHit.minBy(_.healthDef)
      else
        alives.minBy(_.healthDef)

  // here for the sake of it:
  val rangedAttackName = "thisShouldntExist"
  val defendingName = "thisShouldntExistEither"