package gameStuff

import gameStuff.Character

import java.security.KeyStore.TrustedCertificateEntry
import scala.util.Random
import scala.collection.mutable.Buffer

class Monster(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int, var distance: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):

  val armourMod = monsterArmourMod
  val healthMod = monsterHealthMod
  val damageMod = monsterDamageMod
  val toHitMod  = monsterToHitMod

  val rangedAttackName = "thisShouldntExist"
  val defendingName = "thisShouldntExistEither"

  private var whoAttackedLast: Option[Character] = None

  def currentDis: Int = distance

  override def isInMelee: Boolean = currentDis == 1

  override def attack(target: Character) = 
    if target.takeDamage(this.damagePerAttack, this.toHit, this) then
      s"The attack by ${this.name} hits! ${target.characterName} takes $damagePerAttack damage."
    else
      s"The attack by ${this.name} does not hit."

  override def takeDamage(damage: Int, toHit: Int, attacker: Character): Boolean =
    if armour <= toHit then
      if currentHealth > 0 then
        currentHealth += -damage
        whoAttackedLast = Some(attacker)
        true
      else
        false
    else
      false

  def move(characters: Buffer[Character]): Unit =
    val mostDangerous = characters.filter(!_.isDead).maxBy(_.healthDef)
    mostDangerous match
      case r: Rogue => r.isInMelee
      case f: Fighter => !f.isInMelee
      case m: Mage => !m.isInMelee

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
        ableToHit.maxBy(_.healthDef)
      else
        alives.maxBy(_.healthDef)