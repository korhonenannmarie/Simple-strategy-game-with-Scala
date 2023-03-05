package gameStuff
import gameStuff.Game
import scala.io.StdIn.readLine

object UserInterface:
  private val game = Game()
  this.run()
  
  private def run() =
    while !game.isOver do
      val command = readLine("\nCommand:")
      val turnReport = this.game.testTurn(command)
      if turnReport != None then
        println(turnReport)
    
end UserInterface