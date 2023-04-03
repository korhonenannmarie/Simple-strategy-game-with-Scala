package gameStuff

import gameStuff.Character


class Mage(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = mageArmourMod
  val healthMod = mageHealthMod
  val damageMod = mageDamageMod