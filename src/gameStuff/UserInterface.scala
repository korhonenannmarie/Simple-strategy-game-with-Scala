package gameStuff
import gameStuff.Game
import scala.io.StdIn.readLine

object UserInterface extends App:
  private val game = Game()
  this.testRun()

  private def run() =
    game.playGame()

  private def testRun() =
    game.testPlayGame()

        
end UserInterface