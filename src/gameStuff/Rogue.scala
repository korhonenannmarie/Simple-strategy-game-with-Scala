package gameStuff

import gameStuff.Character

class Rogue(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = rogueArmourMod
  val healthMod = rogueHealthMod
  val damageMod = rogueDamageMod
  val toHitMod = mageToHitMod

  def crossbow(monster: Monster) =
    if !monster.isInMelee && monster.takeDamage(this.damagePerAttack, this.toHit) then
      damageDone += damagePerAttack
      s"${monster.characterName} takes $damagePerAttack damage.\n"
    else
      "The crossbow attack does not hit.\n"
      