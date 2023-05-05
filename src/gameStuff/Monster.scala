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

  private var whoAttackedLast: Option[Character] = None


  def currentDis: Int = distance

  override def isInMelee: Boolean = currentDis == 0

  override def attack(target: Character) = 
    if target.takeDamage(this.damagePerAttack, this.toHit, this) then
      s"the attack by ${this.name} hits! ${target.characterName} takes $damagePerAttack damage."
    else
      s"the attack by ${this.name} does not hit."

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
    val mostDangerous = characters.filter(!_.isDead).maxBy(_.healthToAttacker)
    mostDangerous match
      case _: Rogue => distance = 0
      case _: Fighter => distance = 1
      case _: Mage => distance = 1

  def chooseTarget(characters: Buffer[Character]): Character =
    val alives = characters.filter(!_.isDead)
    val ableToHit = alives.filter(_.currentArmour <= toHit)
    if !ableToHit.isEmpty then
      ableToHit.maxBy(_.healthToAttacker)
    else
      alives.maxBy(_.healthToAttacker)


