package gameStuff

import gameStuff.Character

class Rogue(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = rogueArmourMod
  val healthMod = rogueHealthMod
  val damageMod = rogueDamageMod
  val toHitMod = mageToHitMod

  val rangedAttackName = "crossbow"
  val defendingName = "goes into a protective stance"