import scala.collection.mutable.Buffer
class Game:

  private var roundCount: Int = 0
  private var currentScore: Int = 0
  private var isOver: Boolean = false
  private var highScores = Buffer()
  private var roundIsOver: Boolean = false
  private var waveIsOver: Boolean = false

