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
  val toHitMod  = mageToHitMod


  def currentDis: Int = distance

  override def isInMelee: Boolean = currentDis == 0

  override def attack(target: Character) = 
    if target.takeDamage(this.damagePerAttack, this.toHit) then
      "the attack hits!"
    else
      "the attack does not hit."

  def move(characters: Buffer[Character]): Unit =
    val mostDangerous = characters.filter(!_.isDead).maxBy(_.healthToAttacker)
    mostDangerous match
      case _: Rogue => distance = 0
      case _: Fighter => distance = 1
      case _: Mage => distance = 1

  def chooseTarget(characters: Buffer[Character]): Character =
    characters.filter(!_.isDead).maxBy(_.healthToAttacker)