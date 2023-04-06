package gameStuff

import gameStuff.Character

class Monster(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int) 
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = monsterArmourMod
  val healthMod = monsterHealthMod
  val damageMod = monsterDamageMod