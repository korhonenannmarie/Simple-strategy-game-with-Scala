package gameStuff

import gameStuff.Character

class Rogue(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = rogueArmourMod
  val healthMod = rogueHealthMod
  val damageMod = rogueDamageMod
  val toHitMod = mageToHitMod

  def crossbow(target: Character) =
    if !target.isInMelee && target.takeDamage(this.damagePerAttack, this.toHit) then
      damageDone += damagePerAttack
      s"${target.characterName} takes $damagePerAttack damage.\n"
    else
      "The crossbow attack does not hit.\n"
      