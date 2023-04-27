package gameStuff

class Fighter(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = fighterArmourMod
  val healthMod = fighterHealthMod
  val damageMod = fighterDamageMod
  val toHitMod = mageToHitMod


  def longBow(monster: Monster) =
    if !monster.isInMelee && monster.takeDamage(this.damagePerAttack, this.toHit) then
      damageDone += damagePerAttack
      s"${monster.characterName} takes $damagePerAttack damage.\n"
    else
      "The longbow attack does not hit.\n"

  def protect(target: Character) =
    target.defend()
    this.defend()


  
  
