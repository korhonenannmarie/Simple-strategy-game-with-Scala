package gameStuff

import gameStuff.Character
import scala.util.Random

class Monster(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int, distance: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = monsterArmourMod
  val healthMod = monsterHealthMod
  val damageMod = monsterDamageMod
  val toHitMod  = mageToHitMod


  def currentDis: Int = distance

  override def isInMelee: Boolean = currentDis == 0

  override def attack(target: Character) = ???





  //todo: attacking logic
  //todo: override almost all the functions lol
  //todo: distance stuff
