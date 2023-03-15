package gameStuff

class Action(input: String):

  private val commandText = input.trim.toLowerCase
  private val actor       = commandText.takeWhile( _ != ' ' )
  private val verb        = commandText.drop(actor.length).takeWhile( _ != ' ' ).trim
  private val target      = commandText.drop(actor.length + verb.length).trim
  
  def execute(actor: Character): Option[String] =
    this.verb match
      // case attack => Some(actor.attack(target))
      case other => None
