import scala.collection.mutable.Buffer
class Game:

  private var roundCount: Int = 0
  private var currentScore: Int = 0
  private var isOver: Boolean = false
  private var highScores = Buffer()
  private var roundIsOver: Boolean = false
  private var waveIsOver: Boolean = false

  private def setCharacters = ???
  private def newWave = ???
  private def playRound = ???
  private def usersTurn = ???
  private def monstersTurn = ???

  def playGame() =
    while !isOver do
      newWave
      while !waveIsOver do
        playRound
        roundCount += 1


  def currentRound = roundCount

  def reset(character: Character) = ???